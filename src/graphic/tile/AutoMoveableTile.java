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
import graphic.HasImage;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AutoMoveableTile extends MoveableTile implements ActionListener, AutoMoveable {

    final Point start;

    private final Random rand = new Random();

    private Direction direction;
    private boolean auto = false;
    private int ticksPerStep = 1;
    private int tickCounter  = 0;

    public AutoMoveableTile(Direction initialDirection,
                            int x, int y, int blockSize, IsBlockType bType,
                            Point maxPoint, HasImage[] imgset) {
        super(x, y, blockSize, bType, maxPoint, imgset);
        this.direction = initialDirection;
        this.start = new Point(x, y);
    }

    public AutoMoveableTile(Direction initialDirection,
                            Point pos, Dimension dim, int blockSize, IsBlockType bType,
                            Point maxPoint, HasImage[] imgset) {
        super(pos, dim, blockSize, bType, maxPoint, imgset);
        this.direction = initialDirection;
        this.start = new Point(pos.x, pos.y);
    }

    @Override
    public void reset() {
        super.x = start.x;
        super.y = start.y;
        tickCounter = 0;
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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void move(Direction direction) {
        if (direction == null) {
            return;
        }

        this.direction = direction;
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
