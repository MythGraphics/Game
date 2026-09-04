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
import static game.resource.Resource.ResourceType.AIR;
import static game.resource.Resource.ResourceType.HEALTH;
import game.resource.ResourceConsumeListener;
import static graphic.io.BinaryIO.TILESET;
import graphic.io.DescriptorLoader;
import graphic.map.BlockTile;
import graphic.map.CollisionEvent;
import static graphic.map.DefaultBlockType.BUBBLE;
import graphic.map.UWMap;
import java.io.IOException;

public class UWMapGameRoutine extends DefaultGameRoutine implements ResourceConsumeListener {

    private final Player player;

    public UWMapGameRoutine(UWMap map, GameFrame gameFrame) {
        super(gameFrame);
        this.player = initPlayer(map, gameFrame);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    private Player initPlayer(UWMap map, GameFrame frame) {
        Resource health = new Resource("Gesundheit", HEALTH, 1000, 1000);
        health.addResourceChangeListener(frame);
        Resource air = map.getResource();
        air.addResourceChangeListener(frame);
        air.addResourceConsumeListener(this);
        Player player = new Player(GameFrame.playerName, frame.textFrame, health, air);
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImg( dLoader.loadSpriteSets( TILESET+"player/" )[0][0] );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public void resourceConsumePerformed(Resource r, int use, int overuse) {
        if (overuse > 0 && r.getType() == AIR ) {
            player.getResource(HEALTH).forceConsume(overuse);
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getTarget().getBlockType() ) {
            case BUBBLE -> {
                getPlayer().getResource(AIR).recharge(100);
                if ( e.getTarget() instanceof BlockTile tile ) {
                    tile.destroy();
                }
            }
            default -> {
                super.collisionPerformed(e);
            }
        }
    }

}
