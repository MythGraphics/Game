/*
 *
 */

package graphic.missileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.Direction;
import graphic.HasImage;
import graphic.block.IsBlock;
import graphic.map.GameMap;
import graphic.vectorMap.VectorBlock;

public class FiniteMissileLauncher extends MissileLauncher {

    private final int max;
    private int quantity = 0;

    public FiniteMissileLauncher(IsBlock owner, HasImage missileImage, int quantity, int max) {
        super(owner, missileImage);
        this.quantity = quantity;
        this.max      = max;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMaxQuantity() {
        return max;
    }

    public void recharge() {
        quantity = max;
    }

    public void recharge(int amount) {
        quantity = Math.min(amount, max);
    }

    @Override
    public boolean hasMissile() {
        return quantity > 0;
    }

    @Override
    public boolean fireMissile(GameMap map, IsBlock source, Direction d) {
        if ( super.fireMissile( map, source, d )) {
            --quantity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean fireMissile(GameMap map, VectorBlock source, float angle) {
        if ( super.fireMissile( map, source, angle )) {
            --quantity;
            return true;
        } else {
            return false;
        }
    }

}