/*
 *
 */

package game.combat;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.HasName;

public interface Blockable extends HasName {

    int takeDamage(Damage damage);

}
