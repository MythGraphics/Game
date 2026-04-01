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

import graphic.map.Block;
import graphic.map.BlockType;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

public class AnimatedBlock extends Block {

    private Animation ani;
    private Image deadImage;

    public AnimatedBlock(Animation ani, int x, int y, int blockSize, BlockType type) {
        super(x, y, blockSize, type);
        this.ani = ani;
    }

    public AnimatedBlock(Animation ani, Point pos, Dimension dim, BlockType type) {
        super(pos, dim, type);
        this.ani = ani;
    }

    public void setAnimation(Animation ani) {
        this.ani = ani;
    }

    public void setDeadImage(Image image) {
        this.deadImage = image;
    }

    @Override
    public Image getDeadImage() {
        return deadImage;
    }

    @Override
    public Image getAliveImage() {
        if (ani == null) {
            return null;
        }
        return ani.getImage();
    }

}
