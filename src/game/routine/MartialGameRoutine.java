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
import static graphic.map.InteractionType.PLAYER;
import graphic.tile.IsDeadOrAliveTile;

public class MartialGameRoutine extends DefaultGameRoutine {

    public MartialGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getType() ) {
            case PLAYER -> { // Missile -> Player
                if ( e.getTarget() instanceof IsDeadOrAliveTile doa ) {
                    Resource r = doa.getOverlayResource();
                    System.out.println( "Player hit by " + e.getInitiator().getBlockType() );
                    r.forceConsume(10);
                    if ( r.getValue() <= 0 ) {
                        System.out.println("Player destroyed.");
                        doa.setDead();
                    }
                }
            }
            case ENEMY -> { // Missile -> Enemy
                if ( e.getTarget() instanceof IsDeadOrAliveTile doa ) {
                    Resource r = doa.getOverlayResource();
                    System.out.println( "Enemy hit by " + e.getInitiator().getBlockType() );
                    r.forceConsume(10);
                    if ( r.getValue() <= 0 ) {
                        System.out.println("Enemy destroyed.");
                        doa.setDead();
                    }
                }
            }
            default -> super.collisionPerformed(e);
        }
    }

}
