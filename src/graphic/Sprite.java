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

import graphic.map.BlockType;
import graphic.map.DeadOrAliveBlock;
import java.awt.Image;

public class Sprite extends DeadOrAliveBlock {

    public Image aliveImage;
    public Image deadImage;

    public Sprite(Image aliveImage, int x, int y, int blockSize, BlockType type) {
        super(x, y, blockSize, type);
        this.aliveImage = aliveImage;
    }

    @Override
    public Image getAliveImage() {
        return aliveImage;
    }

    @Override
    public Image getDeadImage() {
        return deadImage;
    }

    public void setAliveImage(Image aliveImage) {
        this.aliveImage = aliveImage;
    }

    @Override
    public void setDeadImage(Image deadImage) {
        this.deadImage = deadImage;
    }

}
