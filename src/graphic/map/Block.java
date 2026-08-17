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

    protected IsMapTile tile;
    protected int x;
    protected int y;

    public Block(Point pos, Dimension dim, IsMapTile tile) {
        this(pos.x, pos.y, dim.width, dim.height, tile);
    }

    public Block(int x, int y, int blockSize, IsMapTile tile) {
        this(x, y, blockSize, blockSize, tile);
    }

    public Block(int x, int y, int width, int height, IsMapTile tile) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.tile  = tile;
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

    public IsMapTile getType() {
        return tile;
    }

    public void setType(IsMapTile tile) {
        this.tile = tile;
    }

}
