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

import java.awt.Point;

public interface HasMinMaxPoint {

    int getMaxX();
    int getMaxY();
    int getMinX();
    int getMinY();
    Point getMinPoint();
    Point getMaxPoint();

}
