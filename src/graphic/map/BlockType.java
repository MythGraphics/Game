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

public enum BlockType implements HasMapChar {

    BOUNDARY        (Character.MIN_VALUE, InteractionType.BOUNDARY, false), // map boundary

    // basic types; passable
    SPACE           (' ', InteractionType.NONE,         true),  // default space
    SPACEHOLDER     ('.', InteractionType.NONE,         true),  // spaceholder for big sprites
    PORTAL          ('O', InteractionType.PORTAL,       true),  // portal
    EXIT            ('#', InteractionType.EXIT,         true),  // map exit

    // interactive corpses: passable
    CORPSE          ('c', InteractionType.INTERACTIVE,  true),  // interactive corpse
    CORPSE_ENEMY    ('e', InteractionType.INTERACTIVE,  true),  // interactive enemy corpse
    CORPSE_NPC      ('n', InteractionType.INTERACTIVE,  true),  // interactive npc corpse
    CORPSE_PLAYER   ('p', InteractionType.INTERACTIVE,  true),  // interactive player corpse
    UNDEAD_CORPES   ('u', InteractionType.INTERACTIVE,  true),  // interactive undead corpse

    // special/map-dependent types; passable
    BUBBLE          ('B', InteractionType.INTERACTIVE,  true),  // (air) bubble
    WATERLINE       ('-', InteractionType.SURFACE,      true),  // water line (space); 1 per map; no sprite

    // interactive environment; impassable terrain
    ENVIRONMENT_A   ('I', InteractionType.ENV_IMPASS,   false),
    ENVIRONMENT_B   ('J', InteractionType.ENV_IMPASS,   false),
    ENVIRONMENT_C   ('K', InteractionType.ENV_IMPASS,   false),
    ENVIRONMENT_D   ('L', InteractionType.ENV_IMPASS,   false),
    ENVIRONMENT_E   ('M', InteractionType.ENV_IMPASS,   false),

    // interactive types; impassable
    ENEMY           ('E', InteractionType.ENEMY,        false), // interactive enemy
    NPC             ('N', InteractionType.INTERACTIVE,  false), // interactive NPC
    PLAYER          ('P', null,                         false), // player
    TEXTSIGN        ('T', InteractionType.TEXT,         false), // text output
    UNDEAD          ('U', InteractionType.INTERACTIVE,  false), // interactive undead

    // WALL0-9; impassable terrain
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

    // ENVIRONMENT0-9; passable terrain
    ENVIRONMENT0    ('0', InteractionType.ENV_PASS,     true),
    ENVIRONMENT1    ('1', InteractionType.ENV_PASS,     true),
    ENVIRONMENT2    ('2', InteractionType.ENV_PASS,     true),
    ENVIRONMENT3    ('3', InteractionType.ENV_PASS,     true),
    ENVIRONMENT4    ('4', InteractionType.ENV_PASS,     true),
    ENVIRONMENT5    ('5', InteractionType.ENV_PASS,     true),
    ENVIRONMENT6    ('6', InteractionType.ENV_PASS,     true),
    ENVIRONMENT7    ('7', InteractionType.ENV_PASS,     true),
    ENVIRONMENT8    ('8', InteractionType.ENV_PASS,     true),
    ENVIRONMENT9    ('9', InteractionType.ENV_PASS,     true);

    public final char mapChar;
    public final InteractionType iType;

    public boolean passable = false;

    BlockType(char mapChar, InteractionType iType, boolean passable) {
        this.mapChar    = mapChar;
        this.iType      = iType;
        this.passable   = passable;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    @Override
    public char getMapChar() {
        return mapChar;
    }

    @Override
    public InteractionType getInteractionType() {
        return iType;
    }

    public static BlockType getTileType(char c) {
        for ( BlockType btype : BlockType.values() ) {
            if ( btype.getMapChar() == c ) {
                return btype;
            }
        }
        return null;
    }

}
