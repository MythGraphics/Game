/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import static graphic.Direction.*;
import graphic.DirectionalImage;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

// hin und her pendelnde Fliese
public class ShuttleTile extends AutoMoveableTile {

    public ShuttleTile(Direction initialDirection,
                       int x, int y, IsBlockType bType,
                       int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(initialDirection, x, y, bType, stepSize, maxPoint, imgset);
    }

    public ShuttleTile(Direction initialDirection,
                       int x, int y, int width, int height, IsBlockType bType,
                       int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(initialDirection, x, y, width, height, bType, stepSize, maxPoint, imgset);
    }

    public ShuttleTile(Direction initialDirection,
                       Point pos, Dimension dim, IsBlockType bType,
                       int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(initialDirection, pos.x, pos.y, dim.width, dim.height, bType, stepSize, maxPoint, imgset);
    }

    @Override
    public void move(Direction direction) {
        super.move(direction);
        // Bewegungslogik: Umkehren, wenn Grenzen erreicht
        switch (direction) {
            case UP    -> { if (y <= start.y)    { setDirection( invert( Direction.UP    )); }}
            case DOWN  -> { if (y >= maxPoint.y) { setDirection( invert( Direction.DOWN  )); }}
            case LEFT  -> { if (x <= start.x)    { setDirection( invert( Direction.LEFT  )); }}
            case RIGHT -> { if (x >= maxPoint.x) { setDirection( invert( Direction.RIGHT )); }}
        }
    }

}
