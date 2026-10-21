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
import graphic.block.Collidable;
import graphic.block.IsBlockType;
import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import graphic.missileLauncher.IsMissile;
import graphic.missileLauncher.MissileLauncher;
import java.awt.Point;

@SuppressWarnings("CloneableImplementsClone")
public class Missile extends AutoMoveableTile implements IsMissile {

    public final MissileLauncher source;

    public Missile(Direction initialDirection,
                   int x, int y, IsBlockType bType,
                   int stepSize, Point maxPoint, MissileLauncher source) {
        super( initialDirection, x, y, bType, stepSize, maxPoint, new DirectionalImage( source.getMissileImage() ));
        this.source = source;
    }

    @Override
    public MissileLauncher getSource() {
        return source;
    }

    @Override
    public void collidesWith(Collidable target, GameMap map) {
        map.remove(this);
        super.onCollision(target, map, map); // CAVE: Inversion der Kollisions-Kausalität: initiator <-> target
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        // würde aufgerufen werden, wenn eine Rakete Ziel einer Kollision ist
        map.remove(this);
        return true;
    }

}
