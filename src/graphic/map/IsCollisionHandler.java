/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.block.Collidable;

@FunctionalInterface
public interface IsCollisionHandler {

    void fireEvent(GameMap map, Collidable initiator, Collidable target);

}
