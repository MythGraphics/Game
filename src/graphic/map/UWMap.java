/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.Resource;
import static game.Resource.ResourceType.AIR;
import static graphic.map.BlockType.WATERLINE;
import java.awt.event.ActionEvent;

public abstract class UWMap extends GameMap {

    public final int resourceCost = 1;
    public final Resource air;

    private int uwlevel;

    public UWMap(String[] tileMap) {
        this( tileMap, new Resource( "Luft", AIR, 1000, 1000 ));
    }

    public UWMap(String[] tileMap, Resource air) {
        super(tileMap);
        this.air = air;
    }

    public Resource getUWResource() {
        return air;
    }

    public int getResourceCostPerSec() {
        if ( Math.random() <= 0.5 ) {
            return resourceCost;
        } else {
            return 0;
        }
    }

    public void consumeResource() {
        air.forceConsume( getResourceCostPerSec() );
    }

    public void rechargeResource() {
        air.recharge(resourceCost);
    }

    @Override
    // wird vom Timer ausgelöst
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if ( super.player.y > uwlevel ) {
            // Spieler ist unter Wasser
            consumeResource();
        } else {
            // Spieler ist auf dem oder über Wasser
            rechargeResource();
        }
    }

    @Override
    void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        super.loadTileMapChar(tileMapChar, x, y, tileSize); // SUPER muss zwingend zuerst aufgerufen werden
        BlockType tType = BlockType.getTileType(tileMapChar);
        switch (tType) {
            case WATERLINE -> uwlevel = y;
        }
    }

}
