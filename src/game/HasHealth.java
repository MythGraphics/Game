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

public interface HasHealth extends HasName {

    Resource getHealth();
    void takeDamage(int damage);
    boolean isAlive();

}
