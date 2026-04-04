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
import game.Player;
import game.Resource;
import static game.item.ItemEffect.TYPE.PRÄFIX;
import static game.item.ItemEffect.TYPE.SUFFIX;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UsableItem extends Item implements Usable {

    private final List<ItemEffect> itemEffectList   = new ArrayList<>(); // Liste der Item Effekte auf Spieler Ressourcen
    private final LinkedList<Message> msgListOnUse  = new LinkedList<>();

    public UsableItem(int id, String name) {
        super(id, name);
    }

    public void addMessageOnUse(Message msg) {
        msgListOnUse.add(msg);
    }

    public void addItemEffect(ItemEffect... buffs) {
        this.itemEffectList.addAll( Arrays.asList( buffs ));
    }

    List<ItemEffect> getItemEffectList() {
        return itemEffectList;
    }

    @Override
    public boolean use(Player player) {
        boolean b = false;
        for (int i = 0; i < getItemEffectList().size(); ++i) {
            if ( getItemEffectList().get(i) != null ) {
                applyEffect( player, getItemEffectList().get( i ));
                b = true;
            }
        }
        return b;
    }

    private void applyEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource( effect.resource );
        r.buffMax( effect.buff_max );
        r.recharge( effect.value2add );
        player.fireResourceEvent(r);
    }

    @Override
    public void itemActionPerformed(IsItem item, ItemAction action, DialogOutputListener dialogListener) {
        super.itemActionPerformed(item, action, dialogListener);
        switch (action) {
            case ItemAction.USE -> dialogListener.show( () -> msgListOnUse );
        }
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder( super.getName() );
        for (int i = 0; i < itemEffectList.size(); ++i) {
            if ( itemEffectList.get(i) != null ) {
                if ( itemEffectList.get(i).type == PRÄFIX ) {
                    sb.insert( 0, " " );
                    sb.insert( 0, itemEffectList.get(i).name );
                }
                if ( itemEffectList.get(i).type == SUFFIX ) {
                    sb.append( " " );
                    sb.append( itemEffectList.get(i).name );
                }
            }
        }
        return sb.toString();
    }

}
