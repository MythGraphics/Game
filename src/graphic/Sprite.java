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
import graphic.map.IsMapTile;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Sprite extends Block {

    private BufferedImage image;

    public Sprite(BufferedImage image, Point pos, Dimension dim, IsMapTile tile) {
        this(image, pos.x, pos.y, dim.width, dim.height, tile);
    }

    public Sprite(BufferedImage image, int x, int y, int blockSize, IsMapTile tile) {
        this(image, x, y, blockSize, blockSize, tile);
    }

    public Sprite(BufferedImage image, int x, int y, int width, int height, IsMapTile tile) {
        super(x, y, width, height, tile);
        this.image = image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

}
