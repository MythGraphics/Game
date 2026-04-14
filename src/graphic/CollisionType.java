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

    ENV_IMPASS,     // interactive non-passable environment
    ENV_PASS,       // interactive passable environment
    ENEMY,
    EXIT,
    INTERACTIVE,    // interactive game object
    PORTAL,
    SURFACE,        // interactive map object/part
    TEXT,           // text output
    WALL,           // non-interactive impassable environment

}
