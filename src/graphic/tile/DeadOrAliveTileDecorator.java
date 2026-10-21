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
import graphic.block.Collidable;
import graphic.block.IsBlockType;
import graphic.block.IsMoveableBlock;
import graphic.map.GameMap;
import graphic.map.HasMaxPoint;
import graphic.map.IsCollisionHandler;
import graphic.map.OverlayUtil;
import graphic.missileLauncher.DefaultMissileLauncher;
import graphic.missileLauncher.HasMissileLauncher;
import graphic.missileLauncher.MissileLauncher;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("CloneableImplementsClone")
public class DeadOrAliveTileDecorator<T extends BlockTile> implements IsDeadOrAliveTile, IsMoveableBlock, AutoMoveable,
                                                                      Maneuverable, HasDirection, HasMaxPoint,
                                                                      HasMissileLauncher {

    private final T tile;

    private DeadOrAliveDataSet doaSet;
    private Resource overlayResource;
    private boolean drawOverlay = false;
    private boolean autoMoveAsCorpse = false;
    private MissileLauncher missileLauncher = new DefaultMissileLauncher(this, null);

    public DeadOrAliveTileDecorator(T aliveTile) {
        this(aliveTile, null);
    }

    public DeadOrAliveTileDecorator(T aliveTile, DeadOrAliveDataSet dataSet) {
        this(aliveTile, dataSet, null);
    }

    public DeadOrAliveTileDecorator(T aliveTile, DeadOrAliveDataSet dataSet, Resource overlayResource) {
        this.tile = aliveTile;
        setOverlayResource(overlayResource);
        if (dataSet == null) {
            this.doaSet = new DeadOrAliveDataSet(aliveTile.getBlockType(), aliveTile);
        } else {
            this.doaSet = dataSet;
        }
    }

    public void setMissileLauncher(MissileLauncher missileLauncher) {
        this.missileLauncher = missileLauncher;
    }

    @Override
    public MissileLauncher getMissileLauncher() {
        return missileLauncher;
    }

    public final void setDeadTile(IsBlockType bType, HasImage image) {
        this.doaSet.setDeadTile(bType, image);
    }

    public T getTile() {
        return tile;
    }

    public DeadOrAliveDataSet getDataSet() {
        return doaSet;
    }

    @Override
    public boolean isAlive() {
        return doaSet.isAlive();
    }

    @Override
    public void setDead() {
        doaSet.setDead();
        tile.change(doaSet.getBlockType(), doaSet);
    }

    @Override
    public void setAlive() {
        doaSet.setAlive();
        tile.change(doaSet.getBlockType(), doaSet);
    }

    @Override
    public void stopMovement() {
        if (tile instanceof AutoMoveable auto) {
            auto.stopMovement();
        }
    }

    @Override
    public void startMovement() {
        if (tile instanceof AutoMoveable auto) {
            auto.startMovement();
        }
    }

    public boolean getAutoMoveAsCorpse() {
        return autoMoveAsCorpse;
    }

    public void setAutoMoveAsCorpse(boolean autoMoveAsCorpse) {
        this.autoMoveAsCorpse = autoMoveAsCorpse;
    }

    @Override
    public void move() {
        if ( !isAlive() && !autoMoveAsCorpse ) {
            return;
        }
        if (tile instanceof Moveable m) {
            m.move();
        }
    }

    @Override
    public void move(Direction direction) {
        if ( !isAlive() ) {
            return;
        }
        if (tile instanceof Maneuverable m) {
            m.move(direction);
        }
    }

    @Override
    public int getMaxX() {
        return (tile instanceof HasMaxPoint m) ? m.getMaxX() : 0;
    }

    @Override
    public int getMaxY() {
        return (tile instanceof HasMaxPoint m) ? m.getMaxY() : 0;
    }

    @Override
    public void setX(int x) {
        if (tile instanceof IsMoveableBlock m) {
            m.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        if (tile instanceof IsMoveableBlock m) {
            m.setY(y);
        }
    }

    @Override
    public void setPos(int x, int y) {
        if (tile instanceof IsMoveableBlock m) {
            m.setPos(x, y);
        }
    }

    @Override
    public Direction getViewDirection() {
        return (tile instanceof HasDirection d) ? d.getViewDirection() : null;
    }

    public void moveRandom() {
        if (tile instanceof AutoMoveableTile auto) {
            auto.moveRandom();
        }
    }

    @Override
    public void tilt(Direction direction) {
        if ( !isAlive() ) {
            return;
        }
        if (tile instanceof Maneuverable m) {
            m.tilt(direction);
        }
    }

    @Override
    public void step(Direction direction) {
        if ( !isAlive() ) {
            return;
        }
        if (tile instanceof Maneuverable m) {
            m.step(direction);
        }
    }

    @Override
    public Resource getOverlayResource() {
        return overlayResource;
    }

    public final void setOverlayResource(Resource overlayResource) {
        this.overlayResource = overlayResource;
        drawOverlay = overlayResource != null;
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
        drawOverlay(g2d, offsetX, offsetY);
    }

    public void drawOverlay(boolean drawOverlay) {
        this.drawOverlay = drawOverlay;
    }

    @Override
    public void drawOverlay(Graphics2D g2d, int offsetX, int offsetY) {
        if (drawOverlay) {
            OverlayUtil.drawResourceBar(g2d, offsetX, offsetY, this, tile);
        }
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        /* CAVE: tile.onCollision(initiator, map, handler) kann nicht verwendet werden, da THIS in tile nur die Referenz
         * auf sich selbt übergibt, benötigen aber die Referenz auf diesen Decorator.
         */
        handler.fireEvent(map, initiator, this);
        if (drawOverlay && overlayResource.getValue() <= 0) {
            setDead();
        }
        return getBlockType().isPassable();
    }

    @Override
    public String toString() {
        return tile.toString();
    }

}
