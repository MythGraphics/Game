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
import game.ID;
import game.Npc;
import game.item.Item;
import game.item.ItemEffect;
import static game.item.ItemEffect.ItemEffectType.PRÄFIX;
import static game.item.ItemEffect.ItemEffectType.SUFFIX;
import static game.item.ItemEffect.ValueType.ABSOLUTE;
import static game.item.ItemEffect.ValueType.PERCENT;
import game.item.ReUsableItem;
import game.item.UsableItem;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.*;
import static graphic.map.BlockType.ENVIRONMENT_A;
import static graphic.map.BlockType.TEXTSIGN;
import java.io.IOException;

public class LandMapGameRoutine extends RPGRoutine {

    public LandMapGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
        updatePlayer(gameFrame);
        try {
            addDialog( TEXTSIGN, getLoader().loadNextDialog( getPlayer() ));
            addDialog( ENVIRONMENT_A, getLoader().loadNextDialog( getPlayer() ));
            Npc npc = getLoader().loadNextNpc( getPlayer() );
            addNpc(npc);
            initQuest(npc);
            initEnvLoot();
        } catch (IOException | NullPointerException e) {
            System.err.println( "Initialisieren der Spiel-Routine fehlgeschlagen - Abbruch!" );
            System.err.println( "Ursache: " + e.getMessage() );
            System.exit(255);
        }
    }

    private void updatePlayer(GameFrame frame) {
        Resource air = new Resource("Luft", AIR, 100, 100);
        air.addResourceChangeListener(frame);
        getPlayer().addResource(air);
        Resource money = new Resource("Credits", CREDIT, 1000, 0);
        money.addResourceChangeListener(frame);
        getPlayer().addResource(money);

/*      player.setImg( TilesetUtility.getSpriteSetHorizontal(
            loadImage( TILESET+"player/girl_red_swimsuit.png" ), 140, 200, 4
        )[0]);
 */
    }

    private void initQuest(Npc npc) throws IOException {
        ReUsableItem qItem = new ReUsableItem(-1, "Halskette");
        qItem.addItemEffect(
            new ItemEffect("Neptunes", PRÄFIX, HEALTH, 100, PERCENT, false),
            new ItemEffect("des Delfins", SUFFIX, AIR, 100, PERCENT, false)
        );
        int id = ID.getNextQuestId();
        Item qObj = getLoader().loadQuest( id, npc, qItem, getPlayer() );
        getLoader().loadQuestObjectiveDialog( qObj, getPlayer() );
        addQuestLoot(id, qObj);
    }

    private void initEnvLoot() throws IOException {
        UsableItem item = (UsableItem) getLoader().loadNextItem( getPlayer() );
        item.addItemEffect( new ItemEffect( "Blutsaugender", PRÄFIX, HEALTH, 20, PERCENT, false ));
        addEnvironmentLoot(item);
        item = (UsableItem) getLoader().loadNextItem( getPlayer() );
        item.addItemEffect( new ItemEffect( "einfacher", PRÄFIX, CREDIT, item.getPrice(), ABSOLUTE ));
        addEnvironmentLoot(item);
    }

}
