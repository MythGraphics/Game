/*
 *
 */

package game.routine;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.Npc;
import static graphic.map.BlockType.NPC;
import graphic.map.CollisionEvent;
import static graphic.map.CollisionType.INTERACTIVE;
import graphic.texter.DialogOutputListener;

public abstract class RPGRoutine extends GameRoutine {

    public RPGRoutine(DialogOutputListener dialogListener) {
        super(dialogListener);
    }

    public abstract Npc getNpc();

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getType() ) {
            case INTERACTIVE -> {
                switch ( e.getTarget().getType() ) {
                    case NPC -> {
                        dialogListener.show( getNpc() );
                        if ( getPlayer().hasActiveQuest() ) {
                            getPlayer().deliverQuest();
                            dialogListener.show( getPlayer().getQuest() );
                        } else if ( getNpc().hasQuest() ) {
                            dialogListener.show( getNpc().getQuest() );
                            getPlayer().acceptQuest( getNpc().getQuest() );
                        }
                    }
                }
            }
        }
    }

}
