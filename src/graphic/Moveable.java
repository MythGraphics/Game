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

    void move(Direction direction);
    int getStepSize();
    int getMaxX();
    int getMaxY();
    void setX(int x);
    void setY(int y);
    Direction getCurrentDirection();

}
