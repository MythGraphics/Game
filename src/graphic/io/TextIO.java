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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class TextIO {

    public final static String ZIP_PATH     = "/";
    public final static String LOCAL_PATH   = "src/";

    public final static String PROPERTY     = "properties/";

    public final static String EMPTY_ERROR  = "Parameter null oder leer.";
    public final static String FS_ERROR     = "Laden von Dateisystem fehlgeschlagen: ";
    public final static String JAR_ERROR    = "Laden von JAR fehlgeschlagen: ";

    private TextIO() {}

    /**
     * Try to load from filesystem, if possible, otherwise from jar.
     * @param filePath relative filePath-string
     * @param clazz class reference
     * @return BufferedReader
     * @throws IOException IOException, if an IO error occures.
     */
    public static BufferedReader getTextReader(String filePath, Class<?> clazz) throws IOException {
        InputStream in = null;

        // versuchen, von FS zu laden
        // System.err.println("Versuche von Dateisystem zu laden ...");
        File file = new File(filePath);
        if ( file.exists() && !file.isDirectory() ) {
            in = new FileInputStream(file);
        } else {
            // System.err.println(FS_ERROR + filePath + " im Dateisystem nicht gefunden."); // debug
            file = new File(LOCAL_PATH+filePath);
        }
        if ( file.exists() && !file.isDirectory() ) {
            in = new FileInputStream(file);
        } else {
            // System.err.println(FS_ERROR + LOCAL_PATH+filePath + " im Dateisystem nicht gefunden."); // debug
        }
        if (in != null) {
            return new BufferedReader( new InputStreamReader( in ));
        }

        // versuchen, von JAR zu laden
        // System.err.println("Versuche von JAR zu laden ...");
        if ( PathFinder.getJarName().contains( ".jar" )) {
            in = clazz.getResourceAsStream(filePath);
            if (in != null) {
                return new BufferedReader( new InputStreamReader( in ));
            }
            // System.err.println(JAR_ERROR + filePath + " in JAR nicht gefunden." );

            in = clazz.getResourceAsStream(ZIP_PATH+filePath);
            if (in != null) {
                return new BufferedReader( new InputStreamReader( in ));
            }
            // System.err.println(JAR_ERROR + ZIP_PATH+filePath + " in JAR nicht gefunden." );
        }

        throw new IOException(filePath + " weder in JAR noch im FS gefunden.");
    }

    public static Properties loadProperties(String fileName, Class<?> clazz) throws IOException {
        Properties p = new Properties();
        try ( BufferedReader stream = getTextReader( PROPERTY+fileName, clazz )) {
            p.load(stream);
        }
        return p;
    }

    public static String loadTextFileFromJar(String fileName, Class clazz) throws IOException {
        if ( fileName == null || fileName.isBlank() ) {
            throw new IOException(EMPTY_ERROR);
        }
        try ( InputStream in = clazz.getResourceAsStream( fileName )) {
            if (in == null) {
                throw new IOException(JAR_ERROR + fileName);
            }
            try ( BufferedReader reader = new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ))) {
                return reader.lines().collect( Collectors.joining( "\n" ));
            }
        }
    }

    public static String loadTextFileFromFS(String fileName) throws IOException {
        if ( fileName == null || fileName.isBlank() ) {
            throw new IOException(EMPTY_ERROR);
        }
        try ( BufferedReader reader = new BufferedReader( new FileReader( new File( fileName ), StandardCharsets.UTF_8 ))) {
            return reader.lines().collect( Collectors.joining( "\n" ));
        }
    }

    public static String loadTextFile(String fileName, Class<?> clazz) {
        if ( fileName == null || fileName.isBlank() ) {
            System.err.println(EMPTY_ERROR);
            return "";
        }
        try ( BufferedReader in = getTextReader( PROPERTY+fileName, clazz )) {
            return in.lines().collect( Collectors.joining( "\n" ));
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Text-Datei: " + e.getMessage() );
            return "";
        }
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

    public static String loadProlog(Class<?> clazz) {
        return loadTextFile("prolog.txt", clazz);
    }

    public static String loadEpilog(Class<?> clazz) {
        return loadTextFile("epilog.txt", clazz);
    }

}
