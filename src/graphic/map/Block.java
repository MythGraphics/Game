/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import java.awt.Dimension;
import java.awt.Point;

public abstract class Block implements HasImage {

    public final int width;
    public final int height;

    protected BlockType type;
    protected int x;
    protected int y;

    public Block(Point pos, Dimension dim, BlockType type) {
        this(pos.x, pos.y, dim.width, dim.height, type);
    }

    public Block(int x, int y, int blockSize, BlockType type) {
        this(x, y, blockSize, blockSize, type);
    }

    public Block(int x, int y, int width, int height, BlockType type) {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.type = type;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType bType) {
        this.type = bType;
    }

}
