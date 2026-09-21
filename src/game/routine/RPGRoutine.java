/*
 *
 */

package game.routine;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.GameFrame;
import game.Npc;
import game.Player;
import game.item.Item;
import static game.quest.QuestStatus.INACTIVE;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.CREDIT;
import graphic.map.CollisionEvent;
import static graphic.map.DefaultBlockType.NPC;
import graphic.map.InteractionType;
import graphic.map.IsBlock;
import java.util.*;

public abstract class RPGRoutine extends MartialGameRoutine {

    final LinkedList<Item> envLootPool = new LinkedList<>(); // Items, die zufällig in der Landschaft gefunden werden können
    final Map<Integer, Item> qLootPool = new HashMap<>(); // QuestItems, die zufällig in der Landschaft gefunden werden können, wenn die dazu gehörige Quest aktiv ist.

    private final Map<IsBlock, Npc> npcMap = new HashMap<>();
    private final List<Npc> npcList;

    public RPGRoutine(GameFrame gameFrame, List<Npc> npcList) {
        super(gameFrame);
        this.npcList = npcList;
    }

    public RPGRoutine(GameFrame gameFrame) {
        this( gameFrame, new LinkedList<>() );
    }

    private void mapNpc(IsBlock block, Npc npc) {
        npcMap.put(block, npc);
    }

    public void addNpc(Npc npc) {
        addDialog(NPC, npc);
        npcList.add(npc);
    }

    public Npc getNpc(IsBlock block) {
        Npc npc = npcMap.get(block);
        if ( npc == null && !npcList.isEmpty() ) {
            npc = npcList.removeFirst();
            mapNpc(block, npc);
        }
        return npc;
    }

    public void addEnvironmentLoot(Item item) {
        addItemListener(item);
        envLootPool.add(item);
        Collections.shuffle(envLootPool); // Pool durchmischen
    }

    public void addQuestLoot(int id, Item questItem) {
        addItemListener(questItem);
        qLootPool.put(id, questItem);
    }

    private void addItemListener(Item item) {
        item.addItemActionListener(gameFrame);
        item.addItemMessageListener(gameFrame);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getType() ) {
            case InteractionType.NPC -> {
                Npc npc = getNpc( e.getTarget() );
                if (npc == null) {
                    break;
                }
                if ( getPlayer().hasActiveQuest() ) {
                    getDialogListener(e).show( getPlayer().getQuest() );
                    getPlayer().deliverQuest();
                } else {
                    if ( npc.hasQuest() ) {
                        getDialogListener(e).show( npc.getQuest() );
                        if ( npc.getQuest().getStatus() == INACTIVE ) {
                            getPlayer().acceptQuest( npc.getQuest() );
                        }
                    }
                }
            }
            case InteractionType.TERRAIN -> {
                if ( rand.nextInt( 100 ) < 90 ) {
                    break;
                }
                if ( getPlayer().hasActiveQuest() ) {
                    int id = getPlayer().getQuest().getId();
                    if ( qLootPool.containsKey( id )) {
                        getPlayer().getInventory().add( qLootPool.remove( id ));
                        break;
                    } else {
                        System.err.println("QuestItem zu Quest-ID #" + id + " nicht im Pool.");
                    }
                }
                if ( !envLootPool.isEmpty() ) {
                    getPlayer().getInventory().add( envLootPool.poll() );
                }
            }
        }
    }

}
