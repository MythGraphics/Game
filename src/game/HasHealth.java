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

public interface HasHealth extends HasName {

    Resource getHealth();
    void takeDamage(int damage);
    boolean isAlive();

}
