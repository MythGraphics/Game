/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.EventListener;

public interface CollisionActionListener extends EventListener {

    void collisionPerformed(CollisionEvent e);

}
