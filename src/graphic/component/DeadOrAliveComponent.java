/*
 *
 */

package graphic.component;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.resource.Resource;
import graphic.DeadOrAliveDataSet;
import graphic.map.OverlayUtil;
import graphic.tile.BlockTile;
import graphic.tile.HasOverlayResource;
import java.awt.Graphics2D;

public class DeadOrAliveComponent implements HasOverlayResource {

    private final DeadOrAliveDataSet data;
    private final Resource overlayResource;

    public DeadOrAliveComponent(DeadOrAliveDataSet data, Resource overlayResource) {
        this.data = data;
        this.overlayResource = overlayResource;
    }

    public boolean isAlive() {
        return data.isAlive();
    }

    public void setDead(BlockTile owner) {
        data.setDead();
        owner.change(data.getBlockType(), data);
    }

    public void setAlive(BlockTile owner) {
        data.setAlive();
        owner.change(data.getBlockType(), data);
    }

    @Override
    public Resource getOverlayResource() {
        return overlayResource;
    }

    public void draw(BlockTile owner, Graphics2D g2d, int offsetX, int offsetY) {
        owner.draw(g2d, offsetX, offsetY);
        OverlayUtil.drawResourceBar(g2d, offsetX, offsetY, this, owner);
    }

}
