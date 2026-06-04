/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.Block;
import graphic.map.GameMap;
import java.util.EventObject;

public class CollisionEvent extends EventObject {

    public final CollisionType cType; // im Grunde redundant, da über Block ( getType().getCollisionType() ) zu erhalten
    public final Block target, collider;

    public CollisionEvent(GameMap source, CollisionType cType, Block target, Block collider) {
        super(source);
        this.cType      = cType;
        this.target     = target;
        this.collider   = collider;
    }

    public CollisionType getCollisionType() {
        if ( cType == null ) {
            return target.getType().getCollisionType();
        }
        return cType;
    }

    public Block getCollider() {
        return collider;
    }

    public Block getTarget() {
        return target;
    }

}
