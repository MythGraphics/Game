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
import game.resource.Resource;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import static graphic.map.InteractionType.PLAYER;
import graphic.tile.IsDeadOrAliveTile;

public class MartialGameRoutine extends DefaultGameRoutine {

    public MartialGameRoutine(GameFrame gameFrame, GameMap map) {
        super(gameFrame, map);
    }

    public void consume(IsDeadOrAliveTile doa, int quantity) {
        Resource r = doa.getOverlayResource();
        r.forceConsume(quantity);
        if ( r.getValue() <= 0 ) {
            System.out.println( doa.getBlockType() + " destroyed.");
            doa.setDead();
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getType() ) {
            case PLAYER -> {
                if ( e.getTarget() instanceof IsDeadOrAliveTile doa ) {
                    System.out.println( "Player hit by " + e.getInitiator().getBlockType() );
                    consume(doa, 10);
                    if ( e.getInitiator() instanceof IsDeadOrAliveTile doa2 ) {
                        consume(doa2, 10);
                    }
                }
            }
            case ENEMY -> {
                if ( e.getTarget() instanceof IsDeadOrAliveTile doa ) {
                    System.out.println( "Enemy hit by " + e.getInitiator().getBlockType() );
                    consume(doa, 10);
                    if ( e.getInitiator() instanceof IsDeadOrAliveTile doa2 ) {
                        consume(doa2, 10);
                    }
                }
            }
            default -> super.collisionPerformed(e);
        }
    }

}
