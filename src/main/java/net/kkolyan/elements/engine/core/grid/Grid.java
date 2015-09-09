package net.kkolyan.elements.engine.core.grid;

import net.kkolyan.elements.engine.core.Debug;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.CollectionUtil;
import net.kkolyan.elements.engine.utils.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class Grid<T> implements Field<T> {
    private Map<Coordinates,List<T>> cells = new HashMap<Coordinates, List<T>>();
    private double resolution;
    private boolean rebuildRequired;
    private Function<T,? extends Located> locationResolver;
    private Map<Object,List<T>> bucketsByObject = new IdentityHashMap<Object, List<T>>();


    public Grid(double resolution, Function<T, ? extends Located> locationResolver) {
        this.locationResolver = locationResolver;
        this.resolution = resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
        if (!cells.isEmpty()) {
            rebuildRequired = true;
        }
    }

    @Override
    public List<T> lookup(Located areaCenter, Located areaSize) {
        Collection<List<T>> neighbourCells = new ArrayList<List<T>>();
        performLookup(areaCenter, areaSize, neighbourCells);
        return CollectionUtil.concatenate(neighbourCells);
    }

    private void performLookup(Located areaCenter, Located areaSize, Collection<? super List<T>> neighbourCells) {
        if (rebuildRequired) {
            throw new IllegalStateException("rebuild required");
        }

        Debug.drawRectangle(areaCenter, areaSize, "red");

        int x0 = (int) Math.floor((areaCenter.getX() - areaSize.getX()/2) / resolution);
        int x1 = (int) Math.floor((areaCenter.getX() + areaSize.getX()/2) / resolution);
        int y0 = (int) Math.floor((areaCenter.getY() - areaSize.getY()/2) / resolution);
        int y1 = (int) Math.floor((areaCenter.getY() + areaSize.getY()/2) / resolution);

        for (int x = x0; x <= x1; x ++) {
            for (int y = y0; y <= y1; y ++) {
                neighbourCells.add(getCell(x, y));
            }
        }
    }

    @Override
    public List<T> lookupAtLongArea(Located longAreaBegin, Located longAreaEnd, double thickness) {
        if (thickness < 1) {
            throw new IllegalStateException("so small objects can't be used due to performance issues. or there are mistake");
        }
        Vector begin = new Vector(longAreaBegin);
        Vector path = begin.vectorTo(longAreaEnd);
        Collection<List<T>> neighbourCells = Collections.newSetFromMap(new IdentityHashMap<List<T>, Boolean>());
        int samples = (int) (4.0 * path.magnitude() / thickness);
        for (int i = 0; i < samples; i ++) {
            Vector sampleCenter = begin.getTranslated(path.getMultiplied(1.0 * i / samples));
            performLookup(sampleCenter, new Vector(thickness * 2, thickness * 2), neighbourCells);
        }
        return CollectionUtil.concatenate(neighbourCells);
    }

    private List<T> getCell(Located o) {
        int x = (int) Math.floor(o.getX() / resolution);
        int y = (int) Math.floor(o.getY() / resolution);
        return getCell(x, y);
    }

    private List<T> getCell(int x, int y) {
        Coordinates coordinates = new Coordinates(x, y);
        List<T> cell = cells.get(coordinates);
        if (cell == null) {
            cell = new ArrayList<T>();
            cells.put(coordinates, cell);
        }
        return cell;
    }

    @Override
    public void rebuild() {
        List<T> objects = CollectionUtil.concatenate(cells.values());
        for (List<T> cell: cells.values()) {
            cell.clear();
        }
        bucketsByObject.clear();
        for (T o: objects) {
            addInternal(o);
        }
        rebuildRequired = false;
    }

    private void addInternal(T o) {
        List<T> cell = getCell(locationResolver.apply(o));
        cell.add(o);
        bucketsByObject.put(o, cell);
    }

    @Override
    public void add(T o) {
        addInternal(o);
    }

    @Override
    public void update(T t) {
        List<T> oldCell = bucketsByObject.remove(t);
        if (oldCell != null) {
            oldCell.remove(t);
            addInternal(t);
        }
    }

    @Override
    public int getObjectsCount() {
        return bucketsByObject.size();
    }

    @Override
    public void clear() {
        cells.clear();
        bucketsByObject.clear();
    }
}
