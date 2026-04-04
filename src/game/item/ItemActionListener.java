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

import game.DialogOutputListener;

// für das Inventar
public interface ItemActionListener {

    void itemActionPerformed(IsItem item, ItemAction action, DialogOutputListener dialogListener);

}
