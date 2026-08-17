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

import graphic.map.IsChangeableMapTile;
import graphic.map.IsMapTile;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

public abstract class DeadOrAliveSprite extends Sprite implements DeadOrAlive {

    private boolean dead = false;
    private Image deadImage;

    public DeadOrAliveSprite(Image aliveImage, Image deadImage, Point pos, Dimension dim, IsMapTile tile) {
        this(aliveImage, deadImage, pos.x, pos.y, dim.width, dim.height, tile);
    }

    public DeadOrAliveSprite(Image aliveImage, Image deadImage, int x, int y, int blockSize, IsMapTile tile) {
        this(aliveImage, deadImage, x, y, blockSize, blockSize, tile);
    }

    public DeadOrAliveSprite(Image aliveImage, Image deadImage, int x, int y, int width, int height, IsMapTile tile) {
        super(aliveImage, x, y, width, height, tile);
        this.deadImage = deadImage;
    }

    public void setAliveImage(Image aliveImage) {
        super.setImage(aliveImage);
    }

    public void setDeadImage(Image deadImage) {
        this.deadImage = deadImage;
    }

    public Image getAliveImage() {
        return super.getImage();
    }

    public Image getDeadImage() {
        return deadImage;
    }

    @Override
    public Image getImage() {
        if (dead) {
            return getDeadImage();
        } else {
            return getAliveImage();
        }
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void swap() {
        if (dead) {
            alive();
        } else {
            dead();
        }
    }

    @Override
    public void dead() {
        dead = true;
        if (tile instanceof IsChangeableMapTile cTile) {
            cTile.setPassable(true);
            cTile = cTile.getDeadTile(cTile);
        }
    }

    @Override
    public void alive() {
        dead = false;
        if (tile instanceof IsChangeableMapTile cTile) {
            cTile.setPassable(false);
            cTile = cTile.getAliveTile(cTile);
        }
    }

}
