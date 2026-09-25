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
import static graphic.Direction.RIGHT;
import graphic.DirectionalImage;
import graphic.Maneuverable;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

public class MoveableTile extends BlockTile implements Maneuverable {

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

    @Override
    public int getStepSize() {
        return stepSize;
    }

    public Point getMaxPoint() {
        return maxPoint;
    }

    @Override
    public int getMaxX() {
        return maxPoint.x;
    }

    @Override
    public int getMaxY() {
        return maxPoint.y;
    }

    @Override
    public Direction getCurrentDirection() {
        return imgset.getDirection();
    }

    /**
     * Dreht und bewegt den Spieler in die gegebene Richtung.
     * @param direction
     */
    @Override
    public void move(Direction direction) {
        tilt(direction);
        step(direction);
    }

    /**
     * Dreht den Spieler in die gegebene Richtung.
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
     * Bewegt den Spieler in die gegebene Richtung ohne ihn zu drehen.
     * @param direction
     */
    @Override
    public void step(Direction direction) {
        if (direction == null) {
            return;
        }

        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - stepSize >= 0)          { y -= stepSize; }}
            case DOWN  -> { if (y + stepSize <= maxPoint.y) { y += stepSize; }}
            case LEFT  -> { if (x - stepSize >= 0)          { x -= stepSize; }}
            case RIGHT -> { if (x + stepSize <= maxPoint.x) { x += stepSize; }}
        }
    }

}
