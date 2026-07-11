/*
 *
 */

package graphic.io;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 2.0.0
 *
 */

public enum FileExt {

    MAP             (".map",  TextIO.MAP),
    SET             (".set",  BinaryIO.TILESET), // sprite set
    SPRITE          (".spr",  BinaryIO.SPRITE),
    MOVEABLE_SPRITE (".mspr", BinaryIO.TILESET),
    ANI             (".ani",  BinaryIO.TILESET),
    MOVEABLE_ANI    (".mani", BinaryIO.TILESET);

    private final String ext;
    private final String path;

    FileExt(String ext, String path) {
        this.ext  = ext;
        this.path = path;
    }

    public String getFileExtension() {
        return ext;
    }

    public String getPath() {
        return path;
    }

    public String getFilePath(String filePräfix) {
        return getPath() + filePräfix + getFileExtension();
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
