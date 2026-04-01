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

import game.UWMapRoutine;

public class MapBuilder {

    private MapBuilder() {}

    // gemeinsame Klasse: GameMap

    public static DefaultLandMap DefaultLandMap() {
        DefaultLandMap map = new DefaultLandMap(DefaultLandMap.DEFAULT_TILE_MAP);
        map.init();
        return map;
    }

    public static DefaultSpaceMap DefaultSpaceMap() {
        DefaultSpaceMap map = new DefaultSpaceMap(DefaultSpaceMap.DEFAULT_TILE_MAP);
        map.init();
        return map;
    }

    public static DefaultUWMap DefaultUWMap(UWMapRoutine uw) {
        DefaultUWMap map = new DefaultUWMap(DefaultUWMap.DEFAULT_TILE_MAP, uw);
        map.init();
        return map;
    }

}
