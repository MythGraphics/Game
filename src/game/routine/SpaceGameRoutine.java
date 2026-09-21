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
import game.combat.CombatFactory;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.CREDIT;
import static game.resource.Resource.ResourceType.HEALTH;
import static graphic.io.BinaryIO.SPRITE;
import static graphic.io.BinaryIO.loadImage;

public class SpaceGameRoutine extends DefaultGameRoutine {

    private final Player player;

    private boolean victory = false;

    public SpaceGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
        this.player = initPlayer(gameFrame);
        gameFrame.textFrame.addCloseListener( () -> {
            if (victory) { gameFrame.dispose(); }
        });
    }

    private Player initPlayer(GameFrame gameFrame) {
        Resource health  = new Resource( "Gesundheit", HEALTH, 1000, 1000 );
        health.addResourceChangeListener(gameFrame);
        Resource credit  = new Resource( "Münzen", CREDIT, 1000*1000, 0 );
        credit.addResourceChangeListener(gameFrame);
        Player player = new Player(GameFrame.playerName, gameFrame.textFrame, health, credit);
        player.setPlayerAsMinion( CombatFactory.getDefaultSoldier( player.getHealth() ));
        player.setImg( loadImage( SPRITE+"player/man1.png" ));
        return player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}
