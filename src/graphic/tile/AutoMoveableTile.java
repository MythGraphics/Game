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
public class AutoMoveableTile extends ManeuverableTile implements AutoMoveable {

    public final Point start;
    public final Random rand = new Random();
    public final Direction initialDirection;

    private boolean autoMove = true;
    private int ticksPerStep = 1;
    private int tickCounter  = 0;

    public AutoMoveableTile(Direction initialDirection,
                            int x, int y, IsBlockType bType,
                            int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(x, y, bType, stepSize, maxPoint, imgset);
        this.initialDirection = initialDirection;
        this.start = new Point(x, y);
        setDirection(initialDirection);
    }

    public AutoMoveableTile(Direction initialDirection,
                            int x, int y, int width, int height, IsBlockType bType,
                            int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(x, y, width, height, bType, stepSize, maxPoint, imgset);
        this.initialDirection = initialDirection;
        this.start = new Point(x, y);
        setDirection(initialDirection);
    }

    public AutoMoveableTile(Direction initialDirection,
                            Point pos, Dimension dim, IsBlockType bType,
                            int stepSize, Point maxPoint, DirectionalImage imgset) {
        this(initialDirection, pos.x, pos.y, dim.width, dim.height, bType, stepSize, maxPoint, imgset);
    }

    public void reset() {
        super.x = start.x;
        super.y = start.y;
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

    public final void setDirection(Direction direction) {
        imgset.setDirection(direction);
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
