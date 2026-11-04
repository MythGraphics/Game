/*
 *
 */

package graphic.component;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.block.Block;
import graphic.block.Collidable;
import graphic.block.IsMoveableBlock;
import graphic.map.GameMap;
import java.awt.Point;

public class PortalComponent {

    public final int DEFAULT_COLLISION_DELAY = 30;

    private PortalComponent otherPortal;
    private Point target                = new Point();
    private boolean targetIsOtherPortal = false;
    private int collisionDelay          = 0;

    public PortalComponent(Point target) {
        this.target         = target;
        targetIsOtherPortal = false;
    }

    public PortalComponent(int targetX, int targetY) {
        this.target.x       = targetX;
        this.target.y       = targetY;
        targetIsOtherPortal = false;
    }

    public PortalComponent(Block other) {
        setTarget(other);
    }

    public void setRandomTarget(GameMap map) {
        setTarget( map.getRandomSpace() );
    }

    public final void setTarget(Block other) {
        PortalComponent portal = other.getComponent(PortalComponent.class);
        if (portal != null) {
            targetIsOtherPortal = true;
            this.otherPortal    = portal;
            target.x            = other.getX();
            target.y            = other.getY();
        } else {
            targetIsOtherPortal = false;
        }
    }

    public void setTarget(Point target) {
        targetIsOtherPortal = false;
        this.target = target;
    }

    public void setTarget(int x, int y) {
        targetIsOtherPortal = false;
        target.x = x;
        target.y = y;
    }

    public Point getTarget() {
        return target;
    }

    public void setCollisionDelay(int delay) {
        this.collisionDelay = delay;
    }

    public void moveThrough(IsMoveableBlock block) {
        Point destination = getTarget();
        if (targetIsOtherPortal && otherPortal != null) {
            otherPortal.setCollisionDelay(DEFAULT_COLLISION_DELAY);
        }
        block.setPos(destination.x, destination.y);
    }

    public void onCollision(Collidable initiator) {
        if (collisionDelay > 0) {
            --collisionDelay;
            return;
        }
        if (initiator instanceof IsMoveableBlock block) {
            moveThrough(block);
        }
    }

}
