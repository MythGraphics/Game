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
import static game.item.ItemEffect.ValueType.ABSOLUTE;
import static game.item.ItemEffect.ValueType.PERCENT;
import game.item.ReUsableItem;
import game.item.UsableItem;
import static graphic.io.BinaryIO.TILESET;
import graphic.io.DescriptorLoader;
import static graphic.map.BlockType.ENVIRONMENT_A;
import static graphic.map.BlockType.TEXTSIGN;
import graphic.map.CollisionEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public class LandMapGameRoutine extends GameRoutine {

    final LinkedList<Item> items = new LinkedList<>(); // Items, die zufällig (10%) in der Landschaft gefunden werden können

    private Npc npc;
    private Item qObj;
    private Player player;

    public LandMapGameRoutine(GameFrame frame) {
        super(frame.textFrame);
        initPlayer(frame);
        try {
            addDialog( TEXTSIGN, getLoader().loadNextDialog( player ));
            addDialog( ENVIRONMENT_A, getLoader().loadNextDialog( player ));
            npc = getLoader().loadNextNpc(player);

            initQuests(frame);
            initEnvItems(frame);
        } catch (IOException | NullPointerException e) {
            System.err.println( "Initialisieren der Spiel-Routine fehlgeschlagen - Abbruch!" );
            System.err.println( "Ursache: " + e.getMessage() );
            System.exit(255);
        }
    }

    private void initPlayer(GameFrame frame) {
        Resource health = new Resource("Gesundheit", HEALTH, 1000, 1000);
        Resource air    = new Resource("Luft", AIR, 100, 100);
        Resource money  = new Resource("Credits", CREDIT, 1000, 0);
        health.addResourceChangeListener(frame);
        air.addResourceChangeListener(frame);
        money.addResourceChangeListener(frame);
        player = new Player(GameFrame.playerName, frame.textFrame, health, air, money);
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImg( dLoader.loadSpriteSets( TILESET+"player/" )[0][0] );
        } catch (IOException e) {
            e.printStackTrace();
        }

/*      player.setImg( TilesetUtility.getSpriteSetHorizontal(
            loadImage( TILESET+"player/girl_red_swimsuit.png" ), 140, 200, 4
        )[0]);
 */
    }

    private void initQuests(GameFrame frame) throws IOException {
        ReUsableItem qItem = new ReUsableItem(-1, "Halskette");
        qItem.addItemActionListener(frame);
        qItem.addItemMessageListener(frame);
        qItem.addItemEffect(
            new ItemEffect("Neptunes", PRÄFIX, HEALTH, 100, PERCENT, false),
            new ItemEffect("des Delfins", SUFFIX, AIR, 100, PERCENT, false)
        );
        int questID = ID.getNextQuestId();
        qObj = getLoader().loadQuest(questID, npc, qItem, player);
        getLoader().loadQuestObjectiveDialog(qObj, player);
    }

    private void initEnvItems(GameFrame frame) throws IOException {
        UsableItem item = (UsableItem) getLoader().loadNextItem(player);
        item.addItemEffect( new ItemEffect( "Blutsaugender", PRÄFIX, HEALTH, 20, PERCENT, false ));
        items.add(item);
        item = (UsableItem) getLoader().loadNextItem(player);
        item.addItemEffect( new ItemEffect( "einfacher", PRÄFIX, CREDIT, item.getPrice(), ABSOLUTE ));
        items.add(item);
        items.forEach( i -> {
            // Listener registrieren
            i.addItemActionListener(frame);
            i.addItemMessageListener(frame);
        });
        Collections.shuffle(items); // Item-Liste durchmischen
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getType() ) {
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
                switch ( e.getTarget().getType() ) {
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
