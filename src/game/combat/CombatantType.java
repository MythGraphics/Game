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

import static game.combat.DamageType.*;

public enum CombatantType {

    APOTHEKER       (GIFT),
    MAGIER          (FEUER),
    KRIEGER         (PHYSISCH),
    SPACE_MARINE    (NUKLEAR);

    final DamageType dType;

    CombatantType(DamageType dType) {
        this.dType = dType;
    }

    public DamageType getDamageType() {
        return dType;
    }

}
