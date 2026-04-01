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

public enum CollisionType {

    WALL,           // non-interactive impassable environment
    ENV_PASS,       // interactive passable environment
    ENV_IMPASS,     // interactive non-passable environment
    INTERACTIVE,    // interactive game object
    TEXT,           // text output
    PORTAL,
    EXIT;

}
