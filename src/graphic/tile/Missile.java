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

import graphic.Direction;
import graphic.DirectionalImage;
import graphic.map.*;
import graphic.missileLauncher.MissileLauncher;
import java.awt.Point;

@SuppressWarnings("CloneableImplementsClone")
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

    public Missile(Direction initialDirection,
                   int x, int y, IsBlockType bType,
                   int stepSize, MissileLauncher source) {
        this( initialDirection, x, y, bType, stepSize, source.getMissileImage() );
    }

    public Missile(Direction initialDirection,
                   int x, int y, IsBlockType bType,
                   int stepSize, DirectionalImage imgset) {
        super(initialDirection, x, y, bType, stepSize, MAX, imgset);
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
