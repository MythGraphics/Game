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
import static game.combat.NamePool.*;

public enum WeaponType {

//  NAME    (crit, block, needAmmo)
    AXT     (25, 25, NONE,          WEAPON_AXT),
    DOLCH   (45,  5, NONE,          WEAPON_DOLCH),
    GEWEHR  (30, 30, PROJECTILE,    WEAPON_GEWEHR),
    KNÜPPEL (10, 15, NONE,          WEAPON_KNÜPPEL),
    GLEVE   (35, 45, NONE,          WEAPON_GLEVE),
    SCHILD  ( 0, 75, NONE,          WEAPON_SCHILD),
    SCHWERT (25, 10, NONE,          WEAPON_SCHWERT),
    STAB    ( 1, 20, SPELL,         WEAPON_STAB),
    ZAUBER  (50,  0, SPELL,         WEAPON_ZAUBER);

    final int crit;  // kritische Trefferchance (pro Waffe/Hand) in %
    final int block; // Block-Chance (pro Waffe/Hand) in %
    final AmmoType aType; // benötigte Munition oder Ressource (Mana) oder null
    final String[] namePool;

    WeaponType(int crit, int block, AmmoType aType, String[] namePool) {
        this.crit  = crit;
        this.block = block;
        this.aType = aType;
        this.namePool = namePool;
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

    public String[] getNamePool() {
        return namePool;
    }

}
