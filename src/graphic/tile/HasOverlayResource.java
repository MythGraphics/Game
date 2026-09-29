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

import game.resource.Resource;

@FunctionalInterface
public interface HasOverlayResource {

    Resource getOverlayResource();

}
