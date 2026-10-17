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

public interface IsMoveableBlock extends IsBlock, Moveable {

    void setX(int x);
    void setY(int y);

}
