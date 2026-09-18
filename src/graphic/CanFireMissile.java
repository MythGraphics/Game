/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.GameMap;
import graphic.map.IsBlock;

public interface CanFireMissile extends IsBlock {

    boolean canFireMissile();
    DirectionalImage getMissileImage();
    void fireMissile(GameMap map, Direction direction);

}
