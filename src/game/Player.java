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

import game.combat.Combatant;
import game.item.Item;
import static game.item.ItemEvent.ItemActionType.REMOVE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import game.quest.Quest;
import game.quest.QuestListener;
import static game.quest.QuestStatus.ACTIVE;
import static game.quest.QuestStatus.COMPLETE;
import game.resource.Resource;
import game.resource.Resource.ResourceType;
import static game.resource.Resource.ResourceType.CREDIT;
import static game.resource.Resource.ResourceType.HEALTH;
import graphic.texter.DialogOutputListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends InteractiveObject implements HasHealth, Trader, HasMinion {

    private final Map<Resource.ResourceType, Resource> resources;
    private final DialogOutputListener dialogListener;  // Dialog-Ausgabe
    private final ArrayList<UsableItem> items;          // aktive, also angelegte Items
    private final List<QuestListener> questListeners;   // feuert bei Änderung des Quest-Status
    private final InventoryManager inventory;
    private final MinionManager minions;

    private Quest quest;
    private game.combat.Player playerMinion;

    public Player(String name, DialogOutputListener dialogListener, Resource... resources) {
        this(name, dialogListener);
        if (resources != null) {
            for (Resource r : resources) {
                addResource(r);
            }
        }
    }

    public Player(String name, DialogOutputListener dialogListener) {
        super(name);
        this.dialogListener = dialogListener;
        this.questListeners = new ArrayList<>();
        this.items          = new ArrayList<>();
        this.inventory      = new InventoryManager(this);
        this.minions        = new MinionManager();
        this.resources      = new HashMap<>();
        addResource( new Resource( "Gesundheit", HEALTH, 1000, 1000 ));
        addResource( new Resource( "Credits", CREDIT, 1000*1000, 0 ));
    }

    /**
     * Fügt die Resource dem Spieler und damit seiner Resourcenliste hinzu.
     * @param r hinzuzufügende Resource
     */
    public final void addResource(Resource r) {
        resources.put( r.getType(), r );
    }

    public Resource getResource(ResourceType type) {
        return resources.get(type);
    }

    @Override
    public Resource getHealth() {
        return getResource(HEALTH);
    }

    @Override
    public boolean isAlive() {
        return getHealth().getValue() > 0;
    }

    @Override
    public void takeDamage(int damage) {
        if ( damage < 0 ) {
            getHealth().recharge(damage);
        } else {
            getHealth().forceConsume(damage);
        }
    }

    public void setPlayerAsMinion(game.combat.Player minion) {
        this.playerMinion = minion;
    }

    public game.combat.Player getPlayerAsMinion() {
        return playerMinion;
    }

    private void fireQuestEvent(Quest quest) {
        questListeners.forEach( listener -> listener.questActionPerformed( quest ));
    }

    public DialogOutputListener getDialogOutputListener() {
        return dialogListener;
    }

    @Override
    public MinionManager getMinionManager() {
        return minions;
    }

    @Override
    public Combatant getMinion() {
        return minions.getCurrent();
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
    public InteractiveObject clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException( "Clone on " + getClass() + " not supported." );
    }

}
