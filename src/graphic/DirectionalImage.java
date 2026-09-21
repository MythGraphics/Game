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

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DirectionalImage implements HasImage, HasDirectionalImage, Cloneable {

    private final HasImage[] imageset;
    private Direction d;

    public DirectionalImage(HasImage[] imageset) {
        this(Direction.values()[0], imageset);
    }

    public DirectionalImage(Direction initialDirection, HasImage[] imageset) {
        if (imageset == null || imageset.length < 4) {
            throw new IllegalArgumentException("imageset is null or from insufficient length.");
        }
        this.d = initialDirection;
        this.imageset = imageset;
    }

    // Copy-Constructor (shallow)
    public DirectionalImage(DirectionalImage other) {
        this.d = other.d;
        this.imageset = other.imageset;
    }

    // Copy-Factory (shallow)
    public static DirectionalImage newInstance(DirectionalImage other) {
        return new DirectionalImage(other);
    }

    /**
     * Erstellt ein DirectionalImage, das für ALLE Richtungen die selbe Animation nutzt.
     * @param data
     * @return
     */
    public static DirectionalImage createSingleAnimation(AnimationData data) {
        Direction[] directionSet     = Direction.values();
        AnimationPlayer[] playerSet  = new AnimationPlayer[directionSet.length];
        AnimationPlayer sharedPlayer = new AnimationPlayer(data);
        for (int i = 0; i < directionSet.length; i++) {
            playerSet[i] = sharedPlayer;
        }
        return new DirectionalImage(playerSet);
    }

    /**
     * Erstellt ein DirectionalImage, das für ALLE Richtungen das selbe Bild nutzt.
     * @param image
     * @return
     */
    public static DirectionalImage createSingleTile(HasImage image) {
        HasImage[] set = new HasImage[Direction.values().length];
        Arrays.fill(set, image);
        return new DirectionalImage(set);
    }

    /**
     * Erstellt ein DirectionalImage, das für ALLE Richtungen das selbe Bild nutzt.
     * @param image
     * @return
     */
    public static DirectionalImage createSingleTile(BufferedImage image) {
        return createSingleTile( () -> image );
    }

    public static HasImage[] create(BufferedImage[] imageset) {
        if (imageset == null || imageset.length < 4) {
            throw new IllegalArgumentException("imageset is null or from insufficient length.");
        }
/*
        HasImage[] array = Arrays.stream(imageset)
                                 .map(img -> (HasImage) () -> img)
                                 .toArray(HasImage[]::new);
 */
        HasImage[] array = new HasImage[imageset.length];
        for (int i = 0; i < array.length; ++i) {
            BufferedImage img = imageset[i];
            array[i] = () -> img;
        }
        return array;
    }

    public void setDirection(Direction d) {
        this.d = d;
    }

    public Direction getDirection() {
        return d;
    }

    @Override
    public BufferedImage getImage() {
        return imageset[d.ordinal()].getImage();
    }

    @Override
    public BufferedImage getImage(Direction d) {
        return imageset[d.ordinal()].getImage();
    }

    @Override
    public DirectionalImage clone() {
        return new DirectionalImage(this);
    }

}
