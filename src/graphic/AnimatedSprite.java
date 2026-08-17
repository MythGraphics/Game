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

import graphic.map.IsMapTile;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class AnimatedSprite extends DeadOrAliveSprite {

    private Animation ani;

    public AnimatedSprite(Animation ani, BufferedImage deadImage, int x, int y, int blockSize, IsMapTile tile) {
        this(ani, deadImage, x, y, blockSize, blockSize, tile);
    }

    public AnimatedSprite(Animation ani, BufferedImage deadImage, Point pos, Dimension dim, IsMapTile tile) {
        this(ani, deadImage, pos.x, pos.y, dim.width, dim.height, tile);
    }

    public AnimatedSprite(Animation ani, BufferedImage deadImage, int x, int y, int width, int height, IsMapTile tile) {
        super( ani.getImage(), deadImage, x, y, width, height, tile );
        this.ani = ani;
    }

    public void setAnimation(Animation ani) {
        this.ani = ani;
    }

    @Override
    public BufferedImage getAliveImage() {
        if (ani == null) {
            return null;
        }
        return ani.getImage();
    }

}
