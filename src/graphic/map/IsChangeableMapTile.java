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

public interface IsChangeableMapTile extends IsMapTile {

    void setPassable(boolean passable);
    void setInteractionType(InteractionType iType);
    IsChangeableMapTile getAliveTile(IsChangeableMapTile tile);
    IsChangeableMapTile getDeadTile(IsChangeableMapTile tile);

}
