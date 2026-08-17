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

public interface IsMapTile {

    char getMapChar();
    InteractionType getInteractionType();
    boolean isPassable();

}
