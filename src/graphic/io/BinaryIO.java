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

    public final static String LOCAL_PATH   = "src/";

    public final static String RESOURCE     = "resources/";
    public final static String AUDIO        = RESOURCE+"audio/";
    public final static String IMG          = RESOURCE+"img/";
    public final static String SPRITE       = RESOURCE+"sprites/";
    public final static String TILESET      = RESOURCE+"tilesets/";

    public static String JAR                = "MythGraphics_Game.jar";

    private BinaryIO() {}

    // new javax.swing.ImageIcon( getClass().getResource( "/path/icon.png" ));

    public static String removeLeadingSlash(String filePath) {
        if ( filePath.startsWith( "/" )) {
            return filePath.substring(1);
        } else {
            return filePath;
        }
    }

    public static BufferedImage loadImageFromJar(String imgpath, String jarname) throws IOException {
        // jarname ließe sich über graphic.io.PathFinder holen
        System.out.println("loadImageFromJAR: " + imgpath); // debug
        String[] jars = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String jar : jars) {
            System.out.println("JAR: " + jar); // debug
            System.out.println("ImgPath: " + imgpath); // debug
            if ( jar.contains( jarname )) {
                try {
                    return Reader.getImage( new ZipFile( jar ), imgpath );
                } catch (NullPointerException e) {
                    throw new IOException(imgpath + " konnte nicht aus JAR gelesen werden.");
                }
            }
        }
        throw new IOException( "JAR nicht auffindbar: " + jarname );
    }

    public static BufferedImage loadImageFromFS(String imgpath) throws IOException {
        System.out.println("loadImageFromFS: " + imgpath); // debug
        if ( imgpath == null || imgpath.isBlank() ) {
            return null;
        }
        File imgfile = new File(imgpath);
        if ( !imgfile.exists() ) {
            throw new IOException(imgpath + " existiert nicht.");
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
     * @param imgPath relative img path
     * @param jar jar name
     * @return BufferedImage
     */
    public static BufferedImage loadImage(String imgPath, String jar) {
        if ( imgPath == null || imgPath.isBlank() ) {
            return null;
        }
        try {
            return loadImageFromFS(LOCAL_PATH+imgPath);
        }
        catch (IOException ignore) {
            // ignorieren und versuchen, von JAR zu laden
            /* System.err.println( "Laden des Bildes von Dateisystem fehlgeschlagen: " + e.getMessage() ); */
        }
        try {
            return loadImageFromJar(imgPath, jar);
        }
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
        path = AUDIO+audioFilePath;
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
