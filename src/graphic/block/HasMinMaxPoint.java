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
import java.awt.Point;

public interface HasMinMaxPoint extends Moveable {

    int getMaxX();
    int getMaxY();
    int getMinX();
    int getMinY();
    Point getMinPoint();
    Point getMaxPoint();

}
