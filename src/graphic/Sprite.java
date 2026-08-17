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
import java.awt.Image;
import java.awt.Point;

public class Sprite extends Block {

    private Image image;

    public Sprite(Image image, Point pos, Dimension dim, IsMapTile tile) {
        this(image, pos.x, pos.y, dim.width, dim.height, tile);
    }

    public Sprite(Image image, int x, int y, int blockSize, IsMapTile tile) {
        this(image, x, y, blockSize, blockSize, tile);
    }

    public Sprite(Image image, int x, int y, int width, int height, IsMapTile tile) {
        super(x, y, width, height, tile);
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public Image getImage() {
        return image;
    }

}
