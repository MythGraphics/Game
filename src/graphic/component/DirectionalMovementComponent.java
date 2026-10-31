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
import static graphic.Direction.*;
import graphic.block.Block;
import graphic.block.HasMinMaxPoint;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;

public class DirectionalMovementComponent implements Moveable {

    private Direction direction = RIGHT;
    private int stepSize        = DEFAULT_TILE_SIZE;

    public DirectionalMovementComponent(Direction initialDirection, int stepSize) {
        this.direction = initialDirection;
        this.stepSize  = stepSize;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void move(Block owner) {
        int x = owner.getX();
        int y = owner.getY();
        if (owner instanceof HasMinMaxPoint minmax) {
            int minX = minmax.getMinX();
            int minY = minmax.getMinY();
            int maxX = minmax.getMaxX();
            int maxY = minmax.getMaxY();
            switch (direction) {
                case UP    -> { if (y - stepSize >= minY)  { owner.setY(y - stepSize); }}
                case DOWN  -> { if (y + stepSize <= maxY)  { owner.setY(y + stepSize); }}
                case LEFT  -> { if (x - stepSize >= minX)  { owner.setX(x - stepSize); }}
                case RIGHT -> { if (x + stepSize <= maxX)  { owner.setX(x + stepSize); }}
            }
        } else {
            switch (direction) {
                case UP    -> owner.setY(y - stepSize);
                case DOWN  -> owner.setY(y + stepSize);
                case LEFT  -> owner.setX(x - stepSize);
                case RIGHT -> owner.setX(x + stepSize);
            }
        }
    }

}
