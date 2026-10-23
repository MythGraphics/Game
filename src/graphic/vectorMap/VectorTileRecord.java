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

public record VectorTileRecord(IsBlockType blockType, BufferedImage getImage, boolean isSymmetric) implements HasImage {

    public static VectorTileRecord build(IsBlockType bType, HasImage image) {
        return new VectorTileRecord(bType, image.getImage(), true);
    }

}
