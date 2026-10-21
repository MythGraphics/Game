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

    private final IsBlockTile tile;

    private Point target = new Point(0,0);
    private Portal other;
    private boolean targetIsOtherPortal;
    private boolean active = true;

    public Portal(IsBlockTile tile, Point target) {
        this.tile           = tile;
        this.target         = target;
        targetIsOtherPortal = false;
    }

    public Portal(IsBlockTile tile, Portal other) {
        this.tile  = tile;
        this.other = other;
        setTarget(other);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public final void setTarget(Portal other) {
        this.other          = other;
        targetIsOtherPortal = true;
        active              = true;
        other.active        = true;
    }

    public void setTarget(Point p) {
        target.x = p.x;
        target.y = p.y;
        targetIsOtherPortal = false;
    }

    public void setTarget(int x, int y) {
        target.x = x;
        target.y = y;
        targetIsOtherPortal = false;
    }

    public Point getTargetAsPoint() {
        return targetIsOtherPortal ? new Point( other.getX(), other.getY() ) : target;
    }

    public Portal getTargetAsPortal() {
        return targetIsOtherPortal ? other : new Portal(tile, target);
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        tile.draw(g2d, offsetX, offsetY);
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        if (initiator instanceof IsMoveableBlock m) {
            if (targetIsOtherPortal) {
                if (active) {
                    other.setActive(false);
                    m.setPos( other.getX(), other.getY() );
                } else {
                    return true;
                }
            } else {
                m.setPos(target.x, target.y);
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
