/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.Image;

public enum Direction {

    UP,
    LEFT,
    DOWN,
    RIGHT;

    public static Direction parseDirection(char c) {
        switch (c) {
            case 'U': case 'u':
                return UP;
            case 'L': case 'l':
                return LEFT;
            case 'D': case 'd':
                return DOWN;
            case 'R': case 'r':
                return RIGHT;
        }
        return null;
    }

    public static Direction invert(Direction d) {
        switch (d) {
            case UP:    return DOWN;
            case DOWN:  return UP;
            case RIGHT: return LEFT;
            case LEFT:  return RIGHT;
        }
        return null; // sollte niemals erreicht werden
    }

    public static Animation[] buildDirectionalImageSet(Image[] imageset) {
        return Animation.buildDirectionalImageSet(imageset);
    }

}
