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

import graphic.Alignment;
import static graphic.Alignment.HORIZONTAL;
import static graphic.Alignment.VERTICAL;
import graphic.Direction;
import static graphic.Direction.parseDirection;
import graphic.map.MapType;
import graphic.map.TileMap;
import graphic.tile.TilesetBuilder;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import util.EnumHelper;

public class DescriptorLoader {

    public final static String MAP = TextIO.MAP;

    private final Class<?> clazz;

    public DescriptorLoader(Class<?> clazz) {
        this.clazz = clazz;
    }

    private static String getFileString(String path, String filename, FileExt ext) {
        if (path == null) {
            path = ".";
        }
        path = getDirPath(path);
        return path + filename + ext.getFileExtension();
    }

    public final static String getDirPath(String path) {
        if ( !path.contains("/") && !path.endsWith( File.pathSeparator )) {
            path += File.separator;
        }
        return path;
    }

    public BufferedImage loadSprite(String path, String filename) throws IOException {
        String descriptor = getFileString(path, filename, FileExt.SPRITE);
        Properties p = TextIO.loadProperties(descriptor, clazz);

        BufferedImage image = BinaryIO.loadImage( path + p.getProperty( "imgfile" ));
        int offsetX         = Integer.parseInt( p.getProperty( "offsetX", "0" ));
        int offsetY         = Integer.parseInt( p.getProperty( "offsetY", "0" ));
        int sizeX           = Integer.parseInt( p.getProperty( "sizeX" ));
        int sizeY           = Integer.parseInt( p.getProperty( "sizeY" ));

        return image.getSubimage(offsetX, offsetY, sizeX, sizeY);
    }

    public BufferedImage[] loadSpriteSet(String path, String filename) throws IOException {
        return loadDescriptor1(path, filename, FileExt.SET);
    }

    public BufferedImage[] loadAnimatedSprite(String path, String filename) throws IOException {
        return loadDescriptor1(path, filename, FileExt.ANI);
    }

    public BufferedImage[] loadMoveableSprite(String path, String filename) throws IOException {
        BufferedImage[][] array = loadDescriptor2(path, filename, FileExt.MOVEABLE_SPRITE);
        BufferedImage[] result  = new BufferedImage[4];
        for (int i = 0; i < 4; ++i) {
            result[i] = array[i][0];
        }
        return result;
    }

    public BufferedImage[][] loadMoveableAnimatedSprite(String path, String filename) throws IOException {
        return loadDescriptor2(path, filename, FileExt.MOVEABLE_ANI);
    }

    // MoveableSprite, MoveableAnimatedSprite
    private BufferedImage[][] loadDescriptor2(String path, String filename, FileExt ext) throws IOException {
        List<BufferedImage> list = new ArrayList<>();
        BufferedImage[][] array  = new BufferedImage[4][];
        String descriptor = getFileString(path, filename, ext);
        Properties p = TextIO.loadProperties(descriptor, clazz);

        BufferedImage image = BinaryIO.loadImage( path + p.getProperty( "imgfile" ));
        int offsetX         = Integer.parseInt( p.getProperty( "offsetX", "0" ));
        int offsetY         = Integer.parseInt( p.getProperty( "offsetY", "0" ));
        int spaceX          = Integer.parseInt( p.getProperty( "spaceX", "0" ));
        int spaceY          = Integer.parseInt( p.getProperty( "spaceY", "0" ));
        int sizeX           = Integer.parseInt( p.getProperty( "sizeX" ));
        int sizeY           = Integer.parseInt( p.getProperty( "sizeY" ));
        int numberOfTiles   = Integer.parseInt( p.getProperty( "numberOfSprites", "1" ));
        Alignment alignment = Alignment.parseAlignment( p.getProperty( "alignment", "H" ).charAt( 0 ));
        String directionStr = p.getProperty("direction");
        Direction[] d       = parseDirection(directionStr);

        TilesetBuilder builder = new TilesetBuilder(image, sizeX, sizeY);
        builder.setSpace(spaceX, spaceY);
        builder.setOffset(offsetX, offsetY);
        switch (alignment) {
            case HORIZONTAL -> builder.setDirection(Direction.RIGHT);
            case VERTICAL   -> builder.setDirection(Direction.DOWN);
        }
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < numberOfTiles; ++i) {
                list.add( builder.nextImage() );
            }
            array[d[j].ordinal()] = list.toArray(BufferedImage[]::new);

            list.clear();
            builder.setCursorOnStart();
            switch (alignment) {
                case HORIZONTAL -> builder.moveCursorDown();
                case VERTICAL   -> builder.moveCursorRight();
            }
        }

        return array;
    }

    // SpriteSet, Animation
    private BufferedImage[] loadDescriptor1(String path, String filename, FileExt ext) throws IOException {
        List<BufferedImage> list = new ArrayList<>();
        String descriptor = getFileString(path, filename, ext);
        Properties p = TextIO.loadProperties(descriptor, clazz);

        BufferedImage image = BinaryIO.loadImage( path + p.getProperty( "imgfile" ));
        int offsetX         = Integer.parseInt( p.getProperty( "offsetX", "0" ));
        int offsetY         = Integer.parseInt( p.getProperty( "offsetY", "0" ));
        int spaceX          = Integer.parseInt( p.getProperty( "spaceX", "0" ));
        int spaceY          = Integer.parseInt( p.getProperty( "spaceY", "0" ));
        int sizeX           = Integer.parseInt( p.getProperty( "sizeX" ));
        int sizeY           = Integer.parseInt( p.getProperty( "sizeY" ));
        int numberOfTiles   = Integer.parseInt( p.getProperty( "numberOfSprites", "1" ));
        Alignment alignment = Alignment.parseAlignment( p.getProperty( "alignment", "H" ).charAt( 0 ));

        TilesetBuilder builder = new TilesetBuilder(image, sizeX, sizeY);
        builder.setSpace(spaceX, spaceY);
        builder.setOffset(offsetX, offsetY);
        switch (alignment) {
            case HORIZONTAL -> builder.setDirection(Direction.RIGHT);
            case VERTICAL   -> builder.setDirection(Direction.DOWN);
        }
        for (int i = 0; i < numberOfTiles; ++i) {
            list.add( builder.nextImage() );
        }

        return list.toArray(BufferedImage[]::new);
    }

    /**
     * Lädt die TileMap vom Dateisystem.
     * @param path Pfad der Map-Datei.
     * @return TileMap
     */
    public static TileMap loadMap(String path) {
        File mapFile;
        mapFile = new File(path);
        if ( !mapFile.exists() ) {
            System.err.println("MapFile don't exists: " + path);
            System.err.println("... maybe not a problem ...");
            mapFile = new File( FileExt.MAP.getFilePath( path ));
            if ( !mapFile.exists() ) {
                System.err.println( "MapFile don't exists: " + mapFile.getPath() );
                return null;
            }
        }

        try ( BufferedReader in = new BufferedReader( new FileReader( mapFile ))) {
            // erste Zeile ist MapType
            MapType type = EnumHelper.getEnumFromString( MapType.class, in.readLine() );
            List<String> list = in.lines()
                                  .filter( line -> !line.isEmpty() )
                                  .toList();
            TileMap map = new TileMap(list, type);
            return map;
        } catch (IOException e) {
            System.err.println("Reading map file failed: " + e.getMessage() );
            return null;
        }
    }

    /**
     * Lädt die TileMap.
     * @param path Pfad der Map-Datei.
     * @param clazz Klassen-Referenz
     * @return TileMap
     */
    public static TileMap loadMap(String path, Class clazz) {
        String mapString = TextIO.loadTextFile(path, clazz);
        if ( mapString == null || mapString.isBlank() ) {
            System.err.println("Map file is NULL or empty: " + path);
            return null;
        }
        List<String> mapList = mapString.lines()
                                        .filter( line -> !line.isEmpty() )
                                        .toList();
        MapType type = EnumHelper.getEnumFromString( MapType.class, mapList.get( 0 ));
        return new TileMap( mapList.subList( 1, mapList.size() ), type );
    }
}
