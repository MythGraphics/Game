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
import static graphic.block.InteractionType.*;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import graphic.missileLauncher.IsMissile;
import graphic.tile.IsDeadOrAliveTile;

public class MartialGameRoutine extends DefaultGameRoutine {

    public MartialGameRoutine(GameFrame gameFrame, GameMap map) {
        super(gameFrame, map);
    }

    public void attack(IsDeadOrAliveTile doat, int quantity, GameMap map) {
        if ( !doat.isAlive() ) {
            return; // Tote mögen in Frieden ruhen
        }
        Resource r = doat.getOverlayResource();
        r.forceConsume(quantity);
        if ( r.getValue() <= 0 ) {
            System.out.println( doat.getBlockType() + " destroyed.");
            doat.setDead();
            map.disable(doat);
        }
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
//      System.out.println( e.initiator.getClass().getName() + " --> " + e.target.getClass().getName() ); // debug
        switch( e.getType() ) {
            case CORPSE -> { return; }
            case PORTAL -> { return; }
            case PLAYER -> {
                if (e.target instanceof IsDeadOrAliveTile doat) {
                    System.out.println( "Player hit by " + e.getInitiator().getBlockType() );
                    attack(doat, 10, e.map);
                }
            }
            case ENEMY -> {
                if (e.target instanceof IsDeadOrAliveTile doat) {
                    System.out.println( "Enemy hit by " + e.getInitiator().getBlockType() );
                    attack(doat, 10, e.map);
                }
            }
            default -> super.collisionPerformed(e);
        }

        if (e.initiator instanceof IsDeadOrAliveTile doat && !(e.target instanceof IsMissile)) {
            System.out.println( e.initiator.getBlockType() + " hit " + e.target.getBlockType() );
            attack(doat, 10, e.map);
        }
    }

}
