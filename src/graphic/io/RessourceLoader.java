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
import java.util.stream.Collectors;

public class RessourceLoader {

    public final static String ZIP_PATH     = "/resources/";
    public final static String LOCAL_PATH   = "src"+ZIP_PATH;
    public final static String EMPTY_ERROR = "Parameter null oder leer.";
    public final static String FS_ERROR    = "Laden von Dateisystem fehlgeschlagen: ";
    public final static String JAR_ERROR   = "Laden von JAR fehlgeschlagen: ";

    private RessourceLoader() {}

    public static String loadTextFileFromJar(String fileName, Class clazz) throws IOException {
        if ( fileName == null || fileName.isBlank() ) {
            throw new IOException(EMPTY_ERROR);
        }
        try ( InputStream in = clazz.getResourceAsStream( fileName )) {
            if (in == null) {
                throw new IOException("Datei in JAR nicht gefunden: " + fileName);
            }
            // via Stream API auslesen
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

    public static String loadTextFile(String fileName, Class clazz) {
        if ( fileName == null || fileName.isBlank() ) {
            System.err.println(EMPTY_ERROR);
            return null;
        }

        // versuchen, von FS zu laden
        try {
            return loadTextFileFromFS(fileName);
        }
        catch (IOException e) {
            System.err.println( fileName + ": " + FS_ERROR + e.getMessage() );
        }
        try {
            return loadTextFileFromFS(LOCAL_PATH+fileName);
        }
        catch (IOException e) {
            System.err.println( LOCAL_PATH+fileName + ": " + FS_ERROR + e.getMessage() );
        }

        // versuchen, von JAR zu laden
        try {
            return loadTextFileFromJar(fileName, clazz);
        }
        catch (IOException e) {
            System.err.println( fileName + ": " + JAR_ERROR + e.getMessage() );
        }
        try {
            return loadTextFileFromJar(ZIP_PATH+fileName, clazz);
        }
        catch (IOException e) {
            System.err.println( ZIP_PATH+fileName + ": " + JAR_ERROR + e.getMessage() );
        }

        System.err.println("Nichts zum Laden gefunden.");
        return null;
    }

}
