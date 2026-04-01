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

import static graphic.Direction.*;
import graphic.map.BlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AutoMoveableSprite extends MoveableSprite implements ActionListener, AutoMoveable {

    final Point start;

    Direction direction;

    private boolean auto = false;
    private int ticksPerStep = 1;
    private int tickCounter = 0;

    public AutoMoveableSprite(Animation[] aniset, int x, int y, int blockSize, BlockType type, Point maxPoint) {
        super(aniset, x, y, blockSize, type, maxPoint);
        start = new Point(x, y);
    }

    public AutoMoveableSprite(Animation[] aniset, Point pos, Dimension dim, int blockSize, BlockType type, Point maxPoint) {
        super(aniset, pos, dim, blockSize, type, maxPoint);
        start = new Point(x, y);
    }

    @Override
    public void reset() {
        super.x = start.x;
        super.y = start.y;
        tickCounter = 0;
    }

    @Override
    public void moveRandom() {
        int randi = new Random().nextInt(4);
        move( Direction.values()[randi] );
    }

    public void start() {
        auto = true;
    }

    public void stop() {
        auto = false;
    }

    public void setTicksPerStep(int ticks) {
        this.ticksPerStep = ticks;
    }

    @Override
    public void move(Direction direction) {
        // Update der aktuellen Animation in der Superklasse
        setAnimation( aniset[direction.ordinal()] );
        // Bewegungslogik; Umkehren, wenn Grenzen erreicht
        switch (direction) {
            case UP    -> { if (y > start.y)    { y -= blockSize; } else { this.direction = Direction.invert(UP);    }}
            case DOWN  -> { if (y < maxPoint.y) { y += blockSize; } else { this.direction = Direction.invert(DOWN);  }}
            case LEFT  -> { if (x > start.x)    { x -= blockSize; } else { this.direction = Direction.invert(LEFT);  }}
            case RIGHT -> { if (x < maxPoint.x) { x += blockSize; } else { this.direction = Direction.invert(RIGHT); }}
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (auto) {
            // Logik für die Verzögerung
            tickCounter++;
            if (tickCounter >= ticksPerStep) {
                move(direction);
                tickCounter = 0; // Zähler zurücksetzen
            }
        }
    }

}
