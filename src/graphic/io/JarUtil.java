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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JarUtil {

    private JarUtil() {}

    // Code by Google Gemini

    public static List<String> listFiles(String folder) throws IOException, URISyntaxException {
        // Pfad für ClassLoader normalisieren (kein führender Slash)
        String pathInJar = folder.startsWith("/") ? folder.substring(1) : folder;
        URI uri = Thread.currentThread().getContextClassLoader().getResource(pathInJar).toURI();
        // URI uri = JarUtil.class.getResource(pathInJar).toURI();

        if ( uri.getScheme().equals( "jar" )) {
            // Sicherstellen, dass das FileSystem existiert oder erstellt wird
            try ( FileSystem fs = getFileSystem( uri )) {
                return readFromPath( fs.getPath( "/" + pathInJar ));
            }
        } else {
            return readFromPath( Paths.get( uri ));
        }
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            // Versuche ein neues zu erstellen
            return FileSystems.newFileSystem( uri, Collections.emptyMap() );
        } catch (FileSystemAlreadyExistsException e) {
            // Falls bereits offen, hole das bestehende
            return FileSystems.getFileSystem(uri);
        }
    }

    private static List<String> readFromPath(Path path) throws IOException {
        // durchsucht das aktuelle Verzeichnis; Zahl weglassen, um rekursiv alles zu durchsuchen
        try ( Stream<Path> walk = Files.walk( path, 1 )) {
            return walk.filter(Files::isRegularFile)
                       .map( p -> p.getFileName().toString() )
                       .collect( Collectors.toList() );
        }
    }

}
