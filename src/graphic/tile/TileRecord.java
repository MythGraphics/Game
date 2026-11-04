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

import graphic.HasImage;
import graphic.block.IsBlockType;
import java.awt.image.BufferedImage;

public record TileRecord(int x, int y, int width, int height, IsBlockType bType, HasImage image) implements HasImage {

    @Override
    public BufferedImage getImage() {
        return image.getImage();
    }

}
