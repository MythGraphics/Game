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
    public final static TextIO TEXTIO       = new TextIO(ZIP_PATH, LOCAL_PATH);

    private final String zip_path;
    private final String local_path;

    public TextIO(String zip_path, String local_path) {
        this.zip_path   = zip_path+PROPERTY;
        this.local_path = local_path+PROPERTY;
    }

    /**
     * Try to load from jar, if possible, otherwise from filesystem.
     * Method uses pre-specified pathes within jar-file and filesystem.
     * @param filepath relative filepath-string
     * @return BufferedReader
     * @throws IOException IOException, if an IO error occures.
     */
    public BufferedReader getTextReader(String filepath) throws IOException {
        // Versuchen aus Jar zu laden
        InputStream in = null;
        if ( PathFinder.getJarName().contains( ".jar" )) {
            in = getClass().getResourceAsStream(zip_path+filepath);
            if (in != null) {
                return new BufferedReader( new InputStreamReader( in ));
            }
            // System.err.println( zip_path+filepath + " in Jar nicht gefunden." );
        }

        // Versuchen aus Dateisystem zu laden
        File file = new File(local_path+filepath);
        if ( file.exists() && !file.isDirectory() ) {
            in = new FileInputStream(file);
        }
        if (in != null) {
            return new BufferedReader( new InputStreamReader( in ));
        }
        // System.err.println( local_path+filepath + " im Dateisystem nicht gefunden." );
        throw new IOException( filepath + " weder in Jar noch im Dateisystem gefunden.");
    }

    public String loadProlog() {
        StringBuilder sb = new StringBuilder("");
        try (BufferedReader in = getTextReader("prolog.txt")) {
            String line;
            while (( line = in.readLine() ) != null ) {
                sb.append("\n").append(line); // Zeilenumbruch erhalten
            }
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Prolog-Datei: " + e.getMessage() );
        }
        return sb.toString();
    }

    public List<String> loadAudioTrackList(String filestr) {
        try (BufferedReader in = getTextReader( filestr )) {
            return in.lines().toList();
        } catch (IOException e) {
            System.err.println( "Fehler beim Lesen der Tracklist-Datei: " + e.getMessage() );
            return Collections.emptyList();
        }
    }

}
