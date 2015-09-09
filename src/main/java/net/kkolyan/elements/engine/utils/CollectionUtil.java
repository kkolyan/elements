package net.kkolyan.elements.engine.utils;

import java.util.*;

/**
 * @author nplekhanov
 */
public class CollectionUtil {

    public static <K,V> Map<K,V> mergedMap(Map<K,? extends V> base, Map<K,? extends V> overrides) {
        Map<K, V> map = new HashMap<>();
        map.putAll(base);
        map.putAll(overrides);
        return map;
    }

    public static String join(Collection<?> coll, String delimiter) {
        if (coll.isEmpty()) {
            return "";
        }
        Iterator<?> it = coll.iterator();
        String s = String.valueOf(it.next());
        while (it.hasNext()) {
            s += delimiter;
            s += String.valueOf(it.next());
        }
        return s;
    }

    public static <S,T> List<T> transform(final List<? extends S> source, final Function<S,T> transformer) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                return transformer.apply(source.get(index));
            }

            @Override
            public int size() {
                return source.size();
            }
        };
    }

    public static <S,T> Collection<T> transform(final Collection<? extends S> source, final Function<S,T> transformer) {
        return new AbstractCollection<T>() {
            @Override
            public Iterator<T> iterator() {
                final Iterator<? extends S> it = source.iterator();
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public T next() {
                        return transformer.apply(it.next());
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public int size() {
                return source.size();
            }
        };
    }

    public static <T> List<T> concatenate(Collection<? extends T> a, Collection<? extends T> b) {
        List<T> list = new ArrayList<T>();
        list.addAll(a);
        list.addAll(b);
        return list;
    }

    public static <T> List<T> concatenate(Collection<? extends Collection<T>> lists) {
        List<T> list = new ArrayList<T>();
        for (Collection<T> c: lists) {
            list.addAll(c);
        }
        return list;
    }

    public static <T> List<T> lazyConcatenate(final Collection<List<T>> lists) {
        return new LazyConcatenatedList<T>(lists);
    }

    public static <T> List<T> subListByType(Collection<?> input, Class<T> type) {
        List<T> result = new ArrayList<T>();
        for (Object s: input) {
            if (type.isInstance(s)) {
                T t = type.cast(s);
                result.add(t);
            }
        }
        return result;
    }

    private static class LazyConcatenatedList<T> extends AbstractList<T> {
        private final Collection<List<T>> lists;

        public LazyConcatenatedList(Collection<List<T>> lists) {
            this.lists = lists;
        }

        @Override
        public T get(int index) {
            int localIndex = index;
            for (List<? extends T> list: lists) {
                if (list == null) {
                    continue;
                }
                if (list.size() > localIndex) {
                    return list.get(localIndex);
                }
                localIndex -= list.size();
            }
            throw new IndexOutOfBoundsException(""+index);
        }

        @Override
        public int size() {
            int n = 0;
            for (List<? extends T> list: lists) {
                if (list == null) {
                    continue;
                }
                n += list.size();
            }
            return n;
        }

        @Override
        public Iterator<T> iterator() {
            return new ConcatenatedIterator<T>(lists.iterator());
        }
    }

    private static class ConcatenatedIterator<T> implements Iterator<T> {
        private Iterator<? extends Iterable<T>> masterIterator;
        private Iterator<T> current;

        private ConcatenatedIterator(Iterator<? extends Iterable<T>> masterIterator) {
            this.masterIterator = masterIterator;
        }

        @Override
        public boolean hasNext() {
            while (true) {
                if (current != null && current.hasNext()) {
                    return true;
                }
                if (!masterIterator.hasNext()) {
                    return false;
                }
                Iterable<T> next = masterIterator.next();
                if (next != null) {
                    current = next.iterator();
                }
            }
        }

        @Override
        public T next() {
            return current.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
