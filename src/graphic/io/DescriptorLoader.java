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

import graphic.Direction;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DescriptorLoader {

    public final static String FILENAME_PREFIX = "descriptor";

    private final Class<?> clazz;

    public DescriptorLoader(Class<?> clazz) {
        this.clazz = clazz;
    }

    public BufferedImage[] loadSprites(String path) throws IOException {
        ArrayList<BufferedImage> list = new ArrayList<>();
        String descriptor = getFileString(path, FileExt.SPRITE);
        ArrayList<String> entries = loadDescriptor(descriptor);
        for ( String line : entries ) {
            list.add( parseDescriptorLine1( line, path ));
        }
        return list.toArray(BufferedImage[]::new);
    }

    public BufferedImage[][] loadSpriteSets(String path) throws IOException {
        return loadDescriptor2(path, FileExt.SET);
    }

    public BufferedImage[][] loadAnimatedSprites(String path) throws IOException {
        return loadDescriptor2(path, FileExt.ANI);
    }

    public BufferedImage[][] loadMoveableSprites(String path) throws IOException {
        return loadDescriptor2(path, FileExt.MOVEABLE_SPRITE);
    }

    public BufferedImage[][][] loadMoveableAnimatedSprites(String path) throws IOException {
        ArrayList<BufferedImage[][]> list = new ArrayList<>();
        String descriptor = getFileString(path, FileExt.MOVEABLE_ANI);
        ArrayList<String> entries = loadDescriptor(descriptor);
        for ( String line : entries ) {
            list.add( parseDescriptorLine3( line, path ));
        }
        return list.toArray(BufferedImage[][][]::new);
    }

    private static String getFileString(String path, FileExt ext) {
        if ( path == null ) { path = "."; }
        path = getDirPath(path);
        return path + FILENAME_PREFIX + ext.getFileExtension();
    }

    private ArrayList<String> loadDescriptor(String descriptor) {
        ArrayList<String> list = new ArrayList<>();
        try ( BufferedReader reader = TextIO.getTextReader( descriptor, clazz )) {
            String line;
            while (( line = reader.readLine() ) != null ) {
                if ( line.startsWith( "#" )) { continue; } // Kommentarzeile überspringen
                list.add(line);
            }
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Descriptor-Datei: " + e.getMessage() );
        }
        return list;
    }

    public final static String getDirPath(String path) {
        if ( !path.contains("/") && !path.endsWith( File.pathSeparator )) {
            path += File.separator;
        }
        return path;
    }

    private static BufferedImage parseDescriptorLine1(String line, String path) throws IOException {
        path = getDirPath(path);
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        if ( tokenizer.countTokens() < 5 ) {
            throw new IOException("Format not parsable. Neccessary 5 tokens not found.");
        }
        BufferedImage image = BinaryIO.loadImage( path + tokenizer.nextToken() );
        int offset_x        = Integer.parseInt( tokenizer.nextToken() );
        int offset_y        = Integer.parseInt( tokenizer.nextToken() );
        int size_x          = Integer.parseInt( tokenizer.nextToken() );
        int size_y          = Integer.parseInt( tokenizer.nextToken() );
        return image.getSubimage( offset_x, offset_y, size_x, size_y );
    }

    // SpriteSet, AnimatedSprites & MoveableSprites
    private BufferedImage[][] loadDescriptor2(String path, FileExt ext) throws IOException {
        ArrayList<BufferedImage[]> list = new ArrayList<>();
        String descriptor = getFileString(path, ext);
        ArrayList<String> entries = loadDescriptor(descriptor);
        for ( String line : entries ) {
            list.add( parseDescriptorLine2( line, path ));
        }
        return list.toArray(BufferedImage[][]::new);
    }

    private static BufferedImage[] parseDescriptorLine2(String line, String path) throws IOException {
        path = getDirPath(path);
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        if ( tokenizer.countTokens() < 7 ) {
            throw new IOException("Format not parsable. Neccessary 7 tokens not found.");
        }
        BufferedImage image = BinaryIO.loadImage( path + tokenizer.nextToken() );
        char alignment      = tokenizer.nextToken().charAt(0);
        int offset_x        = Integer.parseInt( tokenizer.nextToken() );
        int offset_y        = Integer.parseInt( tokenizer.nextToken() );
        int size_x          = Integer.parseInt( tokenizer.nextToken() );
        int size_y          = Integer.parseInt( tokenizer.nextToken() );
        int number          = Integer.parseInt( tokenizer.nextToken() );
        image = image.getSubimage( offset_x, offset_y, image.getWidth(), image.getHeight() );
        switch (alignment) {
            case 'x': case 'X':
                return TilesetUtility.getSpriteSetHorizontal(image, size_x, 0, number);
            case 'y': case 'Y':
                return TilesetUtility.getSpriteSetVertical(image, size_y, 0, number);
        }
        throw new IOException("Format not parsable. Sprite_alignment not x nor y.");
    }

    private static BufferedImage[][] parseDescriptorLine3(String line, String path) throws IOException {
        path = getDirPath(path);
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        BufferedImage[][] dirImgs = new BufferedImage[4][];
        if ( tokenizer.countTokens() < 8 ) {
            throw new IOException("Format not parsable. Neccessary 8 tokens not found.");
        }
        BufferedImage image = BinaryIO.loadImage( path + tokenizer.nextToken() );
        char alignment      = tokenizer.nextToken().charAt(0);
        String dir_alignm   = tokenizer.nextToken();
        int offset_x        = Integer.parseInt( tokenizer.nextToken() );
        int offset_y        = Integer.parseInt( tokenizer.nextToken() );
        int size_x          = Integer.parseInt( tokenizer.nextToken() );
        int size_y          = Integer.parseInt( tokenizer.nextToken() );
        int number          = Integer.parseInt( tokenizer.nextToken() );
        image = image.getSubimage( offset_x, offset_y, image.getWidth(), image.getHeight() );
        BufferedImage currentImg;
        switch (alignment) {
            case 'x': case 'X':
                for (int i = 0; i < dirImgs.length; ++i) {
                    currentImg = image.getSubimage( 0, i*size_y, image.getWidth(), image.getHeight() );
                    int index = Direction.parseDirection( dir_alignm.charAt( i )).ordinal();
                    dirImgs[index] = TilesetUtility.getSpriteSetHorizontal(currentImg, size_x, 0, number);
                    return dirImgs;
                }
            case 'y': case 'Y':
                for (int i = 0; i < dirImgs.length; ++i) {
                    currentImg = image.getSubimage( i*size_x, 0, image.getWidth(), image.getHeight() );
                    int index = Direction.parseDirection( dir_alignm.charAt( i )).ordinal();
                    dirImgs[index] = TilesetUtility.getSpriteSetVertical(currentImg, size_y, 0, number);
                    return dirImgs;
                }
        }
        throw new IOException("Format not parsable. Sprite alignment not x nor y.");
    }

    public static String[] loadMap(String path) {
        ArrayList<String> list = new ArrayList<>();
        File mapfile;
        mapfile = new File( getFileString( path, FileExt.MAP ));
        if ( !mapfile.exists() ) {
            System.err.println("MapFile don't exists.");
            return null;
        }
        try ( BufferedReader in = new BufferedReader( new FileReader( mapfile ))) {
            in.lines().forEach((line) -> {
                if ( line.charAt(0) != ';' ) {
                    list.add(line);
                }
            });
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Map-Datei: " + e.getMessage() );
            return null;
        }
        return list.toArray(String[]::new);
    }

}
