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
import graphic.HasDirection;
import graphic.HasImage;
import static graphic.block.DefaultBlockType.MISSILE;
import graphic.block.IsBlock;
import graphic.map.GameMap;
import graphic.tile.Missile;
import graphic.vectorMap.HasDirectionAngle;
import graphic.vectorMap.VectorBlock;
import graphic.vectorMap.VectorMissile;

@SuppressWarnings("CloneableImplementsClone")
public abstract class MissileLauncher {

    public final static String NOAMMO = "Feuern nicht möglich: Nicht geladen.";

    protected final HasImage missileImage;
    protected final IsBlock owner;

    public MissileLauncher(IsBlock owner, HasImage missileImage) {
        this.owner        = owner;
        this.missileImage = missileImage;
    }

    abstract boolean hasMissile();

    public IsBlock getOwner() {
        return owner;
    }

    public HasImage getMissileImage() {
        // Kopieren, damit jede Missile ihr eigenes HasImage nutzt
//      return () -> missileImage.getImage();
        return missileImage::getImage;
    }

    // --- Welt 1: rasterbasiert ---

    public boolean fireMissile(GameMap map, IsBlock source, HasDirection d) {
        return fireMissile( map, source, d.getViewDirection() );
    }

    public boolean fireMissile(GameMap map, IsBlock source, Direction d) {
        if ( !hasMissile() ) {
            System.out.println(NOAMMO);
            return false;
        }

        int missileX = source.getX();
        int missileY = source.getY();
        switch (d) {
            case RIGHT -> missileX += map.tileSize;
            case LEFT  -> missileX -= map.tileSize;
            case DOWN  -> missileY += map.tileSize;
            case UP    -> missileY -= map.tileSize;
        }
        map.add( new Missile( d, missileX, missileY, MISSILE, GameMap.defaultMissileSpeed, map.getMaxPoint(), this ));
        return true;
    }

    // --- Welt 2: vektorbasiert ---

    public boolean fireMissile(GameMap map, VectorBlock source, HasDirectionAngle launcher) {
        return fireMissile( map, source, launcher.getViewAngle() );
    }

    public boolean  fireMissile(GameMap map, VectorBlock source, float angle) {
        if ( !hasMissile() ) {
            System.out.println(NOAMMO);
            return false;
        }

        // Position vor der Quelle berechnen
        float spawnDistance = map.tileSize / 2.0f;
        int missileX = (int) Math.ceil(source.getX() + Math.cos(angle) * spawnDistance);
        int missileY = (int) Math.ceil(source.getY() + Math.sin(angle) * spawnDistance);

        float missileSpeed = source.getSpeed() + 4.0f;

        map.add( new VectorMissile( angle, missileX, missileY, MISSILE, missileSpeed, this ));
        return true;
    }

}
