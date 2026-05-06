/*
 *
 */

package graphic.io;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.1
 *
 */

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
     * @param number Anzahl der zu ladenden Sprites oder -1 für alle
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(
        BufferedImage image, Point start, int space_x, int space_y, int tileSize, int number
    ) {
        ArrayList<BufferedImage> list = new ArrayList<>();
        for ( int y = start.y, x = start.x; y+tileSize <= image.getHeight(); y += tileSize+space_y, x = start.x ) {
            for (; x+tileSize <= image.getWidth(); x += tileSize+space_x ) {
                list.add( image.getSubimage( x, y, tileSize, tileSize ));
                if ( list.size() == number ) {
                    return list.toArray(BufferedImage[]::new);
                }
            }
        }
        return list.toArray(BufferedImage[]::new);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * Die Länge des Sprites entspricht der Gesamtlänge des Bildes.
     * @param image Quell-Bild
     * @param width Ausdehnung eines Sprites in x-Achse (Breite) oder 0 für anteilige Breite basierend auf der
     *              Gesamtbreite des Bildes
     * @param number Anzahl der zu ladenden Sprites
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetHorizontal(BufferedImage image, int width, int tileSize, int number) {
        if ( width <= 0 ) {
            width = image.getWidth() / number;
        }
        return getSpriteSet( image, image.getHeight(), width, HORIZONTAL, tileSize, number );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * Die Breite des Sprites entspricht der Gesamtbreite des Bildes.
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge) oder 0 für anteilige Länge basierend auf der
     *               Gesamtlänge des Bildes
     * @param number Anzahl der zu ladenden Sprites
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetVertical(BufferedImage image, int height, int tileSize, int number) {
        if ( height <= 0 ) {
            height = image.getHeight() / number;
        }
        return getSpriteSet( image, height, image.getWidth(), VERTICAL, tileSize, number );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge)
     * @param width Ausdehnung eines Sprites in x-Achse (Breite)
     * @param number Anzahl der zu ladenden Sprites
     * @param alignment Ausrichtung des SpriteSets (horizontal oder vertical)
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(
        BufferedImage image, int height, int width, char alignment, int tileSize, int number
    ) {
        BufferedImage subimg;
        ArrayList<BufferedImage> list = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            subimg = image.getSubimage( 0, 0, width, height );
            switch (alignment) {
                case HORIZONTAL:
                    subimg = image.getSubimage( i*width, 0, width, height );
                    break;
                case VERTICAL:
                    subimg = image.getSubimage( 0, i*height, width, height );
                    break;
            }
            if ( subimg.getHeight() < tileSize || subimg.getWidth() < tileSize ) {
                subimg = adjustSprite(subimg, tileSize);
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
     * @param number Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return AnimationSet
     */
    public static Image[][] getAnimationSet(
        BufferedImage image, Point start, int space_x, int space_y, int tileSize, int number
    ) {
        if ( image.getWidth() < ( number*(tileSize+space_x)+start.x )) {
            number = -1;
        }
        if (number == -1) {
            number = image.getWidth()-start.x/(tileSize+space_x);
        }
        int amountX = number;
        int amountY = image.getHeight()-start.y/(tileSize+space_y);
        BufferedImage[][] array = new BufferedImage[amountY][];
        for (int i = 0; i < amountY; ++i) {
            array[i] = new BufferedImage[amountX];
        }
        Point pos = start;
        for ( int i = 0;
              pos.y+tileSize <= image.getHeight();
              pos.y += tileSize+space_y, pos.x = start.x, ++i
        ) {
            for ( int j = 0; pos.x+tileSize <= image.getWidth(); pos.x += tileSize+space_x, ++j ) {
                array[i][j] = image.getSubimage( pos.x, pos.y, tileSize, tileSize );
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
     * @param number Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static Image[][] getAnimationSet(
        BufferedImage image, int space_x, int space_y, int width, int height, int number
    ) {
        if ( image.getWidth() < number*(width+space_x)) {
            number = -1;
        }
        if (number == -1) {
            number = image.getWidth()/(width+space_x);
        }
        int amountX = number;
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
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @return neues Bild
     */
    public static BufferedImage adjustSprite(BufferedImage image, int tileSize) {
        int w = image.getWidth();
        int h = image.getHeight();
        if ( w < tileSize ) {
            w = tileSize;
        }
        if ( h < tileSize ) {
            h = tileSize;
        }
        BufferedImage newImage = new BufferedImage( w, h, image.getType() );
        Graphics2D g2d = newImage.createGraphics(); // Graphics2D-Objekt holen, um das alte Bild in das neue zu zeichnen
        g2d.drawImage(image, 0, 0, null);           // Bild an Position (0,0) zeichnen
        g2d.dispose();                              // Ressourcen freigeben
        return newImage;
    }

}
