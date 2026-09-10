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
import graphic.HasImage;
import graphic.map.Block;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;

// ToDo: (optional) ProjectileType implementieren

public class Projectile extends AutoMoveableTile {

    public final Block source;

    public Projectile(Block source, Direction initialDirection,
                      int x, int y, int blockSize, IsBlockType bType,
                      Point maxPoint, HasImage[] imgset) {
        super(initialDirection, x, y, blockSize, bType, maxPoint, imgset);
        this.source = source;
    }

    public Projectile(Block source, Direction initialDirection,
                      Point pos, Dimension dim, int blockSize, IsBlockType bType,
                      Point maxPoint, HasImage[] imgset) {
        super(initialDirection, pos, dim, blockSize, bType, maxPoint, imgset);
        this.source = source;
    }

    public Block getSource() {
        return source;
    }

}
