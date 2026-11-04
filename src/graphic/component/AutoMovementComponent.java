/*
 *
 */

package graphic.component;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import graphic.block.HasMinMaxPoint;
import graphic.map.GameMap;
import java.awt.Point;

public class AutoMovementComponent extends DirectionalMovementComponent implements HasMinMaxPoint {

    private final Point min;
    private final Point max;

    public AutoMovementComponent(Direction initialDirection, int stepSize) {
        this( initialDirection, stepSize, new Point( 0, 0 ), new Point( Integer.MAX_VALUE, Integer.MAX_VALUE ));
    }

    public AutoMovementComponent(Direction initialDirection, int stepSize, GameMap map) {
        this( initialDirection, stepSize, new Point( 0, 0 ), map.getMaxPoint() );
    }

    public AutoMovementComponent(Direction initialDirection, int stepSize, Point minPoint, Point maxPoint) {
        super(initialDirection, stepSize);
        this.min = minPoint;
        this.max = maxPoint;
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

}
