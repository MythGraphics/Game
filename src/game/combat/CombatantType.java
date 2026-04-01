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
import java.util.Optional;

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

    public static Optional<CombatantType> getByName(String s) {
        if ( s == null || s.isBlank() ) {
            return Optional.empty();
        }
        try {
            return Optional.of( CombatantType.valueOf( s.toUpperCase().trim() ));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
