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

public interface Trader {

    public Inventory getInventory();
    public void addCredits(int credit);
    public int getCredits();

}
