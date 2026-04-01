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

import graphic.map.GameMap;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TilesetUtility {

    public final static char HORIZONTAL = 'h';
    public final static char VERTICAL   = 'v';

    private TilesetUtility() {}

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param start Startposition
     * @param space_x Abstand zwischen Sprites in x-Achse
     * @param space_y Abstand zwischen Sprites in y-Achse
     * @param tilesize Größe des Sprite-Blocks (tilesize x tilesize Pixel)
     * @param number Anzahl der zu ladenden Sprites oder -1 für alle
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(
        BufferedImage image, Point start, int space_x, int space_y, int tilesize, int number
    ) {
        ArrayList<BufferedImage> list = new ArrayList<>();
        for ( int y = start.y, x = start.x; y+tilesize <= image.getHeight(); y += tilesize+space_y, x = start.x ) {
            for (; x+tilesize <= image.getWidth(); x += tilesize+space_x ) {
                list.add( image.getSubimage( x, y, tilesize, tilesize ));
                if ( list.size() == number ) {
                    return list.toArray(new BufferedImage[0]);
                }
            }
        }
        return list.toArray(new BufferedImage[0]);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * Die Länge des Sprites entspricht der Gesamtlänge des Bildes.
     * @param image Quell-Bild
     * @param width Ausdehnung eines Sprites in x-Achse (Breite) oder 0 für anteilige Breite basierend auf der
     *              Gesamtbreite des Bildes
     * @param number Anzahl der zu ladenden Sprites
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetHorizontal(BufferedImage image, int width, int number) {
        if ( width <= 0 ) {
            width = image.getWidth() / number;
        }
        return getSpriteSet( image, image.getHeight(), width, number, HORIZONTAL );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * Die Breite des Sprites entspricht der Gesamtbreite des Bildes.
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge) oder 0 für anteilige Länge basierend auf der
     *               Gesamtlänge des Bildes
     * @param number Anzahl der zu ladenden Sprites
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetVertical(BufferedImage image, int height, int number) {
        if ( height <= 0 ) {
            height = image.getHeight() / number;
        }
        return getSpriteSet( image, height, image.getWidth(), number, VERTICAL );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge)
     * @param width Ausdehnung eines Sprites in x-Achse (Breite)
     * @param number Anzahl der zu ladenden Sprites
     * @param alignment Ausrichtung des SpriteSets (horizontal oder vertical)
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(BufferedImage image, int height, int width, int number, char alignment) {
        BufferedImage subimg;
        ArrayList<BufferedImage> list = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            subimg = image.getSubimage( 0, 0, width, height );
            switch (alignment) {
                case HORIZONTAL -> subimg = image.getSubimage( i*width, 0, width, height );
                case VERTICAL   -> subimg = image.getSubimage( 0, i*height, width, height );
            }
            if ( subimg.getHeight() < GameMap.DEFAULT_TILE_SIZE || subimg.getWidth() < GameMap.DEFAULT_TILE_SIZE ) {
                subimg = adjustSprite(subimg);
            }
            list.add(subimg);
        }
        return list.toArray(BufferedImage[]::new);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein 2D-BufferedImage-Array (AnimationSet).
     * @param image Quell-Bild
     * @param start Startposition
     * @param space_x Abstand zwischen Sprites in x-Achse
     * @param space_y Abstand zwischen Sprites in y-Achse
     * @param tilesize Größe des Sprite-Blocks (tilesize x tilesize Pixel)
     * @param numbers Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static Image[][] getAnimationSet(
        BufferedImage image, Point start, int space_x, int space_y, int tilesize, int numbers
    ) {
        if ( image.getWidth() < ( numbers*(tilesize+space_x)+start.x )) {
            numbers = -1;
        }
        if (numbers == -1) {
            numbers = image.getWidth()-start.x/(tilesize+space_x);
        }
        int amountX = numbers;
        int amountY = image.getHeight()-start.y/(tilesize+space_y);
        BufferedImage[][] array = new BufferedImage[amountY][];
        for (int i = 0; i < amountY; ++i) {
            array[i] = new BufferedImage[amountX];
        }
        Point pos = start;
        for ( int i = 0;
              pos.y+tilesize <= image.getHeight();
              pos.y += tilesize+space_y, pos.x = start.x, ++i
        ) {
            for ( int j = 0; pos.x+tilesize <= image.getWidth(); pos.x += tilesize+space_x, ++j ) {
                array[i][j] = image.getSubimage( pos.x, pos.y, tilesize, tilesize );
                if ( j == amountX-1 ) { break; }
            }
            if ( i == amountY-1 ) { break; }
        }
        return array;
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein 2D-BufferedImage-Array (AnimationSet).
     * Lädt zeilenweise von links nach rechts und von oben nach unten.
     * @param image Quell-Bild
     * @param space_x Abstand zwischen Sprites in x-Achse
     * @param space_y Abstand zwischen Sprites in y-Achse
     * @param width Länge eines Sprites
     * @param height Höhe eines Sprites
     * @param numbers Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static Image[][] getAnimationSet(
        BufferedImage image, int space_x, int space_y, int width, int height, int numbers
    ) {
        if ( image.getWidth() < numbers*(width+space_x)) {
            numbers = -1;
        }
        if (numbers == -1) {
            numbers = image.getWidth()/(width+space_x);
        }
        int amountX = numbers;
        int amountY = image.getHeight()/(height+space_y);
        BufferedImage[][] array = new BufferedImage[amountY][];
        for (int i = 0; i < amountY; ++i) {
            array[i] = new BufferedImage[amountX];
        }
        Point pos = new Point(0,0);
        for (
            int i = 0;
            pos.y+height <= image.getHeight();
            pos.y += height+space_y, pos.x = 0, ++i
        ) {
            for ( int j = 0; pos.x+width <= image.getWidth(); pos.x += width+space_x, ++j ) {
                array[i][j] = image.getSubimage( pos.x, pos.y, width, height );
                if ( j == amountX-1 ) { break; }
            }
            if ( i == amountY-1 ) { break; }
        }
        return array;
    }

    /**
     * Erzeugt ein neues BufferedImage mit GameMap.DEFAULT_TILE_SIZE x GameMap.DEFAULT_TILE_SIZE und positioniert
     * das Ursprüngliche in die obere linke Ecke.
     * @param image Bild
     * @return neues Bild
     */
    public static BufferedImage adjustSprite(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        if ( w < GameMap.DEFAULT_TILE_SIZE ) {
            w = GameMap.DEFAULT_TILE_SIZE;
        }
        if ( h < GameMap.DEFAULT_TILE_SIZE ) {
            h = GameMap.DEFAULT_TILE_SIZE;
        }
        BufferedImage newImage = new BufferedImage( w, h, image.getType() );
        Graphics2D g2d = newImage.createGraphics(); // Graphics2D-Objekt holen, um das alte Bild in das neue zu zeichnen
        g2d.drawImage(image, 0, 0, null);           // Bild an Position (0,0) zeichnen
        g2d.dispose();                              // Ressourcen freigeben
        return newImage;
    }

}
