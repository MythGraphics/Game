/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public interface Moveable {

    void rotate(float delta_phi);
    void accelerate(float vx, float vy);
    void accelerate(float dv);

}
