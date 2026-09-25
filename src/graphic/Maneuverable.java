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

public interface Maneuverable extends Moveable {

    void tilt(Direction direction);
    void step(Direction direction);

}
