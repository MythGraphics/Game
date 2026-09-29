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
import static game.item.ItemEvent.ItemActionType.FIND;
import static game.item.ItemEvent.ItemActionType.USE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import graphic.ui.InventoryCellRenderer;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;

public abstract class Inventory {

    private final Map<Item, Integer> inventory      = new HashMap<>(); // Item mit Stapelgröße
    private final DefaultListModel<Item> listModel  = new DefaultListModel<>();

    public Inventory() {}

    abstract Player getOwner();

    public boolean hasItem(Item item) {
        return inventory.containsKey(item);
    }

    public void add(Item item) {
        int amount = inventory.getOrDefault(item, 0);
        inventory.put(item, amount+1);
        if (amount == 0) { // Das Item war noch nicht im Inventar
            listModel.addElement(item);
        }
        item.fireEvent(this, FIND);
    }

    /**
     * Item benutzen: Das Item wird im Inventar um 1 reduziert und dem Spieler hinzugefügt -->
     * heißt: Der Spieler legt es an und der ItemEffekt wird ausgelöst.
     * @param item Das Item.
     * @return Gibt des Erfolg der Aktion zurück.
     */
    public boolean use(Item item) {
        if ( item == null || !( item instanceof UsableItem )) {
            return false;
        }
        UsableItem usable = (UsableItem) item;
        boolean success = remove(usable); // entfernt Item aus dem Inventar
        if (success) {
            usable.use( getOwner() );
            usable.fireEvent(this, USE);
        }
        if (item instanceof ReUsableItem reuse) {
            getOwner().addActiveItem(reuse);
        }
        return success;
    }

    public boolean remove(Item item) {
        int amount = inventory.getOrDefault(item, 0);
        --amount;
        if ( amount < 0 ) {
            return false; // Item nicht im Inventar
        }
        if ( amount == 0 ) {
            inventory.remove(item);
            listModel.removeElement(item);
        } else {
            inventory.put(item, amount); // aktualisiert die Stapelgröße
        }
        return true;
    }

    public DefaultListModel<Item> getListModel() {
        return listModel;
    }

    public InventoryCellRenderer getCellRenderer() {
        return new InventoryCellRenderer(inventory);
    }

}
