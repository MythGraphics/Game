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
import graphic.AutoMoveable;
import static graphic.block.InteractionType.ENEMY;
import static graphic.block.InteractionType.PLAYER;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import graphic.tile.IsDeadOrAliveTile;

public class MartialGameRoutine extends DefaultGameRoutine {

    public MartialGameRoutine(GameFrame gameFrame, GameMap map) {
        super(gameFrame, map);
    }

    public void attack(IsDeadOrAliveTile doat, int quantity) {
        Resource r = doat.getOverlayResource();
        r.forceConsume(quantity);
        if ( r.getValue() <= 0 ) {
            System.out.println( doat.getBlockType() + " destroyed.");
            doat.setDead();
            if (doat instanceof AutoMoveable auto) {
                auto.stopMovement();
            }
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        if ( e.initiator instanceof IsDeadOrAliveTile doat ) {
            if ( !doat.isAlive() ) {
                return;
            }
            System.out.println( e.initiator.getBlockType() + " hit " + e.target.getBlockType() );
            attack(doat, 10);
        }
//      System.out.println( e.initiator.getClass().getName() + " --> " + e.target.getClass().getName() ); // debug
        switch( e.getType() ) {
            case PLAYER -> {
                if ( e.target instanceof IsDeadOrAliveTile doat ) {
                    System.out.println( "Player hit by " + e.getInitiator().getBlockType() );
                    attack(doat, 10);
                }
            }
            case ENEMY -> {
                if ( e.target instanceof IsDeadOrAliveTile doat ) {
                    System.out.println( "Enemy hit by " + e.getInitiator().getBlockType() );
                    attack(doat, 10);
                }
            }
            default -> super.collisionPerformed(e);
        }
    }

}
