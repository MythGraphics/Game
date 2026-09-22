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
import graphic.map.CollisionEvent;
import static graphic.map.DefaultBlockType.BUBBLE;
import graphic.map.GameMap;
import graphic.map.UWMap;
import graphic.tile.BlockTile;
import java.io.IOException;

public class UWGameRoutine extends DefaultGameRoutine implements ResourceConsumeListener {

    private final UWMap map;

    public UWGameRoutine(UWMap map, GameFrame gameFrame) {
        super(gameFrame);
        this.map = map;
    }

    @Override
    protected Player createPlayer() {
        Player player = super.createPlayer();
        Resource air = map.getResource();
        air.addResourceChangeListener(gameFrame);
        air.addResourceConsumeListener(this);
        player.addResource(air);
        DescriptorLoader dLoader = new DescriptorLoader( getClass() );
        try {
            player.setImage( dLoader.loadSpriteSet(TILESET+"player/", "descriptor")[0] );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public void resourceConsumePerformed(Resource r, int use, int overuse) {
        if ( overuse > 0 && r.getType() == AIR ) {
            getPlayer().getResource(HEALTH).forceConsume(overuse);
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getTarget().getBlockType() ) {
            case BUBBLE -> {
                getPlayer().getResource(AIR).recharge(100);
                if ( e.getTarget() instanceof BlockTile tile ) {
                    tile.destroy( (GameMap) e.getSource() );
                }
            }
            default -> {
                super.collisionPerformed(e);
            }
        }
    }

}
