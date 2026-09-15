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

public interface CanFireProjectile extends IsBlock {

    DirectionalImage getProjectileImage();
    boolean canFireProjectile();

}
