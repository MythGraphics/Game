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

@FunctionalInterface
public interface Moveable {

    void move(Block owner);

}
