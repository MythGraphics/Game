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

import game.*;
import static game.item.ItemEffect.TYPE.PRÄFIX;
import static game.item.ItemEffect.TYPE.SUFFIX;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Item extends TextBox implements Tradable, ItemActionListener {

    final ArrayList<ItemEffect> itemEffect; // Liste der Item Effekte auf die Spieler Ressourcen
    /* msgListOnFind entspricht msgList der super-Klasse,
     * applyEffect() referenziert dann Items der msgListOnUse auf msgList der super-Klasse
     */
    final LinkedList<Message> msgListOnUse = new LinkedList<>();

    private int price = 0;
    private boolean reusable = true; // kann das Item an- und abgelegt werden (TRUE) oder wird es verbraucht (FALSE)?

    public Item(int id, String name) {
        super(id, name);
        this.itemEffect = new ArrayList<>();
    }

    public void addMessageOnUse(Message msg) {
        msgListOnUse.add(msg);
    }

    public void addMessageOnFind(Message msg) {
        super.msgList.add(msg);
    }

    public boolean isReusable() {
        return reusable;
    }

    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }

    public boolean isUsable() {
        return !itemEffect.isEmpty();
    }

    public void addItemEffect(ItemEffect... buffs) {
        this.itemEffect.addAll( Arrays.asList( buffs ));
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void revokeEffect(Player player) {
        for (int i = 0; i < itemEffect.size(); ++i) {
            if ( itemEffect.get(i) != null ) {
                revokeEffect( player, itemEffect.get(i) );
        }}
    }

    private void revokeEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource( effect.resource );
        r.buffMax( -effect.buff_max );
        player.fireResourceEvent(r);
    }

    public void applyEffect(Player player) {
        for (int i = 0; i < itemEffect.size(); ++i) {
            if ( itemEffect.get(i) != null ) {
                applyEffect( player, itemEffect.get(i) );
        }}
    }

    private void applyEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource( effect.resource );
        r.buffMax( effect.buff_max );
        r.recharge( effect.value2add );
        player.fireResourceEvent(r);
    }

    @Override
    public void itemActionPerformed(Item item, ItemAction action, DialogOutputListener dialogListener) {
        switch (action) {
            case ItemAction.FIND -> dialogListener.show( () -> super.msgList );
            case ItemAction.USE -> dialogListener.show( () -> msgListOnUse );
        }
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder( super.getName() );
        for (int i = 0; i < itemEffect.size(); ++i) {
            if ( itemEffect.get(i) != null ) {
                if ( itemEffect.get(i).type == PRÄFIX ) {
                    sb.insert( 0, " " );
                    sb.insert( 0, itemEffect.get(i).name );
                }
                if ( itemEffect.get(i).type == SUFFIX ) {
                    sb.append( " " );
                    sb.append( itemEffect.get(i).name );
                }
            }
        }
        return sb.toString();
    }

}
