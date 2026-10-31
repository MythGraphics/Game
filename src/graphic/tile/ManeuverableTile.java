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
import graphic.block.HasMinMaxPoint;
import graphic.block.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

// Player
@SuppressWarnings("CloneableImplementsClone")
public class ManeuverableTile extends BlockTile implements IsManeuverableTile, HasMinMaxPoint {

    public final DirectionalImage imgset;
    public final Point min; // start
    public final Point max;
    public final int stepSize;

    public ManeuverableTile(int x, int y, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super( x, y, bType, () -> imgset.getImage() );
        this.imgset     = imgset;
        this.min        = new Point(x, y);
        this.max        = max;
        this.stepSize   = stepSize;
    }

    public ManeuverableTile(int x, int y, int width, int height, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super( x, y, width, height, bType, () -> imgset.getImage() );
        this.imgset     = imgset;
        this.min        = new Point(x, y);
        this.max        = max;
        this.stepSize   = stepSize;
    }

    public ManeuverableTile(Point start, Dimension dim, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super( start.x, start.y, dim.width, dim.height, bType, () -> imgset.getImage() );
        this.imgset     = imgset;
        this.min        = start;
        this.max        = max;
        this.stepSize   = stepSize;
    }

    public int getStepSize() {
        return stepSize;
    }

    @Override
    public int getMinX() {
        return min.x;
    }

    @Override
    public int getMinY() {
        return min.y;
    }

    @Override
    public Point getMinPoint() {
        return min;
    }

    @Override
    public Point getMaxPoint() {
        return max;
    }

    @Override
    public int getMaxX() {
        return max.x;
    }

    @Override
    public int getMaxY() {
        return max.y;
    }

    @Override
    public Direction getViewDirection() {
        return imgset.getDirection();
    }

    /**
     * Dreht und bewegt die Fliese in die gegebene Richtung.
     * @param direction
     */
    @Override
    public void move(Direction direction) {
        tilt(direction);
        step(direction);
    }

    /**
     * Bewegt die Fliese in die aktuelle Blickrichtung.
     */
    @Override
    public void move() {
        step( getViewDirection() );
    }

    /**
     * Dreht die Fliese in die gegebene Richtung.
     * @param direction
     */
    @Override
    public void tilt(Direction direction) {
        if (direction == null) {
            return;
        }
        imgset.setDirection(direction);
    }

    /**
     * Bewegt die Fliese in die gegebene Richtung ohne sie zu drehen.
     * @param direction
     */
    @Override
    public void step(Direction direction) {
        if (direction == null) {
            return;
        }

        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - stepSize >= min.y)  { y -= stepSize; }}
            case DOWN  -> { if (y + stepSize <= max.y)  { y += stepSize; }}
            case LEFT  -> { if (x - stepSize >= min.x)  { x -= stepSize; }}
            case RIGHT -> { if (x + stepSize <= max.x)  { x += stepSize; }}
        }
    }

}
