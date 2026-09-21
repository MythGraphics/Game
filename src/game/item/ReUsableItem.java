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

import game.HasUIImage;
import game.Player;
import game.item.ItemEvent.ItemActionType;
import static game.item.ItemEvent.ItemActionType.REMOVE;
import game.resource.Resource;
import graphic.texter.Message;
import java.util.LinkedList;

public class ReUsableItem extends UsableItem implements HasUIImage {

    final LinkedList<Message> msgListOnRemove = new LinkedList<>();

    public ReUsableItem(int id, String name) {
        super(id, name, false); // ein ReUsableItem ist niemals 'consumable'
    }

    public void addMessageOnRemove(Message msg) {
        msgListOnRemove.add(msg);
    }

    public ReUsableItem remove(Player player) {
        for (int i = 0; i < getItemEffectList().size(); ++i) {
            if ( getItemEffectList().get(i) != null ) {
                revokeEffect( player, getItemEffectList().get( i ));
        }}
        return this;
    }

    private void revokeEffect(Player player, ItemEffect effect) {
        Resource r = player.getResource(effect.rType);
        effect.revoke(r);
    }

    @Override
    public LinkedList<Message> getDialog(ItemActionType actionType) {
        switch (actionType) {
            case REMOVE: return msgListOnRemove;
            default:     return super.getDialog(actionType);
        }
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException( getClass() + ": clone() not supported." );
    }

}
