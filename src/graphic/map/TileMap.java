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

import java.util.List;

public class TileMap {

    final MapType type;
    final char[][] tileMap;

    public TileMap(String[] tileRows, MapType type) {
        tileMap = new char[tileRows.length][tileRows[0].length()];
        for (int r = 0; r < tileRows.length; ++r) {
            for (int c = 0; c < tileRows[0].length(); ++c) {
                tileMap[r][c] = tileRows[r].charAt(c);
            }
        }
        this.type = type;
    }

    public TileMap(List<String> tileRows, MapType type) {
        this( tileRows.toArray( String[]::new ), type );
    }

    public TileMap(char[][] tileMap, MapType type) {
        this.tileMap = tileMap;
        this.type = type;
    }

    public MapType getType() {
        return type;
    }

    public char getTileChar(int r, int c) {
        return tileMap[r][c];
    }

    public char[][] getTileMap() {
        return tileMap;
    }

}
