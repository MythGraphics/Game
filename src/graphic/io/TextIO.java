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
        if ( PathFinder.getJarName().contains( ".jar" )) {
            in = clazz.getResourceAsStream(ZIP_PATH+filepath);
            if (in != null) {
                return new BufferedReader( new InputStreamReader( in ));
            }
            // System.err.println( zip_path+filepath + " in Jar nicht gefunden." );
        }

        // versuchen, von FS zu laden
        File file = new File(LOCAL_PATH+filepath);
        if ( file.exists() && !file.isDirectory() ) {
            in = new FileInputStream(file);
        }
        if (in != null) {
            return new BufferedReader( new InputStreamReader( in ));
        }
        // System.err.println( local_path+filepath + " im Dateisystem nicht gefunden." );
        throw new IOException( filepath + " weder in JAR noch im FS gefunden.");
    }

    public static String loadProlog(Class<?> clazz) {
        StringBuilder sb = new StringBuilder("");
        try (BufferedReader in = getTextReader( PROPERTY+"prolog.txt", clazz )) {
            String line;
            while (( line = in.readLine() ) != null ) {
                sb.append("\n").append(line); // Zeilenumbruch erhalten
            }
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Prolog-Datei: " + e.getMessage() );
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
