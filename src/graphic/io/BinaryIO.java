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

    public final static String JAR          = "MythGraphics_Game.jar";
    public final static String ZIP_PATH     = "/resources/";
    public final static String LOCAL_PATH   = "src/resources/";

    public final String AUDIO               = "audio/";
    public final String IMG                 = "img/";
    public final String SPRITE              = "sprites/";
    public final String TILESET             = "tilesets/";

    public final static BinaryIO BINARYIO   = new BinaryIO(ZIP_PATH, LOCAL_PATH);

    public final String zip_path;
    public final String local_path;

    public BinaryIO(String zip_path, String local_path) {
        this.zip_path   = zip_path;
        this.local_path = local_path;
    }

    // new javax.swing.ImageIcon( getClass().getResource( "/path/icon.png" ));

    public static BufferedImage loadImageFromJar(String filepath, String jarname) throws IOException {
        // jarname ließe sich über graphic.io.PathFinder holen
        String[] jars = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String jar : jars) {
            if ( jar.contains( jarname )) {
                try {
                    return Reader.getImage( new ZipFile(jar), filepath );
                } catch (NullPointerException e) {
                    throw new IOException( filepath + " konnte nicht aus Jar gelesen werden." );
                }
            }
        }
        throw new IOException( jarname + ": Jar nicht auffindbar." );
    }

    public static BufferedImage loadImageFromFS(String imgpath) throws IOException {
        if ( imgpath == null || imgpath.isEmpty() ) {
            return null;
        }
        File imgfile = new File(imgpath);
        if ( !imgfile.exists() ) {
            throw new IOException( imgpath + " existiert nicht." );
        }
        return ImageIO.read(imgfile);
    }

    /**
     * Try to load from jar, if possible, otherwise from filesystem.
     * Method uses pre-specified pathes within jar-file and filesystem.
     * @param imgpath relative imgpath-string
     * @return BufferedImage
     */
    public BufferedImage loadImage(String imgpath) {
        if ( imgpath == null || imgpath.isEmpty() ) {
            return null;
        }
        try {
            return loadImageFromJar(zip_path+imgpath, JAR);
        } catch (IOException e) {
            // ignorieren und versuchen, von Dateisystem zu laden
            /* System.err.println( e.getMessage() ); */
        }
        try {
            return loadImageFromFS(local_path+imgpath);
        } catch (IOException e) {
            System.err.println( e.getMessage() );
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

        // Original in das optimierte Bild zeichnen
        optimizedImage.createGraphics().drawImage(sourceImage, 0, 0, null);

        return optimizedImage;
    }

    public AudioInputStream loadAudioStream(String audioFilePath) throws UnsupportedAudioFileException, IOException {
        // versuche von Dateisystem zu laden
        String path = local_path+AUDIO+audioFilePath;
        File file = new File(path);
        if ( file.exists() ) {
            return AudioSystem.getAudioInputStream( new BufferedInputStream( getClass().getResourceAsStream( audioFilePath )));
        }

        // versuche von Jar zu laden
        path = zip_path+AUDIO+audioFilePath;
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            // Fallback: Falls '/' vergessen wurde
            is = getClass().getResourceAsStream("/"+path);
        }
        if (is == null) {
            throw new IOException("Audio-Quelle nicht gefunden: " + audioFilePath);
        }
        return AudioSystem.getAudioInputStream( new BufferedInputStream( getClass().getResourceAsStream( path )));
/*
        // den ganzen Quatsch cachen
        byte[] audioCache = is.readAllBytes();
        is.close();

        // Erzeuge einen Stream aus dem Cache
        InputStream bais = new ByteArrayInputStream(audioCache);
        return AudioSystem.getAudioInputStream( new BufferedInputStream( bais ));
 */
    }

}
