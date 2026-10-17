/*
 *
 */

package graphic.io;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.4
 *
 */

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtility {

    private ImageUtility() {}

    public static String convertToHtmlBase64(BufferedImage img) throws IOException {
        // das fertige Präfix für das HTML-src-Attribut mitschicken
        return "data:image/png;base64," + convertToBase64String(img);
    }

    public static String convertToBase64String(BufferedImage img) throws IOException {
        byte[] imageBytes = convertImageToPngBytes(img);
        return Base64.getEncoder().encodeToString(imageBytes); // Bytes in einen Base64-String codieren
    }

    public static byte[] convertImageToPngBytes(BufferedImage img) throws IOException {
        if (img == null) {
            return new byte[0];
        }
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream() ) {
            // BufferedImage in komprimierte PNG-Bytes wandeln
            ImageIO.write(img, "png", out);
            return out.toByteArray();
        }
    }

    public static BufferedImage convertBytesToImage(byte[] imageBytes) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        try ( ByteArrayInputStream in = new ByteArrayInputStream( imageBytes )) {
            return ImageIO.read(in);
        }
    }

    public static Image scaleImage(Image image, int targetWidth, int targetHeight) {
        if (image == null) {
            return null;
        }
        BufferedImage bimg = toBufferedImage(image);
        return scale(bimg, targetWidth, targetHeight, false);
    }

    /**
     * Skaliert das Bild, behält das Seitenverhältnis bei.
     * @param image Bild
     * @param size  Zielgröße in Pixel
     * @return      skaliertes Bild
     */
    public static BufferedImage scale(BufferedImage image, int size) {
        return scale(image, size, size, false);
    }

    /**
     * Skaliert das Bild, behält das Seitenverhältnis bei.
     * @param image        Bild
     * @param targetWidth  Zielbreite in Pixel
     * @param targetHeight Zielhöhe in Pixel
     * @param highQuality  skaliert weich in hoher Qualität (bikubische Interpolation, Antialiasing)
     * @return             neues, skaliertes Bild
     */
    public static BufferedImage scale(BufferedImage image, int targetWidth, int targetHeight, boolean highQuality) {
        if (image == null) {
            return null;
        }

        // Das kleinere Verhältnis verwenden, um das Seitenverhältnis beizubehalten
        double ratio  = Math.min( (double) targetWidth / image.getWidth(), (double) targetHeight / image.getHeight() );

        // Neue Breite und Höhe berechnen
        int newWidth  = (int) ( image.getWidth()  * ratio );
        int newHeight = (int) ( image.getHeight() * ratio );

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        if (highQuality) {
            // qualitativ hochwertige, weiche Interpolation
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }

        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return scaled;
    }

    /**
     * Erzeugt eine Bild von mindestens tileSize x tileSize und platziert das Original oben links.
     * Es wird nicht skaliert.
     * @param image    Bild
     * @param tileSize Größe des Sprites (tileSize x tileSize Pixel)
     * @return         neues Bild
     */
    public static BufferedImage adjust(BufferedImage image, int tileSize) {
        if (image == null) {
            return null;
        }

        int width  = Math.max( image.getWidth(),  tileSize );
        int height = Math.max( image.getHeight(), tileSize );

        int type = ( image.getType() == BufferedImage.TYPE_CUSTOM ) ? BufferedImage.TYPE_INT_ARGB : image.getType();

        BufferedImage newImage = new BufferedImage(width, height, type);
        Graphics2D g2d = newImage.createGraphics();      // Graphics2D-Objekt holen, um das alte Bild in das Neue zu zeichnen
        g2d.drawImage(image, 0, 0, width, height, null); // Bild an Position (0,0) zeichnen
        g2d.dispose();

        return newImage;
    }

    /**
     * Skaliert das Bild auf ein Quadrat der angegebenen Seitelänge.
     * Bricht das Seitenverhältnis.
     * @param image       Bild
     * @param size        Seitenlänge des Quadrates
     * @param highQuality modifiziert weich in hoher Qualität (bikubische Interpolation, Antialiasing)
     * @return            neues, angepasstes Bild
     */
    public static BufferedImage stretch2Square(BufferedImage image, int size, boolean highQuality) {
        return stretch(image, size, size, highQuality);
    }

    /**
     * Skaliert das Bild auf die angegebene Breite und Höhe.
     * Bricht das Seitenverhältnis.
     * @param image       Bild
     * @param width       ZielBreite
     * @param height      ZielHöhe
     * @param highQuality modifiziert weich in hoher Qualität (bikubische Interpolation, Antialiasing)
     * @return            neues, angepasstes Bild
     */
    public static BufferedImage stretch(BufferedImage image, int width, int height, boolean highQuality) {
        if (image == null) {
            return null;
        }

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        if (highQuality) {
            // qualitativ hochwertige, weiche Interpolation
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }

        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return scaled;
    }

    /**
     * Spiegelt ein BufferedImage horizontal oder vertikal.
     * @param inputImage Das Originalbild
     * @param horizontal TRUE für horizontal spiegeln, FALSE für vertikal
     * @return           neues, gespiegeltes BufferedImage
     */
    public static BufferedImage flip(BufferedImage inputImage, boolean horizontal) {
        if (inputImage == null) {
            return null;
        }

        int width  = inputImage.getWidth();
        int height = inputImage.getHeight();
        int type   = inputImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : inputImage.getType();

        // Erstelle ein neues Bild mit denselben Dimensionen und Typ
        BufferedImage flippedImage = new BufferedImage(width, height, type);
        Graphics2D g2d = flippedImage.createGraphics();
        AffineTransform transformer = new AffineTransform();

        if (horizontal) {
            // Verschiebe das Bild nach rechts und skaliere x mit -1
            transformer.translate(width, 0);
            transformer.scale(-1, 1);
        } else {
            // Verschiebe das Bild nach unten und skaliere y mit -1
            transformer.translate(0, height);
            transformer.scale(1, -1);
        }

        // Zeichne das Originalbild mit der Transformation in das neue Bild
        g2d.drawImage(inputImage, transformer, null);
        g2d.dispose();

        return flippedImage;
    }

    public static BufferedImage rotateRight(BufferedImage source) {
        int width  = source.getWidth();
        int height = source.getHeight();
        BufferedImage rotated = new BufferedImage( height, width, source.getType() ); // Breite und Höhe für das Zielbild tauschen

        Graphics2D g2d = rotated.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI/2);
        transform.translate(height, 0);
        g2d.drawImage(source, transform, null);
        g2d.dispose();

        return rotated;
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image == null) {
            return null;
        }
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // sicherstellen, dass das Bild vollständig geladen ist. ImageIcon nutzt intern einen MediaTracker und regelt das.
        image = new ImageIcon(image).getImage();

        int width  = image.getWidth(null);
        int height = image.getHeight(null);
        if (width <= 0 || height <= 0) {
            return null;
        }

        // neues BufferedImage erstellen
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Inhalt von Image in das BufferedImage zeichnen
        Graphics2D g2d = bimg.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return bimg;
    }

}
