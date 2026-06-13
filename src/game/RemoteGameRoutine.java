/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

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
