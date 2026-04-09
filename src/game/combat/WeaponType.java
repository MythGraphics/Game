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

import static game.combat.AmmoType.*;

public enum WeaponType {

//  NAME        (crit, block, needAmmo)
    AXT         (25, 25, NONE),
    DOLCH       (45,  5, NONE),
    GEWEHR      (30, 30, PROJECTILE ),
    KNÜPPEL     (10, 15, NONE),
    KRIEGSCLEVE (35, 45, NONE),
    SCHILD      ( 0, 75, NONE),
    SCHWERT     (25, 10, NONE),
    STAB        ( 1, 20, NONE),
    ZAUBER      (50,  0, SPELL );

    final int crit;  // kritische Trefferchance (pro Waffe/Hand) in %
    final int block; // Block-Chance (pro Waffe/Hand) in %
    final AmmoType aType; // benötigte Munition oder Ressource (Mana) oder null

    WeaponType(int crit, int block, AmmoType aType) {
        this.crit  = crit;
        this.block = block;
        this.aType = aType;
    }

    public int getCrit() {
        return crit;
    }

    public int getBlock() {
        return block;
    }

    public AmmoType getAmmoType() {
        if (aType == null) {
            return NONE;
        } else {
            return aType;
        }
    }

    public boolean needAmmo() {
        return !(aType == null || aType == NONE);
    }

}
