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

    BOUNDARY        (Character.MIN_VALUE, CollisionType.BOUNDARY, false), // map boundary

    // basic types; passable
    SPACE           (' ', CollisionType.NONE,           true),  // default space
    SPACEHOLDER     ('.', CollisionType.NONE,           true),  // spaceholder for big sprites
    PORTAL          ('O', CollisionType.PORTAL,         true),  // portal
    EXIT            ('#', CollisionType.EXIT,           true),  // map exit

    // interactive corpses: passable
    CORPSE          ('c', CollisionType.INTERACTIVE,    true),  // interactive corpse
    CORPSE_ENEMY    ('e', CollisionType.INTERACTIVE,    true),  // interactive enemy corpse
    CORPSE_NPC      ('n', CollisionType.INTERACTIVE,    true),  // interactive npc corpse
    CORPSE_PLAYER   ('p', CollisionType.INTERACTIVE,    true),  // interactive player corpse
    UNDEAD_CORPES   ('u', CollisionType.INTERACTIVE,    true),  // interactive undead corpse

    // special/map-dependent types; passable
    BUBBLE          ('B', CollisionType.INTERACTIVE,    true),  // (air) bubble
    WATERLINE       ('-', CollisionType.SURFACE,        true),  // water line (space); 1 per map; no sprite

    // interactive environment; impassable terrain
    ENVIRONMENT_A   ('I', CollisionType.ENV_IMPASS,     false),
    ENVIRONMENT_B   ('J', CollisionType.ENV_IMPASS,     false),
    ENVIRONMENT_C   ('K', CollisionType.ENV_IMPASS,     false),
    ENVIRONMENT_D   ('L', CollisionType.ENV_IMPASS,     false),
    ENVIRONMENT_E   ('M', CollisionType.ENV_IMPASS,     false),

    // interactive types; impassable
    ENEMY           ('E', CollisionType.ENEMY,          false), // interactive enemy
    NPC             ('N', CollisionType.INTERACTIVE,    false), // interactive NPC
    PLAYER          ('P', null,                         false), // player
    TEXTSIGN        ('T', CollisionType.TEXT,           false), // text output
    UNDEAD          ('U', CollisionType.INTERACTIVE,    false), // interactive undead

    // WALL0-9; impassable terrain
    WALL0           ('v', CollisionType.WALL,           false),
    WALL1           ('V', CollisionType.WALL,           false),
    WALL2           ('w', CollisionType.WALL,           false),
    WALL3           ('W', CollisionType.WALL,           false),
    WALL4           ('x', CollisionType.WALL,           false),
    WALL5           ('X', CollisionType.WALL,           false),
    WALL6           ('y', CollisionType.WALL,           false),
    WALL7           ('Y', CollisionType.WALL,           false),
    WALL8           ('z', CollisionType.WALL,           false),
    WALL9           ('Z', CollisionType.WALL,           false),

    // ENVIRONMENT0-9; passable terrain
    ENVIRONMENT0    ('0', CollisionType.ENV_PASS,       true),
    ENVIRONMENT1    ('1', CollisionType.ENV_PASS,       true),
    ENVIRONMENT2    ('2', CollisionType.ENV_PASS,       true),
    ENVIRONMENT3    ('3', CollisionType.ENV_PASS,       true),
    ENVIRONMENT4    ('4', CollisionType.ENV_PASS,       true),
    ENVIRONMENT5    ('5', CollisionType.ENV_PASS,       true),
    ENVIRONMENT6    ('6', CollisionType.ENV_PASS,       true),
    ENVIRONMENT7    ('7', CollisionType.ENV_PASS,       true),
    ENVIRONMENT8    ('8', CollisionType.ENV_PASS,       true),
    ENVIRONMENT9    ('9', CollisionType.ENV_PASS,       true);

    public final char mapChar;
    public final CollisionType cType;

    public boolean passable = false;

    BlockType(char mapChar, CollisionType cType, boolean passable) {
        this.mapChar = mapChar;
        this.cType = cType;
        this.passable = passable;
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
    public CollisionType getCollisionType() {
        return cType;
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
