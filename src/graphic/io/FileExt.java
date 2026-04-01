/*
 *
 */

package graphic.io;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public enum FileExt {

    MAP             (".map"),
    SET             (".set"), // sprite set
    SPRITE          (".spr"),
    MOVEABLE_SPRITE (".mspr"),
    ANI             (".ani"),
    MOVEABLE_ANI    (".mani");

    private final String ext;

    FileExt(String ext) {
        this.ext = ext;
    }

    public String getFileExtension() {
        return ext;
    }

    public static FileExt getFileExt(SpriteType type) {
        switch (type) {
            case SpriteType.SPRITE:
                return SPRITE;
            case SpriteType.ANIMATED_SPRITE:
                return ANI;
            case SpriteType.MOVEABLE_SPRITE:
                return MOVEABLE_SPRITE;
            case SpriteType.MOVEABLE_ANIMATED_SPRITE:
                return MOVEABLE_ANI;
        }
        return null;
    }

    public static FileExt getByName(String s) {
        if ( !s.startsWith( "." )) {
            s = "." + s;
        }
        for ( FileExt ext : FileExt.values() ) {
            if ( ext.getFileExtension().equalsIgnoreCase( s )) {
                return ext;
            }
        }
        return null;
    }

}
