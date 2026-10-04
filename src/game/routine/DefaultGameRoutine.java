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

import game.GameFrame;
import game.Player;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.CREDIT;
import graphic.Moveable;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import static graphic.map.InteractionType.EXIT;
import static graphic.map.InteractionType.PORTAL;
import graphic.texter.DialogOutputListener;
import java.awt.Point;

public class DefaultGameRoutine extends GameRoutine {

    public final GameFrame gameFrame;
    public final GameMap map;
    public final Player player;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public DefaultGameRoutine(GameFrame gameFrame, GameMap map) {
        this.gameFrame  = gameFrame;
        this.map        = map;
        this.player     = buildPlayer();
        initMap();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public GameMap getMap() {
        return map;
    }

    public void initMap() {
        if ( !map.isInitialized() ) {
            map.init();
        }
    }

    /**
     * Overwrite to implement an event-dependent dialogListener besides the initial one.
     * @param e CollisionEvent
     * @return DialogOutputListener
     */
    @Override
    public DialogOutputListener getDialogListener(CollisionEvent e) {
        return gameFrame.textFrame;
    }

    protected Player buildPlayer() {
        // Resourcen health & credit essentiell für das Player-Objekt
        Resource health = Player.DEFAULT_HEALTH;
        Resource credit = new Resource("Münzen", CREDIT, 1000*1000, 0);
        health.addResourceChangeListener(gameFrame);
        credit.addResourceChangeListener(gameFrame);
        return new Player(GameFrame.playerName, getDialogListener(null), health, credit);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getType() ) {
            case EXIT   -> (( GameMap ) e.getSource() ).deactivate();
            case PORTAL -> {
                Point targetPoint = new Point( e.getTarget().getX(), e.getTarget().getY() );
                portals.addIfAbsent(targetPoint);
                if ( portals.size() < 2 ) {
                    // Wenn weniger als 2 Portale bekannt sind, bleibt der Spieler wo er ist.
                    return;
                }
                (( GameMap ) e.getSource() ).moveThroughPortal(( Moveable ) e.initiator, portals.getNext() );
            }

            default -> super.collisionPerformed(e);
        }
    }

}
