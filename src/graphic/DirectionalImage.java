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

public class DirectionalImage implements HasImage, HasDirectionalImage, Cloneable {

    private final HasImage[] imageset;
    private Direction d;

    public DirectionalImage(HasImage[] imageset) {
        this(Direction.values()[0], imageset);
    }

    public DirectionalImage(Direction initialDirection, HasImage[] imageset) {
        if (imageset == null || imageset.length < 4) {
            throw new IllegalArgumentException("imgset is null or from insufficient length.");
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
