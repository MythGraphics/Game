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

import game.combat.AmmoType;
import game.combat.CombatFactory;
import game.combat.CombatFrame;
import game.combat.Combatant;
import game.item.LootManager;
import graphic.CollisionEvent;
import graphic.TextFrame;
import graphic.io.BinaryIO;
import graphic.io.TextIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;

public class SpaceMapGameRoutine extends GameRoutine {

    private final JFrame frame;

    private Enemy enemy;
    private LootManager lootManager = new LootManager();

    public SpaceMapGameRoutine(Player player, JFrame frame) {
        super( player, null, "SpaceMapAudioTrackList.txt" );
        this.frame = frame;
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
        player.setPlayerAsMinion( CombatFactory.getDefaultSpaceMarine( player.getHealth() ));
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

    private void loot(Combatant enemy) {
        player.getInventory().add( lootManager.getAmmo( enemy, AmmoType.PROJECTILE ));
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getBlock().getType() ) {
            case ENEMY -> {
                Combatant enemyMinion  = this.enemy.getMinion();
                Combatant playerMinion = this.player.getPlayerAsMinion();
                if ( !enemyMinion.isAlive() ) {
                    enemyMinion.resurrect();
                    enemyMinion.setLevel( playerMinion.getLevel() );
                }
                CombatFrame cFrame = new CombatFrame(frame, playerMinion, enemyMinion);
                cFrame.setVisible(true);
                if ( !enemyMinion.isAlive() ) {
                    e.block.dead();
                }
                if ( player.isAlive() ) {
                    loot(enemyMinion);
                }
            }
            case ENVIRONMENT_A -> {
                player.getDialogOutputListener().show( new Message(
                    "Warum liegt hier eigentlich Stroh rum?", player
                ));
            }
        }
    }

}
