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

import java.awt.Color;

public enum DamageType {

    ELEKTRIZITÄT    (new Color( 255, 255,   0 )),
    GIFT            (new Color( 153, 255, 153 )),
    FEUER           (new Color( 255, 153, 153 )),
    NUKLEAR         (new Color( 102, 255, 255 )),
    PHYSISCH        (new Color( 242, 242, 242 )),
    SÄURE           (new Color( 255, 153, 255 )); // helles violett: 195, 153, 255

    private final Color color;

    DamageType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
