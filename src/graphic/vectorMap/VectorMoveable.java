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

import graphic.Moveable;

public interface VectorMoveable extends Moveable, HasDirectionAngle {

    void rotate(float phi);
    void accelerate(float vx, float vy);
    void accelerate(float dv);

}
