/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import static graphic.io.BinaryIO.loadImage;
import graphic.io.TextIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import util.EnumHelper;

public class Loader {

    private Loader() {}

    public static Map<Character, BufferedImage> loadMapProperties(String filePath, Class<?> clazz) throws IOException {
        Map<Character, CustomMapTile> blockTypeMap = new HashMap<>();
        blockTypeMap.put( ' ', new CustomMapTile( ' ', null, InteractionType.NONE, true ));
        try (BufferedReader reader = TextIO.getTextReader( filePath, clazz )) {
            while ( reader.ready() ) {
                String[] array = reader.readLine().split(",");
                if (array.length < 4) {
                    continue;
                }
                char c = array[0].charAt(0);
                BufferedImage image = loadImage(array[1]);
                InteractionType type = EnumHelper.getEnumFromString(InteractionType.class, array[2]);
                boolean passable = Boolean.parseBoolean(array[3]);
                blockTypeMap.put( c, new CustomMapTile( c, image, type, passable ));
            }
        }
        // ToDo hier weiter

        Map<Character, BufferedImage> spriteMap = new HashMap<>();
        spriteMap.put(' ', null);
        Properties p = TextIO.loadProperties(filePath, clazz);
        for ( String key : p.stringPropertyNames() ) {
            char c = key.charAt(0);
            BufferedImage image = loadImage( p.getProperty( key ));
            spriteMap.put(c, image);
        }
        return spriteMap;
    }

}
