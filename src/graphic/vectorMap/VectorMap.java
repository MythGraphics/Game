/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.*;
import java.awt.Color;
import java.awt.Dimension;

public abstract class VectorMap extends GameMap {

    public VectorMap(char[][] tileMap) {
        super(tileMap);
    }

    public VectorMap(char[][] tileMap, Dimension visibleSize) {
        this(tileMap, DEFAULT_TILE_SIZE, visibleSize);
    }

    public VectorMap(char[][] tileMap, int tileSize, Dimension visibleSize) {
        super(tileMap, tileSize, visibleSize);
    }

}
