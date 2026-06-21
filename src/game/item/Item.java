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

import game.HasDialog;
import game.HasName;
import game.Message;
import game.TextBox;
import game.item.ItemEvent.ItemActionType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Item extends TextBox implements Tradable, HasName {

    protected final LinkedList<Message> msgListOnBuy  = new LinkedList<>();
    protected final LinkedList<Message> msgListOnSell = new LinkedList<>();

    private final List<ItemMessageListener> messageListeners = new ArrayList<>();
    private final List<ItemActionListener>  actionListeners  = new ArrayList<>();

    private int price = 0;
    private String description = "";

    public Item(int id, String name) {
        super(id, name);
    }

    Item(Item item) {
        super(item);
        this.setPrice( item.getPrice() );
        this.setDescription( item.getDescription() );
        for ( Message m : item.getDialog( ItemActionType.BUY )) {
            this.addMessageOnBuy(m);
        }
        for ( Message m : item.getDialog( ItemActionType.SELL )) {
            this.addMessageOnSell(m);
        }
        for ( Message m : item.getDialog( ItemActionType.FIND )) {
            this.addMessageOnFind(m);
        }
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addItemMessageListener(ItemMessageListener listener) {
        messageListeners.add(listener);
    }

    public void addItemActionListener(ItemActionListener listener) {
        actionListeners.add(listener);
    }

    public final void addMessageOnFind(Message msg) {
        super.msgList.add(msg);
    }

    public final void addMessageOnSell(Message msg) {
        msgListOnSell.add(msg);
    }

    public final void addMessageOnBuy(Message msg) {
        msgListOnBuy.add(msg);
    }

    @Override
    public int getPrice() {
        return price;
    }

    public final void setPrice(int price) {
        this.price = price;
    }

    @Override
    public LinkedList<Message> getDialog() {
        throw new UnsupportedOperationException("getDialog() ohne Parameter hier nicht unterstützt.");
    }

    public LinkedList<Message> getDialog(ItemActionType actionType) {
        switch (actionType) {
            case FIND: return super.msgList;
            case SELL: return msgListOnSell;
            case BUY:  return msgListOnBuy;
            default:   return new LinkedList<>();
        }
    }

    public void fireEvent(Object source, ItemActionType actionType) {
        HasDialog dialog = () -> getDialog(actionType);
        ItemEvent event = new ItemEvent(source, this, actionType, dialog);
        messageListeners.forEach( listener -> listener.showItemMessage( event ));
        actionListeners.forEach(  listener -> listener.itemActionPerformed( event ));
    }

    @Override
    public String toString() {
        return super.toString() + ", Preis: " + price + "\n" + description;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        return new Item(this);
    }

}
