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
import game.item.ItemAction;
import static game.item.ItemAction.FIND;
import static game.item.ItemAction.USE;
import game.item.ReUsable;
import game.item.Usable;
import graphic.ui.InventoryCellRenderer;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;

public abstract class Inventory {

    private final Map<IsItem, Integer> inventory = new HashMap<>(); // Item mit Stapelgröße
    private final DefaultListModel<IsItem> listModel = new DefaultListModel<>();

    public Inventory() {}

    abstract Player getOwner();
    abstract void fireEvent(IsItem item, ItemAction action);

    public boolean hasItem(IsItem item) {
        return inventory.containsKey(item);
    }

    public void add(IsItem item) {
        int amount = inventory.getOrDefault(item, 0);
        inventory.put(item, amount+1);
        if (amount == 0) { // Das Item war noch nicht im Inventar
            listModel.addElement(item);
        }
        fireEvent(item, FIND);
    }

    /**
     * Item benutzen: Das Item wird im Inventar um 1 reduziert und dem Spieler hinzugefügt -->
     * heißt: Der Spieler legt es an und der ItemEffekt wird ausgelöst.
     * @param item Das Item.
     * @return Gibt des Erfolg der Aktion zurück.
     */
    public boolean use(IsItem item) {
        if ( item == null || !( item instanceof Usable )) {
            return false;
        }
        Usable usable = (Usable) item;
        boolean b = remove(usable); // entfernt Item aus dem Inventar
        if (b) {
            usable.use( getOwner() );
            fireEvent(usable, USE);
        }
        if (item instanceof ReUsable reuse) {
            getOwner().addActiveItem(reuse);
        }
        return b;
    }

    public boolean remove(IsItem item) {
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

    public DefaultListModel<IsItem> getListModel() {
        return listModel;
    }

    public InventoryCellRenderer getCellRenderer() {
        return new InventoryCellRenderer(inventory);
    }

}
