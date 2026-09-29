/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 2.0.0
 *
 */

public enum Alignment {

    HORIZONTAL,
    VERTICAL;

    public static Alignment parseAlignment(char a) {
        return switch (a) {
            case 'H', 'h' -> HORIZONTAL;
            case 'V', 'v' -> VERTICAL;
            default       -> null;
        };
    }

}
