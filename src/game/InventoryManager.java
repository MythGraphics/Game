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

import game.item.Item;
import static game.item.ItemEvent.ItemActionType.BUY;
import static game.item.ItemEvent.ItemActionType.SELL;

public class InventoryManager extends Inventory {

    private final Player player;

    public InventoryManager(Player player) {
        this.player = player;
    }

    @Override
    public Player getOwner() {
        return player;
    }

    public boolean buy(Trader buyer, Trader seller, Item item) {
        boolean success = trade(buyer, seller, item);
        if (success) {
            item.fireEvent(this, BUY);
        }
        return success;
    }

    /**
     * Verkauft 1 Item des Stapels.
     * @param seller Der Verkäufter
     * @param buyer Der Käufer
     * @param item Das Item
     * @return TRUE, wenn erfolgreich, FALSE, wenn nicht.
     */
    public boolean sell(Trader seller, Trader buyer, Item item) {
        boolean success = trade(buyer, seller, item);
        if (success) {
            item.fireEvent(this, SELL);
        }
        return success;
    }

    private static boolean trade(Trader buyer, Trader seller, Item item) {
        if ( item.getPrice() > buyer.getCredits() ) {
            return false; // nicht genug Credits
        }
        boolean success = seller.getInventory().hasItem(item);
        if (!success) {
            return false; // Item nicht vorhanden
        }
        success = seller.getInventory().remove(item);
        if (!success) {
            return false; // Verkäufer rückt Item nicht heraus
        }
        seller.addCredits( item.getPrice() );
        buyer.getInventory().add(item);
        buyer.addCredits( -item.getPrice() );
        return true;
    }

}
