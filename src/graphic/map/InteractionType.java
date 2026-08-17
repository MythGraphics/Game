/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public enum InteractionType {

    BOUNDARY,       // map boundary
    ENVIRONMENT,    // interactive environment, impassable (i.e. buildings)
    ENEMY,
    EXIT,           // map exit
    TERRAIN,        // interactive terrain, passable (i.e. bush, gras, mushrooms, ...)
    NONE,           // something that is not null
    NPC,
    PORTAL,
    SURFACE,        // interactive map divider
    TEXT,           // text output
    WALL;           // non-interactive impassable environment

}
