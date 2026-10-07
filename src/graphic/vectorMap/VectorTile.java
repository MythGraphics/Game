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
import java.awt.image.BufferedImage;

public class VectorTile implements HasImage {

    private final BufferedImage[] imageSet;

    public VectorTile(BufferedImage[] imageSet) {
        this.imageSet = imageSet;
    }

    public float getAnglePerTile() {
        return 1.0f / imageSet.length;
    }

    public BufferedImage getImage(float angle) {
        angle = VectorBlock.normalize(angle); // Winkel normalisieren
        int index = (int) (angle * imageSet.length);
        return imageSet[index];
    }

    @Override
    public BufferedImage getImage() {
        // ToDo implementieren
        return null;
    }

}
