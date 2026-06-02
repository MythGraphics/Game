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
import static game.item.ItemEffect.ItemEffectType.PRÄFIX;
import static game.item.ItemEffect.ItemEffectType.SUFFIX;
import game.item.ReUsableItem;
import game.item.UsableItem;
import graphic.CollisionEvent;
import static graphic.io.BinaryIO.TILESET;
import graphic.io.DescriptorLoader;
import static graphic.map.BlockType.ENVIRONMENT_A;
import static graphic.map.BlockType.TEXTSIGN;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public class LandMapGameRoutine extends GameRoutine {

    final LinkedList<Item> items = new LinkedList<>(); // Items, die zufällig (10%) in der Landschaft gefunden werden können

    private Npc npc;
    private Item qObj;
    private Player player;

    public LandMapGameRoutine(GameFrame frame) {
        super(frame);
        initPlayer(frame);
        try {
            addDialog( TEXTSIGN, getLoader().loadNextDialog( player ));
            addDialog( ENVIRONMENT_A, getLoader().loadNextDialog( player ));
            npc = getLoader().loadNextNpc(player);

            // load quest
            ReUsableItem qItem = new ReUsableItem(-1, "Halskette");
            qItem.addItemEffect(
                new ItemEffect("Neptunes", PRÄFIX, HEALTH, 0, 200),
                new ItemEffect("des Delfins", SUFFIX, AIR, 0, 200)
            );
            int questID = ID.getNextQuestId();
            qObj = getLoader().loadQuest(questID, npc, qItem, player);
            getLoader().loadQuestObjectiveDialog(qObj, player);

            // load environment items
            UsableItem item = (UsableItem) getLoader().loadNextItem(player);
            item.addItemEffect( new ItemEffect( "Blutsaugender", PRÄFIX, HEALTH, 20, 20 ));
            items.add(item);
            item = (UsableItem) getLoader().loadNextItem(player);
            item.addItemEffect( new ItemEffect( "einfacher", PRÄFIX, CREDIT, item.getPrice() ));
            items.add(item);
            Collections.shuffle(items); // Item-Liste durchmischen
        } catch (IOException e) {
            System.err.println( "Initialisieren der Spiel-Routine fehlgeschlagen - Abbruch!" );
            System.err.println( "Ursache: " + e.getMessage() );
            System.exit(255);
        }
    }

    private void initPlayer(GameFrame frame) {
        Resource health = new Resource( "Gesundheit", HEALTH, 1000, 1000 );
        health.addResourceChangeListener(frame);
        player = new Player(GameFrame.playerName, frame.textFrame, health);
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImg( dLoader.loadSpriteSets(TILESET+"player/" )[0][0] );
        } catch (IOException e) {
            e.printStackTrace();
        }

/*      player.setImg( TilesetUtility.getSpriteSetHorizontal(
            loadImage( TILESET+"player/girl_red_swimsuit.png" ), 140, 200, 4
        )[0]);
 */
    }

    @Override
    public Player getPlayer() {
        return player;
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

}
