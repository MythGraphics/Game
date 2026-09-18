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
    CORPSE,
    ENVIRONMENT,    // interactive environment, impassable (i.e. buildings)
    ENEMY,
    EXIT,           // map exit
    MISSILE,
    NONE,           // something that is not null to avoid NullPointerException
    NPC,
    PLAYER,         // Player-Player-Interaktion
    PORTAL,
    SURFACE,        // interactive map divider
    TERRAIN,        // interactive terrain, passable (i.e. bush, gras, mushrooms, ...)
    TEXT,           // text output
    UNDEAD,
    WALL;           // non-interactive impassable environment

}
