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

import game.combat.CombatFactory;
import game.combat.CombatFrame;
import game.combat.Combatant;
import graphic.CollisionEvent;
import graphic.TextFrame;
import graphic.io.BinaryIO;
import graphic.io.TextIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;

public class SpaceMapGameRoutine extends GameRoutine {

    private final List<String> audioTrackList;
    private final JFrame frame;

    private Enemy enemy;

    public SpaceMapGameRoutine(Player player, JFrame frame) {
        super(player);
        this.frame = frame;
        audioTrackList = TextIO.TEXTIO.loadAudioTrackList("SpaceMapAudioTrackList.txt");
        try {
            init();
        } catch (IOException e) {
            System.err.println("Initialisieren der Routine fehlgeschlagen - Abbruch!");
            System.exit(255);
            return;
        }
        showProlog();
    }

    private void init() throws IOException {
        enemy = GameObjectLoader.loadNextEnemy();
        player.setPlayerAsMinion( CombatFactory.getDefaultSpaceMarine() );
    }

    @Override
    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

    public final void showProlog() {
        String prolog = TextIO.TEXTIO.loadProlog();
        if (prolog == null) {
            return;
        }
        BufferedImage bg = BinaryIO.BINARYIO.loadImage("bg/interior_of_a_spaceship_by_parker_west.jpg");
        TextFrame textFrame = new TextFrame( new Dimension( 800, 600 ));
        textFrame.getTexter().setFontColor(Color.WHITE);
        textFrame.getTexter().setTypeOverImage(true);
        textFrame.show("Prolog", prolog, bg);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getBlock().getType() ) {
            case ENEMY -> {
/*              player.getDialogOutputListener().show( new Message(
                    "Der Zerg erschreckt sich bei Eurem Anblick zu Tode.", player
                ));
 */
                if ( !enemy.getMinion().isAlive() ) {
                    enemy.getMinion().resurrect();
                    enemy.getMinion().setLevel( player.getPlayerAsMinion().getLevel() );
                };
                CombatFrame cFrame = new CombatFrame( frame, player.getPlayerAsMinion(), enemy.getMinion() );
                cFrame.setVisible(true);
                if ( !enemy.getMinion().isAlive() ) {
                    e.block.dead();
                }
            }
        }
    }

}
