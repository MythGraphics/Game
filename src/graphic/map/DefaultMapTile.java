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

import graphic.HasImage;
import java.awt.Image;
import java.awt.image.BufferedImage;

public enum DefaultMapTile implements IsChangeableMapTile, HasImage {

    BOUNDARY        (Character.MIN_VALUE, InteractionType.BOUNDARY, false), // map boundary

    // basic types; passable
    SPACE           (' ', InteractionType.NONE,         true),  // default space
    SPACEHOLDER     ('.', InteractionType.NONE,         true),  // spaceholder for big sprites
    PORTAL          ('O', InteractionType.PORTAL,       true),  // portal
    EXIT            ('#', InteractionType.EXIT,         true),  // map exit

    // interactive corpses: passable
    CORPSE          ('c', InteractionType.TERRAIN,      true),  // interactive corpse
    CORPSE_ENEMY    ('e', InteractionType.TERRAIN,      true),  // interactive enemy corpse
    CORPSE_NPC      ('n', InteractionType.TERRAIN,      true),  // interactive npc corpse
    CORPSE_PLAYER   ('p', InteractionType.TERRAIN,      true),  // interactive player corpse
    CORPSE_UNDEAD   ('u', InteractionType.TERRAIN,      true),  // interactive undead corpse

    // special/map-dependent types; passable
    BUBBLE          ('B', InteractionType.TERRAIN,      true),  // (air) bubble
    WATERLINE       ('-', InteractionType.SURFACE,      true),  // water line (space); 1 per map; no sprite

    // interactive environment; impassable terrain
    ENVIRONMENT_A   ('I', InteractionType.ENVIRONMENT,  false),
    ENVIRONMENT_B   ('J', InteractionType.ENVIRONMENT,  false),
    ENVIRONMENT_C   ('K', InteractionType.ENVIRONMENT,  false),
    ENVIRONMENT_D   ('L', InteractionType.ENVIRONMENT,  false),
    ENVIRONMENT_E   ('M', InteractionType.ENVIRONMENT,  false),

    // interactive types; impassable
    ENEMY           ('E', InteractionType.ENEMY,        false), // interactive enemy
    NPC             ('N', InteractionType.NPC,          false), // interactive NPC
    PLAYER          ('P', null,                         false), // player
    TEXTSIGN        ('T', InteractionType.TEXT,         false), // text output
    UNDEAD          ('U', InteractionType.NPC,          false), // interactive undead

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
    ENVIRONMENT0    ('0', InteractionType.TERRAIN,      true),
    ENVIRONMENT1    ('1', InteractionType.TERRAIN,      true),
    ENVIRONMENT2    ('2', InteractionType.TERRAIN,      true),
    ENVIRONMENT3    ('3', InteractionType.TERRAIN,      true),
    ENVIRONMENT4    ('4', InteractionType.TERRAIN,      true),
    ENVIRONMENT5    ('5', InteractionType.TERRAIN,      true),
    ENVIRONMENT6    ('6', InteractionType.TERRAIN,      true),
    ENVIRONMENT7    ('7', InteractionType.TERRAIN,      true),
    ENVIRONMENT8    ('8', InteractionType.TERRAIN,      true),
    ENVIRONMENT9    ('9', InteractionType.TERRAIN,      true);

    public final char mapChar;

    public InteractionType iType;
    public boolean passable = false;

    private BufferedImage image;

    DefaultMapTile(char mapChar, InteractionType iType, boolean passable) {
        this.mapChar    = mapChar;
        this.iType      = iType;
        this.passable   = passable;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }

    @Override
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

    @Override
    public void setInteractionType(InteractionType iType) {
        this.iType = iType;
    }

    public static DefaultMapTile getMapTile(char c) {
        for ( DefaultMapTile tile : DefaultMapTile.values() ) {
            if ( tile.getMapChar() == c ) {
                return tile;
            }
        }
        return null;
    }

    @Override
    public IsChangeableMapTile getAliveTile(IsChangeableMapTile tile) {
        switch (tile) {
            case CORPSE_ENEMY:  return ENEMY;
            case CORPSE_NPC:    return NPC;
            case CORPSE_PLAYER: return PLAYER;
            case CORPSE_UNDEAD: return UNDEAD;
            default:            return tile;
        }
    }

    @Override
    public IsChangeableMapTile getDeadTile(IsChangeableMapTile tile) {
        switch (tile) {
            case ENEMY:     return CORPSE_ENEMY;
            case NPC:       return CORPSE_NPC;
            case PLAYER:    return CORPSE_PLAYER;
            case UNDEAD:    return CORPSE_UNDEAD;
            default:        return CORPSE;
        }
    }

    @Override
    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
