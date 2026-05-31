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

import graphic.DeadOrAlive;
import static graphic.map.BlockType.CORPSE;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

public abstract class DeadOrAliveBlock extends Block implements DeadOrAlive {

    public DeadOrAliveBlock(Point pos, Dimension dim, BlockType type) {
        this(pos.x, pos.y, dim.width, dim.height, type);
    }

    public DeadOrAliveBlock(int x, int y, int blockSize, BlockType type) {
        this(x, y, blockSize, blockSize, type);
    }

    public DeadOrAliveBlock(int x, int y, int width, int height, BlockType type) {
        super(x, y, width, height, type);
    }

    public abstract Image getAliveImage();
    public abstract Image getDeadImage();
    public abstract void setDeadImage(Image deadImage);
//  public abstract void setAliveImage(Image aliveImage);

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
        type.setPassable(true);
        type = CORPSE;
    }

    @Override
    public void alive() {
        dead = false;
        type.setPassable(false);
    }

}
