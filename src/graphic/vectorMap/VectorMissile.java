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
import graphic.block.IsBlock;
import graphic.block.IsBlockType;
import graphic.map.GameMap;
import graphic.map.IsCollider;
import graphic.map.IsCollisionHandler;
import graphic.missileLauncher.MissileLauncher;
import graphic.tile.BlockTile;
import graphic.tile.IsMoveableTile;

@SuppressWarnings("CloneableImplementsClone")
public class VectorMissile extends BlockTile implements IsCollider, HasDirectionAngle, IsMoveableTile {

    private final MissileLauncher source;
    private final VectorRecord vec;

    public VectorMissile(float angle, int x, int y, IsBlockType bType, int missileSpeed, MissileLauncher source) {
//      super( x, y, vec.width(), vec.height(), bType, source.getMissileImage() );
        super( x, y, bType, source.getMissileImage() );
        this.source = source;
        float vx    = (float) ( missileSpeed * Math.cos( angle ));
        float vy    = (float) ( missileSpeed * Math.sin( angle ));
        this.vec    = new VectorRecord(vx, vy, angle, GameMap.DEFAULT_TILE_SIZE, GameMap.DEFAULT_TILE_SIZE, false);
    }

    public VectorMissile(VectorRecord vec, int x, int y, IsBlockType bType, MissileLauncher source) {
//      super( x, y, vec.width(), vec.height(), bType, source.getMissileImage() );
        super( x, y, bType, source.getMissileImage() );
        this.vec    = vec;
        this.source = source;
    }

    @Override
    public float getMoveAngle() {
        return VectorBlock.getAngleFromVelocity( vec.vx(), vec.vy() );
    }

    @Override
    public float getViewAngle() {
        return vec.rotAngle();
    }

    @Override
    public void move() {
        // ToDo implementieren
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        map.remove(this);
        return super.onCollision(initiator, map, handler);
    }

    @Override
    public void collidesWith(IsBlock target, GameMap map, IsCollisionHandler handler) {
        map.remove(this);
    }

}
