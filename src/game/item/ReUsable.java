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

import game.HasUIImage;
import game.Player;

public interface ReUsable extends Usable, HasUIImage {

    ReUsable remove(Player player);

}
