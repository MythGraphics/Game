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
import static graphic.io.ImageUtility.scale;
import static graphic.map.GameMap.DEFAULT_TILE_SIZE;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

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

    public Image[][] loadSpriteSets(String path) throws IOException {
        return loadDescriptor2(path, FileExt.SET);
    }

    public Image[][] loadAnimatedSprites(String path) throws IOException {
        return loadDescriptor2(path, FileExt.ANI);
    }

    public Image[][] loadMoveableSprites(String path) throws IOException {
        return loadDescriptor2(path, FileExt.MOVEABLE_SPRITE);
    }

    public Image[][][] loadMoveableAnimatedSprites(String path) throws IOException {
        ArrayList<Image[][]> list = new ArrayList<>();
        String descriptor = getFileString(path, FileExt.MOVEABLE_ANI);
        ArrayList<String> entries = loadDescriptor(descriptor);
        for ( String line : entries ) {
            list.add( parseDescriptorLine3( line, path ));
        }
        return list.toArray(BufferedImage[][][]::new);
    }

    private static String getFileString(String path, FileExt ext) {
        if ( path == null ) { path = "."; }
        if ( !path.endsWith( File.separator )) { path += File.separator; }
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

    private static BufferedImage parseDescriptorLine1(String line, String path) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        if ( tokenizer.countTokens() < 5 ) {
            throw new IOException("Format not parsable. Neccessary 5 tokens not found.");
        }
        BufferedImage image = scale( ImageIO.read( new File( path + tokenizer.nextToken() )), 200 );
        int offset_x        = Integer.parseInt( tokenizer.nextToken() );
        int offset_y        = Integer.parseInt( tokenizer.nextToken() );
        int size_x          = Integer.parseInt( tokenizer.nextToken() );
        int size_y          = Integer.parseInt( tokenizer.nextToken() );
        return image.getSubimage( offset_x, offset_y, size_x, size_y );
    }

    // SpriteSet, AnimatedSprites & MoveableSprites
    private Image[][] loadDescriptor2(String path, FileExt ext) throws IOException {
        ArrayList<Image[]> list = new ArrayList<>();
        String descriptor = getFileString(path, ext);
        ArrayList<String> entries = loadDescriptor(descriptor);
        for ( String line : entries ) {
            list.add( parseDescriptorLine2( line, path ));
        }
        return list.toArray(BufferedImage[][]::new);
    }

    private static Image[] parseDescriptorLine2(String line, String path) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        if ( tokenizer.countTokens() < 7 ) {
            throw new IOException("Format not parsable. Neccessary 7 tokens not found.");
        }
        BufferedImage image = scale( ImageIO.read( new File( path + tokenizer.nextToken() )), 200 );
        char alignment      = tokenizer.nextToken().charAt(0);
        int offset_x        = Integer.parseInt( tokenizer.nextToken() );
        int offset_y        = Integer.parseInt( tokenizer.nextToken() );
        int size_x          = Integer.parseInt( tokenizer.nextToken() );
        int size_y          = Integer.parseInt( tokenizer.nextToken() );
        int number          = Integer.parseInt( tokenizer.nextToken() );
        image = image.getSubimage( offset_x, offset_y, image.getWidth(), image.getHeight() );
        switch (alignment) {
            case 'x': case 'X':
                return TilesetUtility.getSpriteSetHorizontal(image, size_x, number, DEFAULT_TILE_SIZE);
            case 'y': case 'Y':
                return TilesetUtility.getSpriteSetVertical(image, size_y, number, DEFAULT_TILE_SIZE);
        }
        throw new IOException("Format not parsable. Sprite_alignment not x nor y.");
    }

    private static Image[][] parseDescriptorLine3(String line, String path) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(line, ":", false);
        Image[][] dirImgs = new BufferedImage[4][];
        if ( tokenizer.countTokens() < 8 ) {
            throw new IOException("Format not parsable. Neccessary 8 tokens not found.");
        }
        BufferedImage image = scale( ImageIO.read( new File( path + tokenizer.nextToken() )), 200 );
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
                    dirImgs[index] = TilesetUtility.getSpriteSetHorizontal(currentImg, size_x, number, DEFAULT_TILE_SIZE);
                    return dirImgs;
                }
            case 'y': case 'Y':
                for (int i = 0; i < dirImgs.length; ++i) {
                    currentImg = image.getSubimage( i*size_x, 0, image.getWidth(), image.getHeight() );
                    int index = Direction.parseDirection( dir_alignm.charAt( i )).ordinal();
                    dirImgs[index] = TilesetUtility.getSpriteSetVertical(currentImg, size_y, number, DEFAULT_TILE_SIZE);
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
