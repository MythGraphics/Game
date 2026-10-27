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

import graphic.block.Collidable;
import graphic.block.DefaultBlockType;
import static graphic.block.DefaultBlockType.PLAYER;
import graphic.block.IsBlockType;
import graphic.block.IsMoveableBlock;
import graphic.map.GameMap;
import graphic.map.MapRecord;
import graphic.tile.IsBlockTile;
import java.awt.event.KeyEvent;

public abstract class VectorMap extends GameMap {

    public final static float ROTATION_STEP = (float) Math.PI/12.0f;

    private VectorBlock player;

    public VectorMap(char[][] tileMap) {
        super(tileMap, 30);
    }

    public VectorMap(char[][] tileMap, MapRecord rec) {
        super(tileMap, rec);
    }

    @Override
    public VectorBlock getPlayer() {
        return player;
    }

    @Override
    protected void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        IsBlockType bType = DefaultBlockType.getByChar(tileMapChar);
        IsBlockTile tile  = getBlockTile(x, y, bType);

        switch (bType) {
            case PLAYER -> {
                try {
                    if (tile instanceof VectorBlock vb) {
                        this.player = vb;
                    } else {
                        System.err.println(
                            "Player vom Typ " +
                            ( tile == null ? "NULL" : tile.getClass().getName() ) +
                            " inkompatibel: muss " +
                            getClass().getDeclaredField("player").getType().getName() +
                            " sein."
                        );
                    }
                } catch (NoSuchFieldException ignore) {}
            }
            default -> super.loadTileMapChar(tileMapChar, x, y, tileSize);
        }
    }

    @Override
    protected void move() {
        super.move();
        player.move();
        detectCollision(player);
    }

    @Override
    public void moveThroughPortal(IsMoveableBlock moveableBlock, int x, int y) {
        moveableBlock.setPos(x, y);
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
    }

    public static void printKeyBindings() {
        System.out.println("w / 8 (numpad) / Arrow-Key UP:    thrust");
        System.out.println("s / 2 (numpad) / Arrow-Key DOWN:  reverse thrust");
        System.out.println("a / 4 (numpad) / Arrow-Key LEFT:  rotate left");
        System.out.println("d / 6 (numpad) / Arrow-Key RIGHT: rotate right");
    }

    @Override
    public boolean isOutOfMap(Collidable collider) {
        boolean collision = super.isOutOfMap(collider);
        if (collision) {
            if (collider instanceof VectorBlock vb) {
                System.err.println(collider + " is out of map: stopping movement and resetting position ...");
                vb.stopMovement();
                vb.px = Math.clamp(vb.px, DEFAULT_TILE_SIZE, boardWidth  - DEFAULT_TILE_SIZE);
                vb.py = Math.clamp(vb.py, DEFAULT_TILE_SIZE, boardHeight - DEFAULT_TILE_SIZE);
            }
        }
        return collision;
    }

    @Override
    public void resetPlayerPosition() {
        // Implementierung über Collidable->onCollision möglich
        if (lastImpassableCollisionTarget != null) {
            IsVectorBlock.applyWallCollision(player, lastImpassableCollisionTarget);
            lastImpassableCollisionTarget = null;
        }
    }

}
