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
    public final Block block;

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
