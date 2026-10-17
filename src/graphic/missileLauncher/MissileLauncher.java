/*
 *
 */

package graphic.missileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import static graphic.Direction.UP;
import graphic.DirectionalImage;
import graphic.HasDirection;
import static graphic.block.DefaultBlockType.MISSILE;
import graphic.map.GameMap;
import graphic.block.IsBlock;
import graphic.tile.Missile;
import graphic.vectorMap.HasDirectionAngle;
import graphic.vectorMap.VectorMissile;

@SuppressWarnings("CloneableImplementsClone")
public abstract class MissileLauncher {

    private final static String NOAMMO = "Feuern nicht möglich: Nicht geladen.";

    protected final DirectionalImage missileImage;

    public MissileLauncher(DirectionalImage missileImage) {
        this.missileImage = missileImage;
    }

    abstract boolean hasMissile();

    public DirectionalImage getMissileImage() {
        // Kopieren (Copy-Contructor), damit jede Missile ihr eigenes DirectionalImage nutzt
        return new DirectionalImage(missileImage);
    }

    // --- Welt 1: rasterbasiert ---

    public void fireMissile(GameMap map, IsBlock source, HasDirection d) {
        fireMissile( map, source, d.getViewDirection() );
    }

    public void fireMissile(GameMap map, IsBlock source, Direction d) {
        if ( !hasMissile() ) {
            System.out.println(NOAMMO);
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

    // --- Welt 2: vektorbasiert ---

    public void fireMissile(GameMap map, IsBlock source, HasDirectionAngle launcher) {
        fireMissile( map, source, launcher.getViewAngle() );
    }

    public void fireMissile(GameMap map, IsBlock source, float angle) {
        if ( !hasMissile() ) {
            System.out.println(NOAMMO);
            return;
        }

        // Position vor der Quelle berechnen
        float spawnDistance = map.tileSize / 2.0f;
        int missileX = (int) Math.ceil(source.getX() + Math.cos(angle) * spawnDistance);
        int missileY = (int) Math.ceil(source.getY() + Math.sin(angle) * spawnDistance);

        map.add( new VectorMissile( angle, missileX, missileY, MISSILE, GameMap.defaultMissileSpeed, this ));
    }

}
