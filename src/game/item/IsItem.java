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

import game.HasName;

public interface IsItem extends HasName, ItemActionListener {

    void setDescription(String description);
    String getDescription();

}
