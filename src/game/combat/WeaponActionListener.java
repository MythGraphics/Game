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

public interface WeaponActionListener {

    void changeStatsPerformed(AbstractWeapon weapon);
    void normalDamagePerformed(AbstractWeapon weapon, Damage damage);
    void kritDamagePerformed(AbstractWeapon weapon, Damage damage);

}
