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

public interface CombatActionListener extends DamageActionListener {

    void takeDamagePerformed(Combatant combatant, Damage damage);
    void makeDamagePerformed(Combatant c, Damage damage);
    void makeTotalDamagePerformed(Combatant combatant, int dmg);
    void deflectDamagePerformed(Blockable blockable, Damage damage);

}
