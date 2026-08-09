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
import game.Player;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.HEALTH;
import static graphic.io.BinaryIO.TILESET;
import graphic.io.DescriptorLoader;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import graphic.texter.DialogOutputListener;
import java.io.IOException;

public class DefaultGameRoutine extends GameRoutine {

    private final DialogOutputListener outputListener;
    private final Player player;

    public DefaultGameRoutine(GameFrame gameFrame) {
        this.player     = initPlayer(gameFrame);
        outputListener  = gameFrame.textFrame;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Overwrite to implement an event-dependent dialogListener besides the initial one.
     * @param e CollisionEvent
     * @return DialogOutputListener
     */
    @Override
    public DialogOutputListener getDialogListener(CollisionEvent e) {
        return outputListener;
    }

    private Player initPlayer(GameFrame gameFrame) {
        Resource health = new Resource("Gesundheit", HEALTH, 1000, 1000);
        health.addResourceChangeListener(gameFrame);
        Player player = new Player(GameFrame.playerName, gameFrame.textFrame, health);
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImg( dLoader.loadSpriteSets( TILESET+"player/" )[0][0] );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getType() ) {
            case EXIT -> (( GameMap ) e.getSource() ).deactivate();
        }
    }

}
