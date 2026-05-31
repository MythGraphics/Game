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

public interface DamageActionListener extends WeaponActionListener {

    void damagePerformed(Combatant combatant, int dmg);
    void lethalDamagePerformed(Combatant combatant, int dmg, int overkillDmg);

}
