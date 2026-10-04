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

import game.resource.Resource;
import graphic.DirectionalImage;

public class ResourceDepententMissileLauncher extends MissileLauncher {

    private final Resource resource;
    private final int cost;

    public ResourceDepententMissileLauncher(DirectionalImage missileImage, Resource resource, int cost) {
        super(missileImage);
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

}
