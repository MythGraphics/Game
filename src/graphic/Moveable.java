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

public interface Moveable extends IsBlock {

    Direction getCurrentDirection();
    int getStepSize();
    void move(); // Bewegung in Blickrichtung
    void move(Direction direction);
    int getMaxX();
    int getMaxY();
    void setX(int x);
    void setY(int y);

}
