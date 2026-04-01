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

import graphic.map.UWMap;

public class UWMapRoutine {

    final Player player;
    final int resourceCost = 1;

    public UWMapRoutine(Player player) {
        this.player = player;
    }

    public int getResourceCostPerSec() {
        double ref = 1.0/UWMap.FPS;
        if ( Math.random() <= ref ) {
            return resourceCost;
        }
        return 0;
    }

    public void useResource() {
        if ( !player.getResource(1).use( getResourceCostPerSec() )) {
            player.takeDamage( getResourceCostPerSec() );
        }
    }

    public void rechargeResource() {
        player.getResource(1).recharge(resourceCost);
    }

    public Player getPlayer() {
        return player;
    }

}
