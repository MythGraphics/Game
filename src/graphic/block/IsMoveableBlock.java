/*
 *
 */

package graphic.block;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Moveable;

public interface IsMoveableBlock extends Collidable, Moveable {

    void setX(int x);
    void setY(int y);
    void setPos(int x, int y);

}
