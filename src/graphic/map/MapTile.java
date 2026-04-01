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

import java.util.ArrayList;

public record MapTile(String imagePath, BlockType type) {

    final static ArrayList<MapTile> LIST = new ArrayList<>();

    public static void add(MapTile tile) {
        LIST.add(tile);
    }

    public static void add(String imagePath, BlockType type) {
        add( new MapTile( imagePath, type ));
    }

    public static String getImagePath(BlockType type) {
        for ( MapTile tile : LIST ) {
            if ( tile.type() == type ) {
                return tile.imagePath();
            }
        }
        return null;
    }

}
