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

import game.Player;
import game.UWMapRoutine;

public enum MapType {

    LAND {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.createDefaultLandMap();
        }
    },
    SPACE {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.createDefaultSpaceMap();
        }
    },
    UW {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.createDefaultUWMap( new UWMapRoutine( player ));
        }
    };

    public abstract GameMap createMap(Player player);

}
