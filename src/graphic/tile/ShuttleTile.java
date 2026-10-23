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
import graphic.block.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

// hin und her pendelnde Fliese
@SuppressWarnings("CloneableImplementsClone")
public class ShuttleTile extends ManeuverableTile {

    public final Direction initialDirection;

    public ShuttleTile(Direction initialDirection,
                       int x, int y, IsBlockType bType,
                       int stepSize, Point max, DirectionalImage imgset) {
        super(x, y, bType, stepSize, max, imgset);
        this.initialDirection = initialDirection;
        setDirection(initialDirection);
    }

    public ShuttleTile(Direction initialDirection,
                       int x, int y, int width, int height, IsBlockType bType,
                       int stepSize, Point max, DirectionalImage imgset) {
        super(x, y, width, height, bType, stepSize, max, imgset);
        this.initialDirection = initialDirection;
        setDirection(initialDirection);
    }

    public ShuttleTile(Direction initialDirection,
                       Point pos, Dimension dim, IsBlockType bType,
                       int stepSize, Point max, DirectionalImage imgset) {
        super(pos, dim, bType, stepSize,  max, imgset);
        this.initialDirection = initialDirection;
        setDirection(initialDirection);
    }

    public final void setDirection(Direction direction) {
        imgset.setDirection(direction);
    }

    @Override
    public void step(Direction direction) {
        super.step(direction);
        // Bewegungslogik: Umkehren, wenn Grenzen erreicht
        switch (direction) {
            case UP    -> { if (y <= min.y) { setDirection( invert( Direction.UP    )); }}
            case DOWN  -> { if (y >= max.y) { setDirection( invert( Direction.DOWN  )); }}
            case LEFT  -> { if (x <= min.x) { setDirection( invert( Direction.LEFT  )); }}
            case RIGHT -> { if (x >= max.x) { setDirection( invert( Direction.RIGHT )); }}
        }
    }

}
