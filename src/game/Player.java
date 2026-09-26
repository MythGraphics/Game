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
import static game.item.ItemEvent.ItemActionType.REMOVE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import game.quest.Quest;
import game.quest.QuestListener;
import static game.quest.QuestStatus.ACTIVE;
import static game.quest.QuestStatus.COMPLETE;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.CREDIT;
import static game.resource.Resource.ResourceType.HEALTH;
import graphic.texter.DialogOutputListener;
import java.util.ArrayList;
import java.util.List;
import game.combat.Combatant;

public class Player extends Enemy implements Trader {

    private final DialogOutputListener dialogListener;                          // Dialog-Ausgabe
    private final ArrayList<UsableItem> items           = new ArrayList<>();    // aktive, also angelegte Items
    private final List<QuestListener> questListeners    = new ArrayList<>();    // feuert bei Änderung des Quest-Status
    private final InventoryManager inventory;

    private Quest quest;

    public Player(String name, DialogOutputListener dialogListener, Resource... resources) {
        super(-1, name, resources);
        this.dialogListener = dialogListener;
        this.inventory      = new InventoryManager(this);
    }

    public Player(String name, DialogOutputListener dialogListener) {
        this(name,
             dialogListener,
             new Resource( "Gesundheit", HEALTH, 1000, 1000 ),
             new Resource( "Credits", CREDIT, 1000*1000, 0 )
        );

    }

    public void setPlayerAsMinion(game.combat.Player minion) {
        getMinionManager().add(minion);
    }

    public game.combat.Player getPlayerAsMinion() {
        Combatant c = getMinion();
        if (c != null && c instanceof game.combat.Player player) {
            return player;
        } else {
            return null;
        }
    }

    private void fireQuestEvent(Quest quest) {
        questListeners.forEach( listener -> listener.questActionPerformed( quest ));
    }

    public DialogOutputListener getDialogOutputListener() {
        return dialogListener;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getCredits() {
        return getResource(CREDIT).getValue();
    }

    @Override
    public void addCredits(int credits) {
        getResource(CREDIT).recharge(credits);
    }

    public void addQuestListener(QuestListener listener) {
        questListeners.add(listener);
    }

    void addActiveItem(ReUsableItem item) {
        items.add(item);
    }

    public ReUsableItem removeItem(ReUsableItem item) {
        if ( items.remove( item )) {
            item.remove(this); // ItemEffekt zurücksetzen
            item.fireEvent(this, REMOVE);
            return item;
        } else {
            return null;
        }
    }

    public boolean acceptQuest(Quest quest) {
        boolean b;
        if ( b = quest.accept() ) {
            this.quest = quest;
            fireQuestEvent(quest);
        }
        return b;
    }

    public Quest getQuest() {
        return quest;
    }

    /**
     * Gibt die Quest ab.
     * Dabei wird geprüft, ob das Inventar das QuestObjective enthält.
     * Ist dies der Fall, wird die Questbelohnung dem Inventar hinzugefügt und das QuestObjective aus diesem entfernt.
     * @return TRUE or FALSE
     */
    public boolean deliverQuest() {
        if ( !hasActiveQuest() ) {
            return false;
        }
        if ( quest.getStatus() != COMPLETE ) {
            return false;
        }

        inventory.remove( quest.getObjective() );
        inventory.add( quest.deliver() );
        fireQuestEvent(quest);
        quest = null;
        return true;
    }

    private void updateQuestStatus() {
        Item qObj = quest.getObjective();
        if ( inventory.hasItem( qObj )) {
            quest.update(qObj);
        }
    }

    public boolean hasActiveQuest() {
        if (quest == null) {
            return false;
        }
        updateQuestStatus();
        return quest.getStatus() == COMPLETE || quest.getStatus() == ACTIVE;
    }

    @Override
    public Player clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException( "Clone on " + getClass() + " not supported." );
    }

}
