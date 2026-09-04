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

public enum DefaultBlockType implements IsBlockType {

    // end of map
    BOUNDARY        (Character.MIN_VALUE, InteractionType.BOUNDARY, false), // map boundary
    NONE            (Character.MAX_VALUE, InteractionType.NONE,     true),  // something that is not null to avoid NullPointerException

    // basic types; passable
    SPACE           (' ', InteractionType.NONE,         true),  // default space
    SPACEHOLDER     ('.', InteractionType.NONE,         true),  // spaceholder for big sprites
    PORTAL          ('O', InteractionType.PORTAL,       true),  // portal
    EXIT            ('#', InteractionType.EXIT,         true),  // map exit

    // interactive corpses: passable
    CORPSE          ('c', InteractionType.CORPSE,       true),  // interactive corpse
    CORPSE_ENEMY    ('e', InteractionType.CORPSE,       true),  // interactive enemy corpse
    CORPSE_NPC      ('n', InteractionType.CORPSE,       true),  // interactive npc corpse
    CORPSE_PLAYER   ('p', InteractionType.CORPSE,       true),  // interactive player corpse
    CORPSE_UNDEAD   ('u', InteractionType.CORPSE,       true),  // interactive undead corpse

    // special/map-dependent types; passable
    BUBBLE          ('B', InteractionType.TERRAIN,      true),  // (air) bubble
    WATERLINE       ('_', InteractionType.SURFACE,      true),  // water line (space); 1 per map; no sprite

    // interactive environment; impassable
    ENVIRONMENT0   ('I', InteractionType.ENVIRONMENT,   false),
    ENVIRONMENT1   ('J', InteractionType.ENVIRONMENT,   false),
    ENVIRONMENT2   ('K', InteractionType.ENVIRONMENT,   false),
    ENVIRONMENT3   ('L', InteractionType.ENVIRONMENT,   false),
    ENVIRONMENT4   ('M', InteractionType.ENVIRONMENT,   false),

    // interactive entities; impassable
    ENEMY           ('E', InteractionType.ENEMY,        false), // interactive enemy
    NPC             ('N', InteractionType.NPC,          false), // interactive NPC
    PLAYER          ('P', InteractionType.PLAYER,       false), // player
    TEXTSIGN        ('T', InteractionType.TEXT,         false), // text output
    UNDEAD          ('U', InteractionType.UNDEAD,       false), // interactive undead

    // WALL0-9; impassable
    WALL0           ('v', InteractionType.WALL,         false),
    WALL1           ('V', InteractionType.WALL,         false),
    WALL2           ('w', InteractionType.WALL,         false),
    WALL3           ('W', InteractionType.WALL,         false),
    WALL4           ('x', InteractionType.WALL,         false),
    WALL5           ('X', InteractionType.WALL,         false),
    WALL6           ('y', InteractionType.WALL,         false),
    WALL7           ('Y', InteractionType.WALL,         false),
    WALL8           ('z', InteractionType.WALL,         false),
    WALL9           ('Z', InteractionType.WALL,         false),

    // TERRAIN; passable
    TERRAIN         ('+', InteractionType.TERRAIN,      true);

    public final char mapChar;
    public final InteractionType iType;
    public final boolean passable;

    DefaultBlockType(char mapChar, InteractionType iType, boolean passable) {
        this.mapChar    = mapChar;
        this.iType      = iType;
        this.passable   = passable;
    }

    @Override
    public char getMapChar() {
        return mapChar;
    }

    @Override
    public InteractionType getInteractionType() {
        return iType;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }

    public static DefaultBlockType getByChar(char c) {
        for ( DefaultBlockType tile : DefaultBlockType.values() ) {
            if ( tile.getMapChar() == c ) {
                return tile;
            }
        }
        return null;
    }

    public static DefaultBlockType getAliveTile(DefaultBlockType tile) {
        switch (tile) {
            case CORPSE_ENEMY:  return ENEMY;
            case CORPSE_NPC:    return NPC;
            case CORPSE_PLAYER: return PLAYER;
            case CORPSE_UNDEAD: return UNDEAD;
            default:            return tile;
        }
    }

    public static DefaultBlockType getDeadTile(DefaultBlockType tile) {
        switch (tile) {
            case ENEMY:     return CORPSE_ENEMY;
            case NPC:       return CORPSE_NPC;
            case PLAYER:    return CORPSE_PLAYER;
            case UNDEAD:    return CORPSE_UNDEAD;
            default:        return CORPSE;
        }
    }

}
