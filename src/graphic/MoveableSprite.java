/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.BlockType;
import java.awt.Dimension;
import java.awt.Point;

public class MoveableSprite extends AnimatedBlock implements Moveable {

    final Animation[] aniset;
    final Point maxPoint;
    final int blockSize;

    public MoveableSprite(Animation[] aniset, int x, int y, int blockSize, BlockType type, Point maxPoint) {
        super( aniset[0], new Point(x, y), new Dimension(blockSize, blockSize), type );
        this.aniset = aniset;
        this.maxPoint = maxPoint;
        this.blockSize = blockSize;
    }

    public MoveableSprite(Animation[] aniset, Point pos, Dimension dim, int blockSize, BlockType type, Point maxPoint) {
        super(aniset[0], pos, dim, type);
        this.aniset = aniset;
        this.maxPoint = maxPoint;
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public void move(Direction direction) {
        // Update der aktuellen Animation in der Superklasse
        setAnimation( aniset[direction.ordinal()] );
        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y > 0)          { y -= blockSize; }}
            case DOWN  -> { if (y < maxPoint.y) { y += blockSize; }}
            case LEFT  -> { if (x > 0)          { x -= blockSize; }}
            case RIGHT -> { if (x < maxPoint.x) { x += blockSize; }}
        }
    }

    public static Animation[] copyAnimationSet(Animation[] original) {
        Animation[] copy = new Animation[original.length];
        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) {
                copy[i] = original[i].copy();
            }
        }
        return copy;
    }

}
