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

import static graphic.Direction.*;
import static graphic.map.DefaultBlockType.PLAYER;
import graphic.tile.IsBlockTile;
import graphic.tile.IsMoveableTile;
import java.awt.event.KeyEvent;

public abstract class SimpleMap extends GameMap {

    private IsMoveableTile player;

    public SimpleMap(char[][] tileMap, MapRecord rec) {
        super(tileMap, rec);
    }

    public SimpleMap(char[][] tileMap) {
        super(tileMap, 15);
    }

    @Override
    public IsMoveableTile getPlayer() {
        return player;
    }

    @Override
    protected void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        IsBlockType bType = DefaultBlockType.getByChar(tileMapChar);
        IsBlockTile tile  = getBlockTile(x, y, bType);

        switch (bType) {
            case PLAYER -> {
                if (tile instanceof IsMoveableTile moveable) {
                    this.player = moveable;
                } else {
                    System.err.println(
                        "Player vom Typ " +
                        ( tile == null ? "NULL" : tile.getClass().getName() ) +
                        " inkompatibel: muss IsMoveableTile sein."
                    );
                }
                savePlayerPosition(x, y);
            }
            default -> super.loadTileMapChar(tileMapChar, x, y, tileSize);
        }
    }

    @Override
    public void movePlayer(KeyEvent evt) {
        if ( !isActive() ) {
            return;
        }

        saveCurrentPlayerPosition();
        switch ( evt.getKeyCode() ) {
            case KeyEvent.VK_W, KeyEvent.VK_NUMPAD8 -> player.move(UP);
            case KeyEvent.VK_S, KeyEvent.VK_NUMPAD2 -> player.move(DOWN);
            case KeyEvent.VK_D, KeyEvent.VK_NUMPAD6 -> player.move(RIGHT);
            case KeyEvent.VK_A, KeyEvent.VK_NUMPAD4 -> player.move(LEFT);

            case KeyEvent.VK_R, KeyEvent.VK_NUMPAD3 -> player.step(UP);
            case KeyEvent.VK_F, KeyEvent.VK_NUMPAD1 -> player.step(DOWN);
            case KeyEvent.VK_E, KeyEvent.VK_NUMPAD9 -> player.step(RIGHT);
            case KeyEvent.VK_Q, KeyEvent.VK_NUMPAD7 -> player.step(LEFT);

            case KeyEvent.VK_UP                     -> player.tilt(UP);
            case KeyEvent.VK_DOWN                   -> player.tilt(DOWN);
            case KeyEvent.VK_RIGHT                  -> player.tilt(RIGHT);
            case KeyEvent.VK_LEFT                   -> player.tilt(LEFT);
        }
        detectCollision(player);
    }

    public static void printKeyBindings() {
        System.out.println("wsad / 8246 (numpad): move player");
        System.out.println("qe / 79 (numpad): move player sideways without changing view direction");
        System.out.println("rf / 31 (numpad): move player up/down without changing view direction");
        System.out.println("arrow keys: tilt player: change players view direction without moving");
    }

}
