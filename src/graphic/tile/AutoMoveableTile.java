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
import static graphic.Direction.*;
import graphic.DirectionalImage;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AutoMoveableTile extends MoveableTile implements ActionListener, AutoMoveable {

    final Point start;

    private final Random rand = new Random();
    private final Direction initialDirection;

    private Direction direction;
    private boolean auto = false;
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

    @Override
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

    public void start() {
        auto = true;
    }

    public void stop() {
        auto = false;
    }

    public void setTicksPerStep(int ticks) {
        this.ticksPerStep = Math.max(1, ticks);
    }

    public Direction getDirection() {
        return direction;
    }

    public final void setDirection(Direction direction) {
        this.direction = direction;
        imgset.setDirection(initialDirection);
    }

    @Override
    public void move(Direction direction) {
        if (direction == null) {
            return;
        }

        setDirection(direction);
        super.move(direction);

        // Bewegungslogik: Umkehren, wenn Grenzen erreicht
        switch (direction) {
            case UP    -> { if (y <= start.y)    { this.direction = Direction.invert(Direction.UP); }}
            case DOWN  -> { if (y >= maxPoint.y) { this.direction = Direction.invert(Direction.DOWN); }}
            case LEFT  -> { if (x <= start.x)    { this.direction = Direction.invert(Direction.LEFT); }}
            case RIGHT -> { if (x >= maxPoint.x) { this.direction = Direction.invert(Direction.RIGHT); }}
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (auto) {
            // Logik für die Verzögerung
            tickCounter++;
            if (tickCounter >= ticksPerStep) {
                move(direction);
                tickCounter = 0;
            }
        }
    }

}
