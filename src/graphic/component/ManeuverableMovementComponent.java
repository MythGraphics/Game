/*
 *
 */

package graphic.component;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import graphic.DirectionalImage;
import graphic.HasImage;
import graphic.block.Block;
import java.awt.image.BufferedImage;

// Player
public class ManeuverableMovementComponent extends DirectionalMovementComponent implements HasImage {

    private final DirectionalImage imgset;

    public ManeuverableMovementComponent(Direction initialDirection, DirectionalImage imgset, int stepSize) {
        super(initialDirection, stepSize);
        this.imgset = imgset;
    }

    @Override
    public BufferedImage getImage() {
        return imgset.getImage();
    }

    @Override
    public void setDirection(Direction direction) {
        super.setDirection(direction);
        imgset.setDirection(direction);
    }

    /**
     * Dreht und bewegt die Fliese in die gegebene Richtung.
     * @param owner     Besitzer
     * @param direction Richtung
     */
    public void move(Block owner, Direction direction) {
        tilt(direction);
        step(owner, direction);
    }

    /**
     * Bewegt die Fliese in die aktuelle Blickrichtung.
     * @param owner Besitzer
     */
    @Override
    public void move(Block owner) {
        step( owner, getDirection() );
    }

    /**
     * Dreht die Fliese in die gegebene Richtung.
     * @param direction
     */
    public void tilt(Direction direction) {
        if (direction == null) {
            return;
        }

        setDirection(direction);
    }

    /**
     * Bewegt die Fliese in die gegebene Richtung ohne sie zu drehen.
     * @param owner     Besitzer
     * @param direction Richtung
     */
    public void step(Block owner, Direction direction) {
        if (direction == null) {
            return;
        }

        setDirection(direction);
        super.move(owner);
    }

}
