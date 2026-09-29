/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import graphic.DirectionalImage;
import graphic.map.GameMap;
import graphic.map.IsBlock;

public interface CanFireMissile extends IsBlock {

    boolean hasMissile();
    DirectionalImage getMissileImage();
    void fireMissile(GameMap map, Direction direction);

}
