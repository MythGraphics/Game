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

import graphic.Moveable;
import graphic.block.Collidable;

public interface IsMissile extends Collidable, Moveable {

    MissileLauncher getSource();

}
