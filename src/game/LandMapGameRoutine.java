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

import static game.Resource.ResourceType.*;
import game.item.Item;
import game.item.ItemEffect;
import game.item.ItemEffect.TYPE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import graphic.CollisionEvent;
import static graphic.map.BlockType.ENVIRONMENT_A;
import static graphic.map.BlockType.TEXTSIGN;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;

public class LandMapGameRoutine extends PeacefulGameRoutine {

    final LinkedList<Item> items; // Items, die zufällig (10%) in der Landschaft gefunden werden können

    private Npc npc;
    private Item qObj;

    public LandMapGameRoutine(Player player, JFrame frame) {
        super(player, null, frame);
        this.items = new LinkedList<>();
        try {
            addDialog(TEXTSIGN, getLoader().loadNextDialog(player));
            addDialog(ENVIRONMENT_A, getLoader().loadNextDialog(player));

            npc = getLoader().loadNextNpc(player);

            // load quest
            ReUsableItem qItem = new ReUsableItem(-1, "Halskette");
            qItem.addItemEffect(
                new ItemEffect("Neptunes", ItemEffect.TYPE.PRÄFIX, HEALTH, 0, 200),
                new ItemEffect("des Delfins", ItemEffect.TYPE.SUFFIX, AIR, 0, 200)
            );
            int questID = ID.getNextQuestId();
            qObj = getLoader().loadQuest(questID, npc, qItem, player);
            getLoader().loadQuestObjectiveDialog(qObj, player);

            // load environment items
            UsableItem item = (UsableItem) getLoader().loadNextItem(player);
            item.addItemEffect( new ItemEffect( "Blutsaugender", TYPE.PRÄFIX, HEALTH, 20, 20 ));
            items.add(item);
            item = (UsableItem) getLoader().loadNextItem(player);
            item.addItemEffect( new ItemEffect( "einfacher", TYPE.PRÄFIX, CREDIT, item.getPrice() ));
            items.add(item);

            Collections.shuffle(items); // Item-Liste durchmischen
        } catch (IOException e) {
            System.err.println("Initialisieren der Haupt-Routine fehlgeschlagen.");
            System.err.println( e.getMessage() );
            System.exit(255);
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getCollisionType() ) {
            case ENV_PASS -> {
                if ( rand.nextInt(100) < 90 ) {
                    break;
                }
                if ( player.hasActiveQuest() && player.getQuest().check( qObj )) {
                    player.getInventory().add(qObj);
                    break;
                }
                if ( !items.isEmpty() ) {
                    player.getInventory().add( items.poll() );
                    break;
                }
            }
            case INTERACTIVE -> {
                switch ( e.getBlock().getType() ) {
                    case NPC -> {
                        if (player.hasActiveQuest()) {
                            player.deliverQuest();
                            dialogListener.show( player.getQuest() );
                            return;
                        }
                        dialogListener.show(npc);
                        if ( npc.hasQuest() ) {
                            dialogListener.show( npc.getQuest() );
                            player.acceptQuest( npc.getQuest() );
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

}
