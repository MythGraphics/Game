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

public class RemoteGameRoutine extends GameRoutine {

    final Player player;

    public RemoteGameRoutine(GameFrame frame) {
        super(frame.textFrame);
        this.player = new Player(GameFrame.playerName, frame.textFrame);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}
