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

import graphic.texter.DialogOutputListener;

// für das Inventar
public interface ItemActionListener {

    void itemActionPerformed(IsItem item, ItemActionType action, DialogOutputListener dialogListener);

}
