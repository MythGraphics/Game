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

    public final IsBlock initiator, target;

    public CollisionEvent(GameMap source, IsBlock initiator, IsBlock target) {
        super(source);
        this.initiator  = initiator;
        this.target     = target;
    }

    public InteractionType getType() {
        return target.getBlockType().getInteractionType();
    }

    public IsBlock getInitiator() {
        return initiator;
    }

    public IsBlock getTarget() {
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
