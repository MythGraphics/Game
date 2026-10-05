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

import graphic.map.IsBlock;

public interface IsDeadOrAliveTile extends IsBlock, HasOverlayResource {

    boolean isAlive();
    void setDead();
    void setAlive();

}
