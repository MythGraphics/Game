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

import graphic.*;
import graphic.map.BlockTile;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

public class MoveableTile extends BlockTile implements Moveable {

    final HasImage[] imgset;
    final Point maxPoint;
    final int blockSize;

    public MoveableTile(int x, int y, int blockSize, IsBlockType bType,
                        Point maxPoint, HasImage[] imgset) {
        super( x, y, blockSize, blockSize, bType, ( imgset != null && imgset.length > 0 ) ? imgset[0] : () -> null );
        this.imgset    = imgset;
        this.maxPoint  = maxPoint;
        this.blockSize = blockSize;
    }

    public MoveableTile(Point pos, Dimension dim, int blockSize, IsBlockType bType,
                        Point maxPoint, HasImage[] imgset) {
        super( pos.x, pos.y, dim.width, dim.height, bType, ( imgset != null && imgset.length > 0 ) ? imgset[0] : () -> null );
        this.imgset    = imgset;
        this.maxPoint  = maxPoint;
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public void move(Direction direction) {
        // Update der aktuellen Animation
        if ( imgset != null && direction.ordinal() < imgset.length && imgset[ direction.ordinal() ] != null ) {
            setImage( imgset[ direction.ordinal() ] );
        }
        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - blockSize >= 0)          { y -= blockSize; }}
            case DOWN  -> { if (y + blockSize <= maxPoint.y) { y += blockSize; }}
            case LEFT  -> { if (x - blockSize >= 0)          { x -= blockSize; }}
            case RIGHT -> { if (x + blockSize <= maxPoint.x) { x += blockSize; }}
        }
    }

    /**
     * Erstellt ein MoveableTile, der für ALLE Richtungen dieselbe Animation nutzt.
     * @param data
     * @param bType
     * @param x
     * @param y
     * @param blockSize
     * @param maxPoint
     * @return
     */
    public static MoveableTile createSingleAnimation(AnimationData data, IsBlockType bType,
                                                     int x, int y, int blockSize, Point maxPoint) {
        Direction[] directionSet = Direction.values();
        AnimationPlayer[] playerSet = new AnimationPlayer[directionSet.length];
        AnimationPlayer sharedPlayer = new AnimationPlayer(data);
        for (int i = 0; i < directionSet.length; i++) {
            playerSet[i] = sharedPlayer;
        }
        return new MoveableTile(x, y, blockSize, bType, maxPoint, playerSet);
    }

}
