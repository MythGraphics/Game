/*
 *
 */

package graphic.map;

import graphic.block.Collidable;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

@FunctionalInterface
public interface IsCollisionHandler {

    void fireEvent(GameMap map, Collidable initiator, Collidable target);

}
