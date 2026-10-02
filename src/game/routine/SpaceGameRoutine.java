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
import graphic.map.DefaultSpaceMap;

public class SpaceGameRoutine extends MartialGameRoutine {

    public SpaceGameRoutine(GameFrame gameFrame, DefaultSpaceMap map) {
        super(gameFrame, map);
        map.init( getPlayer().getHealth() );
    }

    @Override
    protected Player buildPlayer() {
        Player player = super.buildPlayer();
        player.setImage( loadImage( SPRITE + "player/man1.png" ));
        return player;
    }

}
