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

import graphic.map.CustomMapTile;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

public class CustomSprite extends Sprite {

    public CustomSprite(Point pos, Dimension dim, CustomMapTile tile) {
        this(pos.x, pos.y, dim.width, dim.height, tile);
    }

    public CustomSprite(int x, int y, int blockSize, CustomMapTile tile) {
        this(x, y, blockSize, blockSize, tile);
    }

    public CustomSprite(int x, int y, int width, int height, CustomMapTile tile) {
        super( tile.getImage(), x, y, width, height, tile );
    }

    @Override
    public Image getImage() {
        return ((HasImage) tile).getImage();
    }

}
