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
import java.awt.Image;

public class Sprite extends Block {

    public final Image aliveImage;
    public Image deadImage;

    public Sprite(Image image, int x, int y, int blockSize, BlockType type) {
        super(x, y, blockSize, type);
        this.aliveImage = image;
    }

    public void setDeadImage(Image deadImage) {
        this.deadImage = deadImage;
    }

    @Override
    public Image getAliveImage() {
        return aliveImage;
    }

    @Override
    public Image getDeadImage() {
        return deadImage;
    }

}
