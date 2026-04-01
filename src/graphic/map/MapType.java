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
import java.util.Optional;

public enum MapType {

    LAND {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.DefaultLandMap();
        }
    },
    SPACE {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.DefaultSpaceMap();
        }
    },
    UW {
        @Override
        public GameMap createMap(Player player) {
            return MapBuilder.DefaultUWMap(new UWMapRoutine(player));
        }
    };

    public abstract GameMap createMap(Player player);

    public static Optional<MapType> getByName(String s) {
        if ( s == null || s.isBlank() ) {
            return Optional.empty();
        }
        try {
            return Optional.of( MapType.valueOf( s.toUpperCase().trim() ));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
