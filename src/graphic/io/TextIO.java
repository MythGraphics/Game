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

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TextIO {

    public final static String ZIP_PATH     = "/";
    public final static String LOCAL_PATH   = "src/";
    public final static String PROPERTY     = "properties/";

    private TextIO() {}

    /**
     * Try to load from jar, if possible, otherwise from filesystem.
     * @param filepath relative filepath-string
     * @param clazz class reference
     * @return BufferedReader
     * @throws IOException IOException, if an IO error occures.
     */
    public static BufferedReader getTextReader(String filepath, Class<?> clazz) throws IOException {
        // versuchen, von JAR zu laden
        InputStream in = null;
        // System.err.println("Versuche von JAR zu laden ...");
        if ( PathFinder.getJarName().contains( ".jar" )) {
            in = clazz.getResourceAsStream(ZIP_PATH+filepath);
            if (in != null) {
                return new BufferedReader( new InputStreamReader( in ));
            }
            // System.err.println( ZIP_PATH+filepath + " in JAR nicht gefunden." );
        }

        // versuchen, von FS zu laden
        // System.err.println("Versuche von Dateisystem zu laden ...");
        File file = new File(LOCAL_PATH+filepath);
        if ( file.exists() && !file.isDirectory() ) {
            in = new FileInputStream(file);
        }
        if (in != null) {
            return new BufferedReader( new InputStreamReader( in ));
        }
        // System.err.println( LOCAL_PATH+filepath + " in Dateisystem nicht gefunden." );
        throw new IOException( filepath + " weder in JAR noch im FS gefunden.");
    }

    public static Properties loadProperties(String filename, Class<?> clazz) throws IOException {
        Properties p = new Properties();
        try (BufferedReader stream = getTextReader( PROPERTY+filename, clazz )) {
            p.load(stream);
        }
        return p;
    }

    public static String loadProlog(Class<?> clazz) {
        return loadTextFile("prolog.txt", clazz);
    }

    public static String loadEpilog(Class<?> clazz) {
        return loadTextFile("epilog.txt", clazz);
    }

    public static String loadTextFile(String filename, Class<?> clazz) {
        StringBuilder sb = new StringBuilder("");
        try (BufferedReader in = getTextReader( PROPERTY+filename, clazz )) {
            String line;
            while (( line = in.readLine() ) != null ) {
                sb.append("\n").append(line); // Zeilenumbruch erhalten
            }
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Text-Datei: " + e.getMessage() );
        }
        return sb.toString();
    }

    public static List<String> loadAudioTrackList(String filepath, Class<?> clazz) {
        // CAVE! Alle zurückgegebenen Listen sind immutabel!
        if ( filepath == null || filepath.isBlank() ) {
            System.err.println("Keine Tracklist-Datei übergeben.");
            return Collections.emptyList();
        }
        try (BufferedReader in = getTextReader( PROPERTY+filepath, clazz )) {
            return in.lines().toList();
        }
        catch (FileNotFoundException e) {
            System.err.println( "Tracklist-Datei nicht gefunden: " + e.getMessage() );
        }
        catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Tracklist-Datei: " + e.getMessage() );
        }
        return Collections.emptyList();
    }

}
