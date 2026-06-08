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

import static game.Resource.ResourceType.AIR;
import static game.Resource.ResourceType.HEALTH;
import graphic.CollisionEvent;
import static graphic.io.BinaryIO.TILESET;
import static graphic.io.BinaryIO.loadImage;
import graphic.io.TilesetUtility;
import graphic.map.GameMap;
import graphic.map.UWMap;

public class UWMapGameRoutine extends GameRoutine implements ResourceConsumeListener {

    private Player player;

    public UWMapGameRoutine(UWMap map, GameFrame frame) {
        super(frame);
        initPlayer(map, frame);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    private void initPlayer(UWMap map, GameFrame frame) {
        Resource health = new Resource("Gesundheit", HEALTH, 1000, 1000);
        health.addResourceChangeListener(frame);
        Resource air = map.getResource();
        air.addResourceChangeListener(frame);
        air.addResourceConsumeListener(this);
        player = new Player(GameFrame.playerName, frame.textFrame, health, air);
        player.setImg( TilesetUtility.getSpriteSetHorizontal(
            loadImage( TILESET+"player/girl_red_swimsuit.png" ), 140, 200, 4
        )[0]);
    }

    @Override
    public void resourceConsumePerformed(Resource r, int use, int overuse) {
        if (overuse > 0 && r.getType() == AIR ) {
            player.getResource(HEALTH).forceConsume(overuse);
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getTarget().getType() ) {
            case BUBBLE -> {
                getPlayer().getResource(AIR).recharge(100);
                ((GameMap) e.getSource() ).removeBlock( e.getTarget() );
            }
        }
    }

}
