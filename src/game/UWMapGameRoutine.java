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

import graphic.CollisionEvent;
import graphic.map.UWMap;

public class UWMapGameRoutine extends LandMapGameRoutine {

    public UWMapGameRoutine(Player player) {
        super(player);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getBlock().getType() ) {
            case BUBBLE:
                super.player.getResource(1).recharge(100);
                ((UWMap) e.getSource() ).bubbles.remove( e.getBlock() );
                break;
        }
    }

}
