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

import io.Reader;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BinaryIO {

    public final static String ZIP_PATH     = "/resources/";
    public final static String LOCAL_PATH   = "src"+ZIP_PATH;
    public final static String AUDIO        = "audio/";
    public final static String IMG          = "img/";
    public final static String SPRITE       = "sprites/";
    public final static String TILESET      = "tilesets/";

    public static String JAR                = "MythGraphics_Game.jar";

    private BinaryIO() {}

    // new javax.swing.ImageIcon( getClass().getResource( "/path/icon.png" ));

    public static BufferedImage loadImageFromJar(String filepath, String jarname) throws IOException {
        // jarname ließe sich über graphic.io.PathFinder holen
        String[] jars = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String jar : jars) {
            if ( jar.contains( jarname )) {
                try {
                    return Reader.getImage( new ZipFile(jar), filepath );
                } catch (NullPointerException e) {
                    throw new IOException( filepath + " konnte nicht aus JAR gelesen werden" );
                }
            }
        }
        throw new IOException( jarname + ": nicht auffindbar" );
    }

    public static BufferedImage loadImageFromFS(String imgpath) throws IOException {
        if ( imgpath == null || imgpath.isBlank() ) {
            return null;
        }
        File imgfile = new File(imgpath);
        if ( !imgfile.exists() ) {
            throw new IOException( imgpath + " existiert nicht" );
        }
        return ImageIO.read(imgfile);
    }

    /**
     * Try to load image from filesystem first, then from jar.
     * Uses local static string JAR.
     * @param imgpath relative img path
     * @return BufferedImage
     */
    public static BufferedImage loadImage(String imgpath) {
        return loadImage(imgpath, JAR);
    }

    /**
     * Try to load image from filesystem first, then from jar.
     * @param imgpath relative img path
     * @param jar jar name
     * @return BufferedImage
     */
    public static BufferedImage loadImage(String imgpath, String jar) {
        if ( imgpath == null || imgpath.isBlank() ) { return null; }
        try { return loadImageFromFS(LOCAL_PATH+imgpath); }
        catch (IOException e) {
            // ignorieren und versuchen, von JAR zu laden
            /* System.err.println( "Laden des Bildes von Dateisystem fehlgeschlagen: " + e.getMessage() ); */
        }
        try { return loadImageFromJar(ZIP_PATH+imgpath, jar); }
        catch (IOException e) {
            System.err.println( "Laden des Bildes aus JAR fehlgeschlagen: " + e.getMessage() );
            return null;
        }
    }

    public static BufferedImage loadOptimizedImage(String filepath) throws Exception {
        BufferedImage sourceImage = ImageIO.read( new File( filepath ));
        GraphicsConfiguration config = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

        // kompatibles Bild im VRAM-freundlichen Format erstellen und prüfen, ob das Original Transparenz hat
        int transparency = sourceImage.getColorModel().hasAlpha()
                         ? Transparency.TRANSLUCENT
                         : Transparency.OPAQUE;

        BufferedImage optimizedImage = config.createCompatibleImage(
            sourceImage.getWidth(),
            sourceImage.getHeight(),
            transparency
        );
        optimizedImage.createGraphics().drawImage(sourceImage, 0, 0, null); // Original in das optimierte Bild zeichnen

        return optimizedImage;
    }

    public static AudioInputStream loadAudioStream(String audioFilePath, Class clazz)
    throws UnsupportedAudioFileException, IOException {
        // versuchen, von FS zu laden
        String path = LOCAL_PATH+AUDIO+audioFilePath;
        File file = new File(path);
        if ( file.exists() ) {
            return AudioSystem.getAudioInputStream(
                new BufferedInputStream(
                    clazz.getClass().getResourceAsStream(audioFilePath)
                )
            );
        }

        // versuchen, von JAR zu laden
        path = ZIP_PATH+AUDIO+audioFilePath;
        InputStream is = clazz.getResourceAsStream(path);
        if (is == null) {
            // Fallback: Falls '/' fehlt
            is = clazz.getResourceAsStream("/"+path);
        }
        if (is == null) {
            throw new IOException("Audio-Quelle nicht gefunden: " + audioFilePath);
        }
        return AudioSystem.getAudioInputStream( new BufferedInputStream( clazz.getClass().getResourceAsStream( path )));
/*
        // den ganzen Quatsch cachen
        byte[] audioCache = is.readAllBytes();
        is.close();

        // Stream aus dem Cache erzeugen
        InputStream bais = new ByteArrayInputStream(audioCache);
        return AudioSystem.getAudioInputStream( new BufferedInputStream( bais ));
 */
    }

}
