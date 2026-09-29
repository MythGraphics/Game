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

public interface IsDeadOrAliveTile extends HasOverlayResource {

    boolean isAlive();
    void setDead();
    void setAlive();

}
