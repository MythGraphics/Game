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
import game.item.*;
import game.quest.Quest;
import game.quest.QuestListener;
import static game.quest.QuestStatus.ACTIVE;
import static game.quest.QuestStatus.READY;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Player extends InteractiveObject implements HasHealth, Trader {

    private final ArrayList<Resource> resources; // Health, Credits, Reputation, ...
    private final ArrayList<Usable> items; // aktive, also angelegte Items
    private final Collection<ItemActionListener> itemActionListeners; // feuert bei Änderung an aktiven Spieler-Items
    private final Collection<ItemEffectListener> resourceChangeListeners; // feuert bei Änderung an den Resourcen
    private final Collection<QuestListener> questListeners; // feuert bei Änderung des Quest-Status
    private final DialogOutputListener dialogListener; // Dialog-Ausgabe
    private final InventoryManager inventory;
    private final MinionManager minions;

    private Quest quest;
    private game.combat.Player playerMinion;

    public Player(String name, DialogOutputListener dialogListener) {
        super(name);
        this.dialogListener = dialogListener;
        resources = new ArrayList<>();
        items = new ArrayList<>();
        itemActionListeners = new HashSet<>();
        resourceChangeListeners = new HashSet<>();
        questListeners = new HashSet<>();
        inventory = new InventoryManager(this);
        minions = new MinionManager();
        addDefaultResources();
    }

    private void addDefaultResources() {
        addResource( new Resource( "Gesundheit", HEALTH )); // index 0
        addResource( new Resource( "Münzen", CREDIT, 1000000, 0 )); // index 1
    }

    @Override
    public Resource getHealth() {
        return resources.get(0);
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
            getHealth().force(damage);
        }
    }

    public void setPlayerAsMinion(game.combat.Player minion) {
        this.playerMinion = minion;
    }

    public game.combat.Player getPlayerAsMinion() {
        return playerMinion;
    }

    // wird von Item.applyEffect() aufgerufen
    public void fireResourceEvent(Resource r) {
        resourceChangeListeners.forEach( actionListener -> actionListener.itemEffectPerformed( this ));
    }

    private void fireItemEvent(IsItem item, ItemAction action) {
        itemActionListeners.forEach( listener -> listener.itemActionPerformed(
            item, action, dialogListener
        ));
    }

    private void fireQuestEvent(Quest quest) {
        questListeners.forEach( listener -> listener.questActionPerformed( quest ));
    }

    /**
     * Fügt die Resource dem Spieler und damit seiner Resourcenliste hinzu.
     * @param r hinzuzufügende Resource
     * @return index der Resource in der Resourcenliste
     */
    public final int addResource(Resource r) {
        resources.add(r);
        return resources.lastIndexOf(r);
    }

    public Resource getResource(int index) {
        return resources.get(index);
    }

    public Resource getResource(ResourceType type) {
        for ( Resource r : resources ) {
            if ( r.getType() == type ) {
                return r;
            }
        }
        return null;
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

    public void addResourceListener(ItemEffectListener listener) {
        resourceChangeListeners.add(listener);
    }

    public void addItemActionListener(ItemActionListener listener) {
        itemActionListeners.add(listener);
    }

    public void addQuestListener(QuestListener listener) {
        questListeners.add(listener);
    }

    void addActiveItem(ReUsable item) {
        items.add(item);
    }

    public ReUsable removeActiveItem(ReUsable item) {
        if ( items.remove( item )) {
            item.remove(this); // ItemEffekt zurücksetzen
            fireItemEvent(item, ItemAction.REMOVE);
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

}
