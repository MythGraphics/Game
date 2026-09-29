/*
 *
 */

package game.item;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.texter.HasDialog;

public record ItemEvent(Object source, Item item, ItemActionType actionType, HasDialog dialog) {

    public enum ItemActionType {
        FIND,
        USE,
        REMOVE,
        BUY,
        SELL
    }

}