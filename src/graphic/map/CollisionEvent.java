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

    public final Block initiator, target;

    public CollisionEvent(GameMap source, Block initiator, Block target) {
        super(source);
        this.initiator  = initiator;
        this.target     = target;
    }

    public InteractionType getType() {
        return target.getBlockType().getInteractionType();
    }

    public Block getInitiator() {
        return initiator;
    }

    public Block getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return  getClass().getName() +
                " from source "  + source +
                " by initiator " + initiator +
                " with target "  + target +
                ", type "        + getType() +
                "."
        ;
    }

}
