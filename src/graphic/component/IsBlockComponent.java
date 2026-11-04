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

import graphic.block.Block;

public interface IsBlockComponent {

    default void updateOnEvent(Block owner) {}
    default void updateOnFrame(Block owner) {}

}
