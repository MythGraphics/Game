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

import game.Message;
import game.Player;
import game.Resource;
import graphic.texter.DialogOutputListener;
import java.util.LinkedList;

public class ReUsableItem extends UsableItem implements ReUsable {

    private final LinkedList<Message> msgListOnRemove = new LinkedList<>();

    public ReUsableItem(int id, String name) {
        super(id, name);
    }

    public void addMessageOnRemove(Message msg) {
        msgListOnRemove.add(msg);
    }

    @Override
    public ReUsable remove(Player player) {
        for (int i = 0; i < getItemEffectList().size(); ++i) {
            if ( getItemEffectList().get(i) != null ) {
                revokeEffect( player, getItemEffectList().get(i) );
        }}
        return this;
    }

    private void revokeEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource(effect.rType);
        effect.buff(r);
    }

    @Override
    public void itemActionPerformed(IsItem item, ItemActionType action, DialogOutputListener dialogListener) {
        super.itemActionPerformed(item, action, dialogListener);
        switch (action) {
            case ItemActionType.REMOVE -> dialogListener.show( () -> msgListOnRemove );
        }
    }

}
