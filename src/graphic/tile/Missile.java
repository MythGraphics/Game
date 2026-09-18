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

import graphic.CanFireMissile;
import graphic.Direction;
import graphic.DirectionalImage;
import graphic.map.*;
import java.awt.Dimension;
import java.awt.Point;

public class Missile extends AutoMoveableTile implements IsCollider {

/*
    Methode(int startX, int startY, int targetX, int targetY, int speed) {
        int vx = 0, vy = 0;
        int x = startX;
        int y = startY;

        // Richtungsvektor berechnen und normalisieren
        int dx = targetX - startX;
        int dy = targetY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            vx = (int) (dx / distance) * speed;
            vy = (int) (dy / distance) * speed;
        }
    }
 */

    public final static Point MAX = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public final CanFireMissile source;

    public Missile(CanFireMissile source, Direction initialDirection,
                   int x, int y, IsBlockType bType,
                   int stepSize) {
        this( source, initialDirection, x, y, bType, stepSize, source.getMissileImage() );
    }

    public Missile(CanFireMissile source, Direction initialDirection,
                   int x, int y, IsBlockType bType,
                   int stepSize, DirectionalImage imgset) {
        super(initialDirection, x, y, bType, stepSize, MAX, imgset);
        this.source = source;
    }

    public Missile(CanFireMissile source, Direction initialDirection,
                   Point pos, Dimension dim, IsBlockType bType,
                   int stepSize) {
        this( source, initialDirection, pos, dim, bType, stepSize, source.getMissileImage() );
    }

    public Missile(CanFireMissile source, Direction initialDirection,
                   Point pos, Dimension dim, IsBlockType bType,
                   int stepSize, DirectionalImage imgset) {
        super(initialDirection, pos, dim, bType, stepSize, MAX, imgset);
        this.source = source;
    }

    public CanFireMissile getSource() {
        return source;
    }

    @Override
    public boolean onCollision(GameMap map, Block initiator, IsCollisionHandler handler) {
        map.remove(this);
        return super.onCollision(map, initiator, handler);
    }

    @Override
    public void collides(GameMap map, Block target, IsCollisionHandler handler) {
        map.remove(this);
    }

}
