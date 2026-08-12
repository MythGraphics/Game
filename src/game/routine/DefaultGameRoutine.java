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
import game.resource.ResourceChangeListener;
import static graphic.io.BinaryIO.TILESET;
import graphic.io.DescriptorLoader;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import graphic.texter.DialogOutputListener;
import java.io.IOException;

public class DefaultGameRoutine extends GameRoutine {

    private final Player player;
    private final GameFrame gameFrame;

    public DefaultGameRoutine(GameFrame gameFrame) {
        this.gameFrame  = gameFrame;
        this.player     = initPlayer();
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
        return gameFrame.textFrame;
    }

    private Player initPlayer() {
        Resource health = new Resource("Gesundheit", HEALTH, 1000, 1000);
        health.addResourceChangeListener(gameFrame);
        Player player = new Player( GameFrame.playerName, getDialogListener( null ), health );
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImg( dLoader.loadSpriteSets( TILESET+"player/" )[0][0] );

/*          player.setImg( TilesetUtility.getSpriteSetHorizontal(
 *              loadImage( TILESET+"player/girl_red_swimsuit.png" ), 140, 200, 4
 *          )[0]);
 */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    void addPlayerResource(Resource resource) {
        resource.addResourceChangeListener(gameFrame);
        getPlayer().addResource(resource);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getType() ) {
            case EXIT -> (( GameMap ) e.getSource() ).deactivate();
        }
    }

}
