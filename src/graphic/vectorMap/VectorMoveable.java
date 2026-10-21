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

import graphic.AutoMoveable;

public interface VectorMoveable extends AutoMoveable, HasDirectionAngle {

    void rotate(float phi);
    void accelerate(float vx, float vy);
    void accelerate(float dv);

}
