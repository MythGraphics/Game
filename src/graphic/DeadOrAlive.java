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

import graphic.HasImage;

public interface DeadOrAlive extends HasImage {

    void swap();
    void dead();
    void alive();
    boolean isDead();

}
