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
import game.Message;
import game.TextBox;
import java.util.LinkedList;

public class Item extends TextBox implements IsItem, Tradable, ItemActionListener {

    private final LinkedList<Message> msgListOnBuy  = new LinkedList<>();
    private final LinkedList<Message> msgListOnSell = new LinkedList<>();

    private int price = 0;
    private String description = "";

    public Item(int id, String name) {
        super(id, name);
    }

    Item(Item item) {
        super(item);
        item.setPrice( item.getPrice() );
        for ( Message m : msgListOnBuy ) {
            item.addMessageOnBuy(m);
        }
        for ( Message m : msgListOnSell ) {
            item.addMessageOnSell(m);
        }
        for ( Message m : super.getDialog() ) {
            item.addMessageOnFind(m);
        }
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void addMessageOnFind(Message msg) {
        super.msgList.add(msg);
    }

    public void addMessageOnSell(Message msg) {
        msgListOnSell.add(msg);
    }

    public void addMessageOnBuy(Message msg) {
        msgListOnBuy.add(msg);
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void itemActionPerformed(IsItem item, ItemAction action, DialogOutputListener dialogListener) {
        switch (action) {
            case ItemAction.FIND -> dialogListener.show( () -> super.msgList );
            case ItemAction.SELL -> dialogListener.show( () -> msgListOnSell );
            case ItemAction.BUY  -> dialogListener.show( () -> msgListOnBuy );
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Preis: " + price + "\n" + description;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        if ( this.getClass() == Item.class ) {
            return new Item(this);
        } else {
            throw new CloneNotSupportedException("Item.clone(): Methode von erbender Klasse nicht unterstützt.");
        }
    }

}
