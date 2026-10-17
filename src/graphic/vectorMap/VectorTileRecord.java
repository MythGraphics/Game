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

public record VectorTileRecord(BufferedImage getImage, IsBlockType blockType, boolean isSymmetric) implements HasImage {}
