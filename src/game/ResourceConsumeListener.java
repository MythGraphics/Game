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
public interface ResourceConsumeListener {

    void resourceConsumePerformed(Resource r, int use, int overuse);

}
