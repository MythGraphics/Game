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
    CHEMIKER        (SÄURE),
    KRIEGER         (PHYSISCH),
    MAGIER          (FEUER),
    SCHAMANE        (ELEKTRIZITÄT),
    SOLDAT          (NUKLEAR);

    final DamageType dType;

    CombatantType(DamageType dType) {
        this.dType = dType;
    }

    public DamageType getDamageType() {
        return dType;
    }

}
