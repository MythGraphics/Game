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

public enum Direction {

    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction parseDirection(char c) {
        return switch (c) {
            case 'U', 'u' -> UP;
            case 'R', 'r' -> RIGHT;
            case 'D', 'd' -> DOWN;
            case 'L', 'l' -> LEFT;
            default       -> null;
        };
    }

    public static Direction invert(Direction d) {
        return switch (d) {
            case UP    -> DOWN;
            case RIGHT -> LEFT;
            case DOWN  -> UP;
            case LEFT  -> RIGHT;
        };
    }

    public static Direction[] parseDirection(String s) {
        if ( s.length() != 4 ) {
            throw new IllegalArgumentException("String have to contain 4 and only 4 characters.");
        }
        Direction[] array = new Direction[4];
        for (int i = 0; i < 4; ++i) {
            array[i] = Direction.parseDirection( s.charAt( i ));
        }
        return array;
    }

}
