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

public enum WeaponType {

//  NAME        (crit, block, needAmmo)
    AXT         (25, 25, false),
    DOLCH       (45,  5, false),
    GEWEHR      (30, 30, true ),
    KNÜPPEL     (10, 15, false),
    KRIEGSCLEVE (30, 40, false),
    SCHILD      ( 0, 75, false),
    SCHWERT     (25, 10, false),
    STAB        ( 5, 20, false),
    ZAUBER      (50,  0, true );

    final int crit;  // kritische Trefferchance (pro Waffe/Hand) in %
    final int block; // Block-Chance (pro Waffe/Hand) in %
    final boolean needAmmo; // benötigt sie Munition oder Ressource (Mana)

    WeaponType(int crit, int block, boolean needAmmo) {
        this.crit = crit;
        this.block = block;
        this.needAmmo = needAmmo;
    }

    public int getCrit() {
        return crit;
    }

    public int getBlock() {
        return block;
    }

    public boolean needAmmo() {
        return needAmmo;
    }

}
