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

    public final CollisionType cType; // falls abweichend vom cType des tType
    public final Block block;

    // tType über Block, cType über tType
    public CollisionEvent(GameMap source, CollisionType cType, Block block) {
        super(source);
        this.cType = cType;
        this.block = block;
    }

    public CollisionType getCollisionType() {
        if ( cType == null ) {
            return block.getType().getCollisionType();
        }
        return cType;
    }

    public Block getBlock() {
        return block;
    }

}
