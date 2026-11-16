/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.block.Collidable;
import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import graphic.tile.IsBlockTile;
import graphic.tile.Portal;
import java.awt.Point;

public class VectorPortal extends Portal {

    public VectorPortal(IsBlockTile tile) {
        super(tile);
    }

    public VectorPortal(IsBlockTile tile, Point target) {
        super(tile, target);
    }

    public VectorPortal(IsBlockTile tile, int x, int y) {
        super(tile, x, y);
    }

    public VectorPortal(IsBlockTile tile, Portal other) {
        super(tile, other);
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        if ( isActive() && initiator instanceof Portable portable && portable.isPortable() ) {
            portable.resetPortalCooldown();
            return super.onCollision(initiator, map, handler);
        }

        return true;
    }

}
