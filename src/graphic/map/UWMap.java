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

import game.resource.ConsumeResource;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.AIR;
import static graphic.map.DefaultBlockType.WATERLINE;
import java.awt.event.ActionEvent;

public abstract class UWMap extends GameMap implements ConsumeResource {

    public final int resourceCost = 1;
    public final Resource resource;

    private int uwlevel;

    public UWMap(char[][] tileMap) {
        this( tileMap, new Resource( "Luft", AIR, 1000, 1000 ));
    }

    public UWMap(char[][] tileMap, Resource resource) {
        super(tileMap);
        this.resource = resource;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    public int getResourceCostPerSec() {
        if ( Math.random() <= 0.5 ) {
            return resourceCost;
        } else {
            return 0;
        }
    }

    public void consumeResource() {
        resource.forceConsume( getResourceCostPerSec() );
    }

    public void rechargeResource() {
        resource.recharge(resourceCost);
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
        DefaultBlockType tile = DefaultBlockType.getByChar(tileMapChar);
        switch (tile) {
            case WATERLINE -> uwlevel = y;
        }
    }

}
