/*
 *
 */

package game.item;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.Player;

public interface Usable extends IsItem {

    boolean use(Player player);

}
