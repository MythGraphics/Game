/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import graphic.DirectionalImage;
import graphic.map.Block;
import graphic.map.GameMap;
import graphic.map.IsBlockType;
import graphic.map.IsCollisionHandler;
import java.awt.Dimension;
import java.awt.Point;

public class Projectile extends AutoMoveableTile {

    public final Block source;

    public Projectile(BlockTile source, Direction initialDirection,
                      int x, int y, IsBlockType bType,
                      int stepSize, Point maxPoint) {
        this( source, initialDirection, x, y, bType, stepSize, maxPoint, source.getProjectileImage() );
    }

    public Projectile(Block source, Direction initialDirection,
                      int x, int y, IsBlockType bType,
                      int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(initialDirection, x, y, bType, stepSize, maxPoint, imgset);
        this.source = source;
    }

    public Projectile(BlockTile source, Direction initialDirection,
                      Point pos, Dimension dim, IsBlockType bType,
                      int stepSize, Point maxPoint) {
        this( source, initialDirection, pos, dim, bType, stepSize, maxPoint, source.getProjectileImage() );
    }

    public Projectile(Block source, Direction initialDirection,
                      Point pos, Dimension dim, IsBlockType bType,
                      int stepSize, Point maxPoint, DirectionalImage imgset) {
        super(initialDirection, pos, dim, bType, stepSize, maxPoint, imgset);
        this.source = source;
    }

    public Block getSource() {
        return source;
    }

    @Override
    public boolean onCollision(GameMap source, Block initiator, IsCollisionHandler handler) {
        destroy(source);
        return super.onCollision(source, initiator, handler);
    }

}
