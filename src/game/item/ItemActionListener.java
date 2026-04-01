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

public interface ItemActionListener {

    void itemActionPerformed(Item item, ItemAction action, DialogOutputListener dialogListener);

}
