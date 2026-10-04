/*
 *
 */

package graphic.MissileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import static graphic.Direction.UP;
import graphic.DirectionalImage;
import static graphic.map.DefaultBlockType.MISSILE;
import graphic.map.GameMap;
import graphic.map.IsBlock;
import graphic.tile.Missile;

@SuppressWarnings("CloneableImplementsClone")
public abstract class MissileLauncher {

    final DirectionalImage missileImage;

    public MissileLauncher(DirectionalImage missileImage) {
        this.missileImage = missileImage;
    }

    abstract boolean hasMissile();

    public DirectionalImage getMissileImage() {
        // Kopieren (Copy-Contructor), damit jede Missile ihr eigenes DirectionalImage nutzt
        return new DirectionalImage(missileImage);
    }

    public void fireMissile(GameMap map, IsBlock source, Direction d) {
        if ( !hasMissile() ) {
            return;
        }

        int missileX = source.getX();
        int missileY = source.getY();
        switch (d) {
            case RIGHT -> missileX += map.tileSize;
            case LEFT  -> missileX -= map.tileSize;
            case DOWN  -> missileY += map.tileSize;
            case UP    -> missileY -= map.tileSize;
        }
        map.add( new Missile( d, missileX, missileY, MISSILE, GameMap.defaultMissileSpeed, this ));
    }

}
