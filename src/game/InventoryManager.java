/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.item.IsItem;
import game.item.Item;
import game.item.ItemAction;

public class InventoryManager extends Inventory {

    private final Player player;

    public InventoryManager(Player player) {
        this.player = player;
    }

    @Override
    void fireEvent(IsItem item, ItemAction action) {
        item.itemActionPerformed( item, action, player.getDialogOutputListener() );
    }

    @Override
    public Player getOwner() {
        return player;
    }

    public boolean buy(Trader buyer, Trader seller, Item item) {
        boolean b = trade(buyer, seller, item);
        if (b) {
            fireEvent(item, ItemAction.BUY);
        }
        return b;
    }

    /**
     * Verkauft 1 Item des Stapels.
     * @param seller Der Verkäufter
     * @param buyer Der Käufer
     * @param item Das Item
     * @return TRUE, wenn erfolgreich, FALSE, wenn nicht.
     */
    public boolean sell(Trader seller, Trader buyer, Item item) {
        boolean b = trade(buyer, seller, item);
        if (b) {
            fireEvent(item, ItemAction.SELL);
        }
        return b;
    }

    private static boolean trade(Trader buyer, Trader seller, Item item) {
        if ( item.getPrice() > buyer.getCredits() ) {
            return false; // nicht genug Credits
        }
        boolean b = seller.getInventory().hasItem(item);
        if (!b) {
            return false; // Item nicht vorhanden
        }
        b = seller.getInventory().remove(item);
        if (!b) {
            return false; // Verkäufer rückt Item nicht heraus
        }
        seller.addCredits( item.getPrice() );
        buyer.getInventory().add(item);
        buyer.addCredits( -item.getPrice() );
        return true;
    }

}
