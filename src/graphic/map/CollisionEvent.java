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

import java.util.EventObject;

public class CollisionEvent extends EventObject {

    public final Block target, collider;

    public CollisionEvent(GameMap source, Block target, Block collider) {
        super(source);
        this.target     = target;
        this.collider   = collider;
    }

    public InteractionType getType() {
        return target.getType().getInteractionType();
    }

    public Block getCollider() {
        return collider;
    }

    public Block getTarget() {
        return target;
    }

}
