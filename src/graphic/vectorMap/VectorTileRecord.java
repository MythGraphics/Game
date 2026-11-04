/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import graphic.block.IsBlockType;
import java.awt.image.BufferedImage;

public record VectorTileRecord(IsBlockType blockType, HasImage image, boolean isSymmetric) implements HasImage {

    @Override
    public BufferedImage getImage() {
        return image.getImage();
    }

}
