/*
 *
 */

package graphic.MissileLauncher;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.DirectionalImage;

public class FiniteMissileLauncher extends MissileLauncher {

    private final int max;
    private int quantity = 0;

    public FiniteMissileLauncher(DirectionalImage missileImage, int quantity, int max) {
        super(missileImage);
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

}