/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public interface IsBlock {

    int getX();
    int getY();
    int getWidth();
    int getHeight();
    IsBlockType getBlockType();

}
