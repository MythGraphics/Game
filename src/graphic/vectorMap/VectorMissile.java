/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.block.Collidable;
import graphic.block.IsBlockType;
import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import graphic.missileLauncher.IsMissile;
import graphic.missileLauncher.MissileLauncher;
import graphic.tile.BlockTile;
import graphic.tile.IsMoveableTile;
import util.vector.Vector2f;

@SuppressWarnings("CloneableImplementsClone")
public class VectorMissile extends BlockTile implements HasDirectionAngle, IsMoveableTile, IsMissile {

    private final VectorRecord vec;
    private final MissileLauncher source;

    private int collisionDelay = 5;

    public VectorMissile(float angle, int x, int y, IsBlockType bType, float missileSpeed, MissileLauncher source) {
//      super( x, y, vec.width(), vec.height(), bType, source.getMissileImage() );
        super( x, y, bType, source.getMissileImage() );
        float vx    = (float) ( missileSpeed * Math.cos( angle ));
        float vy    = (float) ( missileSpeed * Math.sin( angle ));
        float vmax  = (float) Math.sqrt(vx*vx + vy*vy);
        this.vec    = new VectorRecord(vx, vy, vmax, angle, GameMap.DEFAULT_TILE_SIZE, GameMap.DEFAULT_TILE_SIZE, false);
        this.source = source;
    }

    public VectorMissile(VectorRecord vec, int x, int y, IsBlockType bType, MissileLauncher source) {
//      super( x, y, vec.width(), vec.height(), bType, source.getMissileImage() );
        super( x, y, bType, source.getMissileImage() );
        this.vec    = vec;
        this.source = source;
    }

    @Override
    public float getMoveAngle() {
        return Vector2f.getAngle( vec.vx(), vec.vy() );
    }

    @Override
    public float getViewAngle() {
        return vec.rotAngle();
    }

    @Override
    public void move() {
        if (collisionDelay > 0) {
            --collisionDelay;
        }
        super.x += vec.vx();
        super.y += vec.vy();
    }

    @Override
    public MissileLauncher getSource() {
        return source;
    }

    @Override
    public void collidesWith(Collidable target, GameMap map) {
        if (collisionDelay > 0) {
            return;
        }
        map.remove(this);
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        // würde aufgerufen werden, wenn eine Rakete Ziel einer Kollision ist
        if (collisionDelay > 0) {
            return true;
        }
        map.remove(this);
        return true;
    }

}
