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
import static graphic.io.BinaryIO.SPRITE;
import static graphic.io.BinaryIO.loadImage;

public class SpaceGameRoutine extends DefaultGameRoutine {

    public SpaceGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
    }

    @Override
    protected Player createPlayer() {
        Player player = super.createPlayer();
        player.setImage( loadImage( SPRITE + "player/man1.png" ));
        return player;
    }

}
