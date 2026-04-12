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

    ELEKTRIZITÄT    ( new Color( 255, 255,   0 ), "des Blitzes" ),
    GIFT            ( new Color( 153, 255, 153 ), "der Viper" ),
    FEUER           ( new Color( 255, 153, 153 ), "des Infernos" ),
    NUKLEAR         ( new Color( 102, 255, 255 ), "der Sonne" ),
    PHYSISCH        ( new Color( 242, 242, 242 ), "der Gewalt" ),
    SÄURE           ( new Color( 255, 153, 255 ), "der Zersetzung" );
    // helles violett: 195, 153, 255

    private final Color color;
    private final String suffix;

    DamageType(Color color, String suffix) {
        this.color = color;
        this.suffix = suffix;
    }

    public Color getColor() {
        return color;
    }

    public String getSuffix() {
        return suffix;
    }

}
