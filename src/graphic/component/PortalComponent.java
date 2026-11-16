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
import graphic.vectorMap.Portable;
import java.awt.Point;

public class PortalComponent {

    public final static int DEFAULT_COLLISION_DELAY = 30;

    private Point target = new Point();

    public PortalComponent(Point target) {
        this.target = target;
    }

    public PortalComponent(int targetX, int targetY) {
        this.target.x = targetX;
        this.target.y = targetY;
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
            target.x = other.getX();
            target.y = other.getY();
        }
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public void setTarget(int x, int y) {
        target.x = x;
        target.y = y;
    }

    public Point getTarget() {
        return target;
    }

    public void moveThrough(IsMoveableBlock block) {
        Point destination = getTarget();
        block.setPos(destination.x, destination.y);
    }

    public void onCollision(Collidable initiator) {
        if (initiator instanceof Portable portable) {
            if ( portable.isPortable() ) {
                portable.resetPortalCooldown();
            } else {
                return;
            }
        }
        if (initiator instanceof IsMoveableBlock block) {
            moveThrough(block);
        }
    }

}
