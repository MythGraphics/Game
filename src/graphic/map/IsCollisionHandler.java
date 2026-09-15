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

public interface IsCollisionHandler {

    void fireEvent(GameMap map, Block initiator, Block target);

}
