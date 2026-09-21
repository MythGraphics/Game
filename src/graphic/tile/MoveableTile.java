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

import static graphic.Direction.RIGHT;
import graphic.*;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

public class MoveableTile extends BlockTile implements Moveable {

    final DirectionalImage imgset;
    final Point maxPoint;
    final int stepSize;

    public MoveableTile(int x, int y, IsBlockType bType,
                        int stepSize, Point maxPoint, DirectionalImage imgset) {
        super( x, y, bType, () -> imgset.getImage() );
        this.imgset   = imgset;
        this.maxPoint = maxPoint;
        this.stepSize = stepSize;
    }

    public MoveableTile(int x, int y, int width, int height, IsBlockType bType,
                        int stepSize, Point maxPoint, DirectionalImage imgset) {
        super( x, y, width, height, bType, () -> imgset.getImage() );
        this.imgset   = imgset;
        this.maxPoint = maxPoint;
        this.stepSize = stepSize;
    }

    public MoveableTile(Point pos, Dimension dim, IsBlockType bType,
                        int stepSize, Point maxPoint, DirectionalImage imgset) {
        this(pos.x, pos.y, dim.width, dim.height, bType, stepSize, maxPoint, imgset);
    }

    public int getStepSize() {
        return stepSize;
    }

    public Direction getCurrentDirection() {
        return imgset.getDirection();
    }

    @Override
    public void move(Direction direction) {
        if (direction == null) {
            return;
        }

        imgset.setDirection(direction);
        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - stepSize >= 0)          { y -= stepSize; }}
            case DOWN  -> { if (y + stepSize <= maxPoint.y) { y += stepSize; }}
            case LEFT  -> { if (x - stepSize >= 0)          { x -= stepSize; }}
            case RIGHT -> { if (x + stepSize <= maxPoint.x) { x += stepSize; }}
        }
    }

}
