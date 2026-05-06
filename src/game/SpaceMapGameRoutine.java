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

import game.combat.*;
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

    private LootManager lootManager = new LootManager();

    public SpaceMapGameRoutine(Player player, JFrame frame) {
        super(player, null, "SpaceMapAudioTrackList.txt", frame);
        player.setPlayerAsMinion( CombatFactory.getDefaultSpaceMarine( player.getHealth() ));
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
        // ToDo hier Properties laden
    }

    public final void showProlog() {
        String prolog = TextIO.loadProlog( getClass() );
        if (prolog == null) {
            return;
        }
        BufferedImage bg = BinaryIO.loadImage("bg/interior_of_a_spaceship_by_parker_west.jpg");
        TextFrame textFrame = new TextFrame( new Dimension( 800, 600 ));
        textFrame.getTexter().setFontColor(Color.WHITE);
        textFrame.getTexter().setTypeOverImage(true);
        textFrame.show("Prolog", prolog, bg);
    }

    @Override
    void loot(Combatant enemy) {
        Ammo loot = lootManager.createAmmo(enemy, AmmoType.PROJECTILE);
        player.getInventory().add(loot);
        dialogListener.show( new Message(
            "Da liegt doch was!\n" + loot.toString(), player
        ));
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getBlock().getType() ) {
            case ENVIRONMENT_A -> {
                player.getDialogOutputListener().show( new Message(
                    "Warum liegt hier eigentlich Stroh rum?", player
                ));
            }
        }
    }

}
