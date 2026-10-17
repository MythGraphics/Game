/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.resource.Resource;
import game.resource.Resource.ResourceType;

public interface HasResource {

    Resource getResource(ResourceType type);
    void addResource(Resource resource);

}
