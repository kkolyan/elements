package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.templates.Sprite;

/**
 * @author nplekhanov
 */
public class Weapon extends Sprite {
    private String waitImageSetId;
    private String shotImageSetId;

    public void setWaitImageSetId(String waitImageSetId) {
        setImageSetId(waitImageSetId);
        this.waitImageSetId = waitImageSetId;
    }

    public void setShotImageSetId(String shotImageSetId) {
        this.shotImageSetId = shotImageSetId;
    }

    @Override
    public void handleAnimationEnd() {
        setImageSetId(waitImageSetId);
        setFrameIndex(0);
    }

    public void shot() {
        setImageSetId(shotImageSetId);
        setFrameIndex(0);
    }
}
