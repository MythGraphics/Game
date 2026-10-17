/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.GameMap;
import graphic.map.MapRecord;
import graphic.tile.IsMoveableTile;
import java.awt.event.KeyEvent;

public abstract class VectorMap extends GameMap {

    public final static float ROTATION_STEP = (float) (Math.PI/20.0);

    private VectorBlock player;

    public VectorMap(char[][] tileMap) {
        super(tileMap, 30);
    }

    public VectorMap(char[][] tileMap, MapRecord rec) {
        super(tileMap, rec);
    }

    @Override
    public IsMoveableTile getPlayer() {
        return player;
    }

    @Override
    public void movePlayer(KeyEvent evt) {
        if ( !isActive() ) {
            return;
        }

        switch ( evt.getKeyCode() ) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W, KeyEvent.VK_NUMPAD8 -> player.accelerate( 0.5f);
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S, KeyEvent.VK_NUMPAD2 -> player.accelerate(-0.5f);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_NUMPAD6 -> player.rotate( ROTATION_STEP);
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A, KeyEvent.VK_NUMPAD4 -> player.rotate(-ROTATION_STEP);
        }
        detectCollision(player);
    }

    @Override
    public void resetPlayerPosition() {
        // Implementierung über Collidable->onCollision möglich
        if (lastImpassableCollisionTarget != null) {
            VectorBlock.applyWallCollision(player, lastImpassableCollisionTarget);
            lastImpassableCollisionTarget = null;
        }
    }

}
