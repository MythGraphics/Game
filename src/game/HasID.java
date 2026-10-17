/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

@FunctionalInterface
public interface HasID {

    int getId();

    public static boolean compare(HasID a, HasID b) {
        return a.getId() == b.getId();
    }

}
