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

import game.Resource.ResourceType;
import static game.Resource.ResourceType.CREDIT;
import static game.Resource.ResourceType.HEALTH;
import game.item.Item;
import static game.item.ItemEvent.ItemActionType.REMOVE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import game.quest.Quest;
import game.quest.QuestListener;
import static game.quest.QuestStatus.ACTIVE;
import static game.quest.QuestStatus.READY;
import graphic.texter.DialogOutputListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends InteractiveObject implements HasHealth, Trader {

    private final Map<ResourceType, Resource> resources; // Health, ...
    private final ArrayList<UsableItem> items; // aktive, also angelegte Items
    private final List<QuestListener> questListeners; // feuert bei Änderung des Quest-Status
    private final DialogOutputListener dialogListener; // Dialog-Ausgabe
    private final InventoryManager inventory;
    private final MinionManager minions;

    private Quest quest;
    private game.combat.Player playerMinion;

    public Player(String name, DialogOutputListener dialogListener) {
        this(
            name, dialogListener,
            new Resource( "Gesundheit", HEALTH, 1000, 1000 ),
            new Resource( "Credits", CREDIT, 1000*1000, 0 )
        );
    }

    public Player(String name, DialogOutputListener dialogListener, Resource... resources) {
        super(name);
        this.dialogListener = dialogListener;
        this.resources = new HashMap<>();
        for ( Resource r : resources) {
            addResource(r);
        }
        items = new ArrayList<>();
        questListeners = new ArrayList<>();
        inventory = new InventoryManager(this);
        minions = new MinionManager();
    }

    @Override
    public Resource getHealth() {
        return resources.get(HEALTH);
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

    public MinionManager getMinionManager() {
        return minions;
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

    public void acceptQuest(Quest quest) {
        quest.accept();
        this.quest = quest;
        fireQuestEvent(quest);
    }

    public Quest getQuest() {
        return quest;
    }

    /**
     * Gibt die Quest ab.
     * Dabei wird geprüft, ob das Inventar das QuestObjective enthält.
     * Ist dies der Fall, wird die Questbelohnung dem Inventar hinzugefügt und das QuestObjective aus diesem entfernt.
     */
    public void deliverQuest() {
        if ( !hasActiveQuest() ) {
            return;
        }
        Item qObj = quest.getQuestObjective();
        if ( !inventory.hasItem( qObj )) {
            return;
        }
        if ( !quest.check( qObj )) {
            return;
        }
        inventory.remove(qObj);
        inventory.add( quest.deliver() );
        fireQuestEvent(quest);
        quest = null;
    }

    public boolean hasActiveQuest() {
        if ( quest == null ) {
            return false;
        }
        return ( quest.getStatus() == READY ) || ( quest.getStatus() == ACTIVE );
    }

    @Override
    public InteractiveObject clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException( "Clone on " + getClass() + " not supported." );
    }

}
