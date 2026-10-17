/*
 *
 */

package graphic.block;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public interface IsBlockType {

    char getMapChar();
    InteractionType getInteractionType();
    boolean isPassable();

}
