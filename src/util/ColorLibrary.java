/*
 *
 */

package util;

/*
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 1.8.1
 *
 */

import java.awt.Color;

public class ColorLibrary {

    private static final int LIGHTBLUE_INT      = 172 << 16 | 207 << 8 | 255;
    private static final int SKYBLUE_INT        = 204 << 8  | 255;
    private static final int LIGHTGREEN_INT     = 102 << 16 | 255 << 8 | 102;
    private static final int DARKGREEN_INT      = 204 << 8;
    private static final int LIGHTRED_INT       = 255 << 16 | 102 << 8 | 102;
    private static final int DARKRED_INT        = 255 << 16;
    private static final int YELLOW_INT         = 255 << 16 | 255 << 8 | 102;
    private static final int GRAY_INT           = 143 << 16 | 143 << 8 | 143;
    private static final int LIGHTGRAY_INT      = 240 << 16 | 240 << 8 | 240;
    private static final int LIGHTPURPLE_INT    = 204 << 16 | 143 << 8 | 255;

    public static final Color LIGHT_BLUE        = new Color(LIGHTBLUE_INT);
    public static final Color SKY_BLUE          = new Color(SKYBLUE_INT);
    public static final Color LIGHT_GREEN       = new Color(LIGHTGREEN_INT);
    public static final Color DARK_GREEN        = new Color(DARKGREEN_INT);
    public static final Color LIGHT_RED         = new Color(LIGHTRED_INT);
    public static final Color DARK_RED          = new Color(DARKRED_INT);
    public static final Color YELLOW            = new Color(YELLOW_INT);
    public static final Color GRAY              = new Color(GRAY_INT);
    public static final Color LIGHT_GRAY        = new Color(LIGHTGRAY_INT);
    public static final Color LIGHT_PURPLE      = new Color(LIGHTPURPLE_INT);

    private ColorLibrary() {}

    public static Color getColor(int value) {
        return new Color(value);
    }

    /**
     * Returns the RGB value representing the color (Bits 16-23 are red, 8-15 are green, 0-7 are blue).
     * Alpha value are not supported by this method (Bits 31-24 are zero).
     * @see java.awt.Color#getRGB()
     * @param color The color object
     * @return the RGB value of the color
     */
    public static int colorToInt(Color color) {
        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        return (r << 16 | g << 8 | b);
    }

    public static Color invert(Color c) {
        return new Color( 255-c.getRed(), 255-c.getGreen(), 255-c.getBlue(), c.getAlpha() );
    }

    public static String colorToDecimalString(Color color) {
        return String.valueOf( colorToInt(color) );
    }

    public static String colorToString(Color color) {
        // Hex-Werte als Rückgabe-String
        return colorToHexString(color);
    }

    public static String colorToHexString(Color color) {
        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        return IntegerToHexString(r) + IntegerToHexString(g) + IntegerToHexString(b);
    }

    private static String IntegerToHexString(int i) {
        // für 2-stellige Hex-Werte
        if (i == 0) {
            return "00";
        }
        String value = Integer.toHexString(i).toUpperCase();
        if ( value.length() < 2 ) {
            value = "0" + value;
        }
        return value;
    }

    /**
     * @param s String des Formats "RRGGBB"(hex)
     * @return The color object represented by the given string.
     */
    public static Color getColor(String s) {
        return new Color( Integer.parseInt(s, 16) );
    }

}
