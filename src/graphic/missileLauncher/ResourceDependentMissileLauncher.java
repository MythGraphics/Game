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

import game.resource.Resource;
import graphic.Direction;
import graphic.HasImage;
import graphic.block.IsBlock;
import graphic.map.GameMap;
import graphic.vectorMap.VectorBlock;

public class ResourceDependentMissileLauncher extends MissileLauncher {

    private final Resource resource;
    private final int cost;

    public ResourceDependentMissileLauncher(IsBlock owner, HasImage missileImage, Resource resource, int cost) {
        super(owner, missileImage);
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be NULL.");
        }
        this.resource = resource;
        this.cost     = cost;
    }

    public Resource getResource() {
        return resource;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean hasMissile() {
        return resource.getValue() >= cost;
    }

    @Override
    public boolean fireMissile(GameMap map, IsBlock source, Direction d) {
        if ( super.fireMissile( map, source, d )) {
            resource.consume(cost);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean fireMissile(GameMap map, VectorBlock source, float angle) {
        if ( super.fireMissile( map, source, angle )) {
            resource.consume(cost);
            return true;
        } else {
            return false;
        }
    }

}
