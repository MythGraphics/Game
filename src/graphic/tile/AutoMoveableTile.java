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

import graphic.AutoMoveable;
import graphic.Direction;
import graphic.DirectionalImage;
import graphic.block.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

@SuppressWarnings("CloneableImplementsClone")
public class AutoMoveableTile extends ShuttleTile implements AutoMoveable {

    public final Random rand = new Random();

    private boolean autoMove = true;
    private int ticksPerStep = 1;
    private int tickCounter  = 0;

    public AutoMoveableTile(Direction initialDirection,
                            int x, int y, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super(initialDirection, x, y, bType, stepSize, max, imgset);
    }

    public AutoMoveableTile(Direction initialDirection,
                            int x, int y, int width, int height, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super(initialDirection, x, y, width, height, bType, stepSize, max, imgset);
    }

    public AutoMoveableTile(Direction initialDirection,
                            Point pos, Dimension dim, IsBlockType bType,
                            int stepSize, Point max, DirectionalImage imgset) {
        super(initialDirection, pos, dim, bType, stepSize, max, imgset);
    }

    public void reset() {
        super.x = min.x;
        super.y = min.y;
        tickCounter = 0;
        imgset.setDirection(initialDirection);
    }

    public void moveRandom() {
        int randi = rand.nextInt(4);
        move( Direction.values()[randi] );
    }

    @Override
    public void startMovement() {
        autoMove = true;
    }

    @Override
    public void stopMovement() {
        autoMove = false;
    }

    public void setTicksPerStep(int ticks) {
        this.ticksPerStep = Math.max(1, ticks);
    }

    @Override
    public void move() {
        if (!autoMove) {
            return;
        }
        // Verzögerungslogik
        tickCounter++;
        if (tickCounter >= ticksPerStep) {
            if ( getX() > getMaxX() || getY() > getMaxY() || getX() < 0 || getY() < 0 ) {
                move( Direction.invert( getViewDirection() ));
            } else {
                move( getViewDirection() );
            }
            tickCounter = 0;
        }
    }

}
