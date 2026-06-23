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

import graphic.texter.Message;
import game.Player;
import game.Resource;
import static game.item.ItemEffect.ItemEffectType.PRÄFIX;
import static game.item.ItemEffect.ItemEffectType.SUFFIX;
import game.item.ItemEvent.ItemActionType;
import static game.item.ItemEvent.ItemActionType.USE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UsableItem extends Item {

    final List<ItemEffect> itemEffectList  = new ArrayList<>(); // Liste der Item Effekte auf Spieler-Ressourcen
    final LinkedList<Message> msgListOnUse = new LinkedList<>();

    public UsableItem(int id, String name) {
        super(id, name);
    }

    UsableItem(UsableItem item) {
        super(item);
        for ( Message m : item.getDialog( ItemActionType.USE )) {
            this.addMessageOnUse(m);
        }
    }

    public final void addMessageOnUse(Message msg) {
        msgListOnUse.add(msg);
    }

    public void addItemEffect(ItemEffect... buffs) {
        this.itemEffectList.addAll( Arrays.asList( buffs ));
    }

    List<ItemEffect> getItemEffectList() {
        return itemEffectList;
    }

    public boolean use(Player player) {
        boolean success = false;
        for (int i = 0; i < getItemEffectList().size(); ++i) {
            if ( getItemEffectList().get(i) != null ) {
                applyEffect( player, getItemEffectList().get( i ));
                success = true;
            }
        }
        return success;
    }

    private void applyEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource(effect.rType);
        effect.buff(r);
    }

    @Override
    public LinkedList<Message> getDialog(ItemActionType actionType) {
        switch (actionType) {
            case USE: return msgListOnUse;
            default:  return super.getDialog(actionType);
        }
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder( super.getName() );
        for (int i = 0; i < itemEffectList.size(); ++i) {
            if ( itemEffectList.get(i) != null ) {
                if ( itemEffectList.get(i).eType == PRÄFIX ) {
                    sb.insert( 0, " " );
                    sb.insert( 0, itemEffectList.get(i).name );
                }
                if ( itemEffectList.get(i).eType == SUFFIX ) {
                    sb.append( " " );
                    sb.append( itemEffectList.get(i).name );
                }
            }
        }
        return sb.toString();
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        if ( this.getClass() == UsableItem.class ) {
            return new UsableItem(this);
        } else {
            throw new CloneNotSupportedException(
                getClass() + ": clone() von erbender Klasse nicht unterstützt."
            );
        }
    }

}
