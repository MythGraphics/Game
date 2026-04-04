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

    private final LinkedList<Message> msgListOnBuy = new LinkedList<>();
    private final LinkedList<Message> msgListOnSell = new LinkedList<>();

    private int price = 0;

    public Item(int id, String name) {
        super(id, name);
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

}
