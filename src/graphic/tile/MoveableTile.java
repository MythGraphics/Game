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
        imgset.setDirection(direction);
        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - stepSize >= 0)          { y -= stepSize; }}
            case DOWN  -> { if (y + stepSize <= maxPoint.y) { y += stepSize; }}
            case LEFT  -> { if (x - stepSize >= 0)          { x -= stepSize; }}
            case RIGHT -> { if (x + stepSize <= maxPoint.x) { x += stepSize; }}
        }
    }

    /**
     * Erstellt ein MoveableTile, das für ALLE Richtungen dieselbe Animation nutzt.
     * @param bType
     * @param x
     * @param y
     * @param stepSize
     * @param maxPoint
     * @param data
     * @return
     */
    public static MoveableTile createSingleAnimation(int x, int y, IsBlockType bType,
                                                     int stepSize, Point maxPoint,
                                                     AnimationData data) {
        Direction[] directionSet = Direction.values();
        AnimationPlayer[] playerSet = new AnimationPlayer[directionSet.length];
        AnimationPlayer sharedPlayer = new AnimationPlayer(data);
        for (int i = 0; i < directionSet.length; i++) {
            playerSet[i] = sharedPlayer;
        }
        return new MoveableTile( x, y, bType, stepSize, maxPoint, new DirectionalImage( playerSet ));
    }

    /**
     * Erstellt ein MoveableTile, das für ALLE Richtungen dasselbe Bild nutzt.
     * @param bType
     * @param x
     * @param y
     * @param stepSize
     * @param maxPoint
     * @param image
     * @return
     */
    public static MoveableTile createSingleTile(int x, int y, IsBlockType bType,
                                                int stepSize, Point maxPoint,
                                                HasImage image) {
        Direction[] directionSet = Direction.values();
        HasImage[] set = new HasImage[directionSet.length];
        for (int i = 0; i < directionSet.length; i++) {
            set[i] = image;
        }
        return new MoveableTile( x, y, bType, stepSize, maxPoint, new DirectionalImage( set ));
    }

}
