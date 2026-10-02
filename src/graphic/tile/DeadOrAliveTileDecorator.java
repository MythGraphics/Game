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

import game.resource.Resource;
import graphic.*;
import static graphic.map.DefaultBlockType.CORPSE;
import graphic.map.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("CloneableImplementsClone")
public class DeadOrAliveTileDecorator<T extends BlockTile> implements IsDeadOrAliveTile, IsBlockTile, IsMoveableTile,
                                                                      AutoMoveable, CanFireMissile {

    private final T tile;
    private IsBlockType aliveType, deadType;
    private DeadOrAliveImageSet imageSet;
    private Resource health;
    private boolean alive = true;
    private boolean drawOverlay = true;

    public DeadOrAliveTileDecorator(T aliveTile) {
        this(aliveTile, CORPSE, null, null);
    }

    public DeadOrAliveTileDecorator(T aliveTile, IsBlockType deadType, DeadOrAliveImageSet imageSet) {
        this(aliveTile, deadType, imageSet, null);
    }

    public DeadOrAliveTileDecorator(T aliveTile, IsBlockType deadType, DeadOrAliveImageSet imageSet,
                                    Resource overlayResource) {
        this.tile       = aliveTile;
        this.aliveType  = aliveTile.getBlockType();
        this.deadType   = deadType;
        this.health     = overlayResource;
        if (imageSet == null) {
            this.imageSet = new DeadOrAliveImageSet(tile);
        } else {
            this.imageSet = imageSet;
        }
    }

    public final void setDeadTile(IsBlockType bType, HasImage image) {
        this.deadType = bType;
        this.imageSet.setDeadImage(image);
    }

    public T getTile() {
        return tile;
    }

    public DeadOrAliveImageSet getImageSet() {
        return imageSet;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setDead() {
        alive = false;
        imageSet.setDead();
        tile.change(deadType, imageSet);
    }

    @Override
    public void setAlive() {
        alive = true;
        imageSet.setAlive();
        tile.change(aliveType, imageSet);
    }

    @Override
    public void stop() {
        if (tile instanceof AutoMoveable auto) {
            auto.stop();
        }
    }

    @Override
    public void start() {
        if (tile instanceof AutoMoveable auto) {
            auto.start();
        }
    }

    @Override
    public void move() {
        if (tile instanceof AutoMoveable auto) {
            auto.move();
        }
    }

    @Override
    public void move(Direction direction) {
        if (tile instanceof Moveable m) {
            m.move(direction);
        }
    }

    @Override
    public int getStepSize() {
        return (tile instanceof Moveable m) ? m.getStepSize() : 0;
    }

    @Override
    public int getMaxX() {
        return (tile instanceof Moveable m) ? m.getMaxX() : 0;
    }

    @Override
    public int getMaxY() {
        return (tile instanceof Moveable m) ? m.getMaxY() : 0;
    }

    @Override
    public void setX(int x) {
        if (tile instanceof Moveable m) {
            m.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        if (tile instanceof Moveable m) {
            m.setY(y);
        }
    }

    @Override
    public Direction getCurrentDirection() {
        return (tile instanceof Moveable m) ? m.getCurrentDirection() : null;
    }

    @Override
    public void moveRandom() {
        if (tile instanceof AutoMoveable auto) {
            auto.moveRandom();
        }
    }

    @Override
    public void tilt(Direction direction) {
        if (tile instanceof Maneuverable m) {
            m.tilt(direction);
        }
    }

    @Override
    public void step(Direction direction){
        if (tile instanceof Maneuverable m) {
            m.step(direction);
        }
    }

    @Override
    public Resource getOverlayResource() {
        return health;
    }

    public void setOverlayResource(Resource overlayResource) {
        health = overlayResource;
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
    public IsBlockType getBlockType() {
        return tile.getBlockType();
    }

    @Override
    public BufferedImage getImage() {
        return tile.getImage();
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        tile.draw(g2d, offsetX, offsetY);
    }

    public void drawOverlay(boolean b) {
        this.drawOverlay = b;
    }

    @Override
    public void drawOverlay(Graphics2D g2d, int offsetX, int offsetY) {
        if (drawOverlay) {
            OverlayUtil.drawResourceBar(g2d, offsetX, offsetY, this, tile);
        }
    }

    @Override
    public boolean onCollision(GameMap map, Block initiator, IsCollisionHandler handler) {
        boolean b = tile.onCollision(map, initiator, handler);
        if (health != null && health.getValue() <= 0) {
            setDead();
        }
        return b;
    }

    @Override
    public DirectionalImage getMissileImage() {
        return tile.getMissileImage();
    }

    @Override
    public void fireMissile(GameMap map, Direction d) {
        tile.fireMissile(map, d);
    }

    @Override
    public boolean hasMissile() {
        return tile.hasMissile();
    }

}
