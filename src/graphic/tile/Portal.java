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

import graphic.block.Collidable;
import graphic.block.IsBlockType;
import graphic.block.IsMoveableBlock;
import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Portal implements IsBlockTile {

    public final int DEFAULT_COLLISION_DELAY = 30;

    private final IsBlockTile tile;

    private Point target = new Point();
    private Portal other;
    private boolean targetIsOtherPortal;
    private boolean active = true;
    private int collisionDelay = 0;

    public Portal(Point target, IsBlockTile tile) {
        this.tile           = tile;
        this.target         = target;
        targetIsOtherPortal = false;
    }

    public Portal(int x, int y, IsBlockTile tile) {
        this.tile           = tile;
        this.target.x       = x;
        this.target.y       = y;
        targetIsOtherPortal = false;
    }

    public Portal(IsBlockTile tile, Portal other) {
        this.tile = tile;
        setTarget(other);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setCollisionDelay(int delay) {
        this.collisionDelay = delay;
    }

    public void setRandomTarget(GameMap map) {
        target = map.getRandomSpace();
    }

    public final void setTarget(Portal other) {
        targetIsOtherPortal = true;
        if (other != null) {
            this.other      = other;
            active          = true;
            other.active    = true;
        } else {
            this.other      = this;
            active          = false;
        }
    }

    public void setTarget(int x, int y) {
        targetIsOtherPortal = false;
        target.x = x;
        target.y = y;
    }

    public IsBlockTile getTile() {
        return tile;
    }

    public Point getTargetAsPoint() {
        return targetIsOtherPortal ? new Point( other.getX(), other.getY() ) : target;
    }

    public Portal getTargetAsPortal() {
        return targetIsOtherPortal ? other : new Portal(target, tile);
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        tile.draw(g2d, offsetX, offsetY);
    }

    public void moveThrough(IsMoveableBlock block) {
        if (targetIsOtherPortal) {
            Portal destination = getTargetAsPortal();
            destination.setCollisionDelay(DEFAULT_COLLISION_DELAY);
            block.setPos( destination.getX(), destination.getY() );
        } else {
            Point destination = getTargetAsPoint();
            block.setPos(destination.x, destination.y);
        }
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        if (!active) {
            return tile.onCollision(initiator, map, handler);
        }

        if (collisionDelay > 0) {
            --collisionDelay;
            return true;
        }

        if (initiator instanceof IsMoveableBlock block) {
            if (targetIsOtherPortal) {
                other.setCollisionDelay(DEFAULT_COLLISION_DELAY);
                block.setPos( other.getX(), other.getY() );
            } else {
                block.setPos(target.x, target.y);
            }
        }
        return tile.onCollision(initiator, map, handler);
    }

    @Override
    public IsBlockType getBlockType() {
        return tile.getBlockType();
    }

    @Override
    public int getX() {
        return tile.getX();
    }

    @Override
    public int getY() {
        return tile.getY();
    }

    @Override
    public int getWidth() {
        return tile.getWidth();
    }

    @Override
    public int getHeight() {
        return tile.getHeight();
    }

    @Override
    public BufferedImage getImage() {
        return tile.getImage();
    }

}
