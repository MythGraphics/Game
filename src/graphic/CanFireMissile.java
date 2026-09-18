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

import graphic.map.IsBlock;

public interface CanFireMissile extends IsBlock {

    DirectionalImage getMissileImage();
    boolean canFireMissile();

}
