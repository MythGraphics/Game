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

import game.combat.Combatant;

public interface HasMinion {

    Combatant getMinion();
    MinionManager getMinionManager();

}
