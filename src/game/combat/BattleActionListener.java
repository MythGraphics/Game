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

public interface BattleActionListener {

    void levelupPerformed(Combatant combatant);
    void killPerformed(Combatant combatant);

}
