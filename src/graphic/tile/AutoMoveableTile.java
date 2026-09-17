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
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AutoMoveableTile extends MoveableTile implements ActionListener, AutoMoveable {

    final Point start;
    final Random rand = new Random();
    final Direction initialDirection;

    private boolean auto = true;
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

    @Override
    public void moveRandom() {
        int randi = rand.nextInt(4);
        move( Direction.values()[randi] );
    }

    @Override
    public void start() {
        auto = true;
    }

    @Override
    public void stop() {
        auto = false;
    }

    public void setTicksPerStep(int ticks) {
        this.ticksPerStep = Math.max(1, ticks);
    }

    public final void setDirection(Direction direction) {
        imgset.setDirection(direction);
    }

    @Override
    public void move() {
        move( getCurrentDirection() );
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (auto) {
            // Logik für die Verzögerung
            tickCounter++;
            if (tickCounter >= ticksPerStep) {
                move();
                tickCounter = 0;
            }
        }
    }

}
