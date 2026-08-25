/*
 *
 */

package graphic.io;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.2
 *
 */

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TilesetUtility {

    public final static char HORIZONTAL = 'h';
    public final static char VERTICAL   = 'v';

    private TilesetUtility() {}

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @param number Anzahl der zu ladenden Sprites oder -1 für alle
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(BufferedImage image,
                                               int tileSize, int number
    ) {
        return getSpriteSet(image, new Point(0, 0), 0, 0, tileSize, number);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param start Startposition
     * @param spaceX Abstand zwischen Sprites in x-Achse
     * @param spaceY Abstand zwischen Sprites in y-Achse
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @param number Anzahl der zu ladenden Sprites oder -1 für alle
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(BufferedImage image,
                                               Point start, int spaceX, int spaceY,
                                               int tileSize, int number
    ) {
        if (image == null || tileSize <= 0) {
            return new BufferedImage[0];
        }

        Point startPt = (start != null) ? start : new Point(0, 0);
        List<BufferedImage> list = new ArrayList<>();

        int strideX = tileSize + spaceX;
        int strideY = tileSize + spaceY;

        if (strideX <= 0 || strideY <= 0) {
            return new BufferedImage[0];
        }

        for ( int y = startPt.y; y + tileSize <= image.getHeight(); y += strideY ) {
            for ( int x = startPt.x; x + tileSize <= image.getWidth(); x += strideX ) {
                list.add( image.getSubimage( x, y, tileSize, tileSize ));
                if (number > 0 && list.size() == number) {
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
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel) oder 0 für Originalgröße
     * @param number Anzahl der zu ladenden Sprites
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetHorizontal(BufferedImage image, int width, int tileSize, int number) {
        if (image == null) {
            return new BufferedImage[0];
        }
        if (width <= 0 && number > 0) {
            width = image.getWidth() / number;
        }
        else if (number <= 0 && width > 0) {
            number = image.getWidth() / width;
        }
        if (width <= 0 || number <= 0) {
            return new BufferedImage[0];
        }
        return getSpriteSet( image, width, image.getHeight(), HORIZONTAL, tileSize, number );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * Die Breite des Sprites entspricht der Gesamtbreite des Bildes.
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge) oder 0 für anteilige Höhe basierend auf der
     *               Gesamthöhe des Bildes
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel) oder 0 für Originalgröße
     * @param number Anzahl der zu ladenden Sprites
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSetVertical(BufferedImage image, int height, int tileSize, int number) {
        if (image == null) {
            return new BufferedImage[0];
        }
        if (height <= 0 && number > 0) {
            height = image.getHeight() / number;
        }
        else if (number <= 0 && height > 0) {
            number = image.getHeight() / height;
        }
        if (height <= 0 || number <= 0) {
            return new BufferedImage[0];
        }
        return getSpriteSet( image, image.getWidth(), height, VERTICAL, tileSize, number );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein BufferedImage-Array (SpriteSet).
     * @param image Quell-Bild
     * @param height Ausdehnung eines Sprites in y-Achse (Länge)
     * @param width Ausdehnung eines Sprites in x-Achse (Breite)
     * @param alignment Ausrichtung des SpriteSets (horizontal oder vertical)
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @param number Anzahl der zu ladenden Sprites
     * @return SpriteSet
     */
    public static BufferedImage[] getSpriteSet(
        BufferedImage image, int width, int height, char alignment, int tileSize, int number
    ) {
        if (image == null || width <= 0 || height <= 0 || number <= 0) {
            return new BufferedImage[0];
        }

        List<BufferedImage> list = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            int x = (alignment == HORIZONTAL) ? i * width  : 0;
            int y = (alignment == VERTICAL)   ? i * height : 0;

            // Grenzen vor dem Herausschneiden prüfen
            if ( x+width > image.getWidth() || y+height > image.getHeight() ) {
                break;
            }

            BufferedImage subimg = image.getSubimage(x, y, width, height);

            // Skalieren, falls tileSize > 0 und das Bild nicht der tileSize entspricht
            if ( tileSize > 0 && ( subimg.getWidth() != tileSize || subimg.getHeight() != tileSize )) {
                subimg = scaleImage(subimg, tileSize);
            }

            list.add(subimg);
        }

        return list.toArray(BufferedImage[]::new);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein 2D-BufferedImage-Array (AnimationSet).
     * @param image Quell-Bild
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @param number Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static BufferedImage[][] getAnimationSet(BufferedImage image,
                                                    int tileSize, int number
    ) {
        return getAnimationSet( image, new Point(0, 0), 0, 0, tileSize, tileSize, number );
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein 2D-BufferedImage-Array (AnimationSet).
     * @param image Quell-Bild
     * @param start Startposition
     * @param spaceX Abstand zwischen Sprites in x-Achse
     * @param spaceY Abstand zwischen Sprites in y-Achse
     * @param tileSize Größe des Sprite-Blocks (tileSize x tileSize Pixel)
     * @param number Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static BufferedImage[][] getAnimationSet(BufferedImage image,
                                                    Point start, int spaceX, int spaceY,
                                                    int tileSize, int number
    ) {
        return getAnimationSet(image, start, spaceX, spaceY, tileSize, tileSize, number);
    }

    /**
     * Lädt die gegebene Anzahl an Unterbildern in ein 2D-BufferedImage-Array (AnimationSet).
     * Lädt zeilenweise von links nach rechts und von oben nach unten.
     * @param image Quell-Bild
     * @param start Startposition
     * @param spaceX Abstand zwischen Sprites in x-Achse
     * @param spaceY Abstand zwischen Sprites in y-Achse
     * @param width Länge eines Sprites
     * @param height Höhe eines Sprites
     * @param number Anzahl der zu ladenden Sprites pro Animation (Zeile) oder -1 für alle
     * @return AnimationSet
     */
    public static BufferedImage[][] getAnimationSet(BufferedImage image, Point start,
                                                    int spaceX, int spaceY,
                                                    int width, int height, int number
    ) {
        if (image == null) {
            return new BufferedImage[0][0];
        }

        int startX = (start != null) ? start.x : 0;
        int startY = (start != null) ? start.y : 0;

        int strideX = width  + spaceX;
        int strideY = height + spaceY;

        if (strideX <= 0 || strideY <= 0) {
            return new BufferedImage[0][0];
        }

        // Maximal mögliche Spalten und Zeilen im Sprite-Sheet berechnen
        int maxCols = Math.max( 0, ( image.getWidth()  - startX + spaceX ) / strideX );
        int maxRows = Math.max( 0, ( image.getHeight() - startY + spaceY ) / strideY );

        int amountX = (number > 0 && number <= maxCols) ? number : maxCols;
        int amountY = maxRows;

        if (amountX <= 0 || amountY <= 0) {
            return new BufferedImage[0][0];
        }

        BufferedImage[][] array = new BufferedImage[amountY][amountX];

        for (int i = 0; i < amountY; i++) {
            int y = startY + i*strideY;
            for (int j = 0; j < amountX; j++) {
                int x = startX + j*strideX;
                array[i][j] = image.getSubimage(x, y, width, height);
            }
        }

        return array;
    }

    public static BufferedImage[][] scaleDirectionalAnimationSet(BufferedImage[][] aniset, int tileSize) {
        if (aniset == null) {
            return new BufferedImage[0][0];
        }
        BufferedImage[][] scaledArray = new BufferedImage[aniset.length][];
        for (int i = 0; i < aniset.length; i++) {
            scaledArray[i] = scaleImageSet(aniset[i], tileSize);
        }
        return scaledArray;
    }

    public static BufferedImage[] scaleImageSet(BufferedImage[] imageset, int tileSize) {
        if (imageset == null) {
            return new BufferedImage[0];
        }
        BufferedImage[] scaledArray = new BufferedImage[imageset.length];
        for (int i = 0; i < imageset.length; i++) {
            scaledArray[i] = scaleImage(imageset[i], tileSize);
        }
        return scaledArray;
    }

    public static BufferedImage scaleImage(BufferedImage image, int tileSize) {
        if (image == null) {
            return null;
        }

        // Falls das Bild schon exakt die Zielgröße hat, direkt zurückgeben
        if ( image.getWidth() == tileSize && image.getHeight() == tileSize ) {
            return image;
        }

        int imgType = image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : image.getType();

        BufferedImage newImage = new BufferedImage(tileSize, tileSize, imgType);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, tileSize, tileSize, null); // Bild an Position 0,0 zeichnen
        g2d.dispose();

        return newImage;
    }

}
