/*
 *
 */

package graphic.block;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class Block implements Collidable {

    protected int x, y;
    protected int width, height;
    protected IsBlockType bType;

    public Block(int x, int y, int width, int height, IsBlockType bType) {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.bType  = bType;
    }

    public Block(int x, int y, int blockSize, IsBlockType bType) {
        this(x, y, blockSize, blockSize, bType);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(Point pos) {
        x = pos.x;
        y = pos.y;
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public IsBlockType getBlockType() {
        return bType;
    }

    public void setBlockType(IsBlockType bType) {
        this.bType = bType;
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        handler.fireEvent(map, initiator, this);
        return getBlockType().isPassable();
    }

    @Override
    public String toString() {
        return "Block " + getBlockType() + " at pixel " + x + ", " + y + " with width/height " + width + "/" + height;
    }

}
