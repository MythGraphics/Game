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

public interface AutoMoveable extends Moveable {

    void move();
    void moveRandom();
    void start();
    void stop();

}
