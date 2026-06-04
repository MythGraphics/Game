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

import static game.Resource.ResourceType.*;
import game.combat.Ammo;
import game.combat.AmmoType;
import game.combat.CombatFactory;
import static game.combat.CombatFactory.Group.ZERG;
import game.combat.Combatant;
import game.item.LootManager;
import graphic.CollisionEvent;
import graphic.DeadOrAlive;
import graphic.TextFrame;
import graphic.io.BinaryIO;
import static graphic.io.BinaryIO.*;
import graphic.io.TextIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpaceMapGameRoutine extends MartialGameRoutine {

    public final static String PROLOG_BG = RESOURCE+"bg/interior_of_a_spaceship_by_parker_west.jpg";

    private Player player;
    private Enemy enemy;

    public SpaceMapGameRoutine(GameFrame frame) {
        super(null, frame);
        setAudioTrackList("SpaceMapAudioTrackList.txt");
        initPlayer(frame);
        try {
            enemy = loader.loadNextEnemy();
        } catch (IOException | NullPointerException e) {
            System.err.println( "Initialisieren der Spiel-Routine fehlgeschlagen - Abbruch!" );
            System.err.println( "Ursache: " + e.getMessage() );
            System.exit(255);
            return;
        }
        showProlog();
    }

    private void initPlayer(GameFrame frame) {
        Resource health  = new Resource( "Gesundheit", HEALTH, 1000, 1000 );
        health.addResourceChangeListener(frame);
        Resource credit  = new Resource( "Münzen", CREDIT, 1000*1000, 0 );
        credit.addResourceChangeListener(frame);
        Resource air     = new Resource( "Sauerstoff", AIR, 1000, 1000 );
        air.addResourceChangeListener(frame);
        Resource stamina = new Resource( "Ausdauer", STAMINA, 100, 100 );
        stamina.addResourceChangeListener(frame);
        player = new Player(GameFrame.playerName, frame.textFrame, health, credit, air, stamina);
        player.addItemActionListener(frame);
        player.setPlayerAsMinion( CombatFactory.getDefaultSoldier( player.getHealth() ));
        player.setImg( loadImage( SPRITE+"player/man1.png" ));
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void enemyDead(Combatant enemyMinion, DeadOrAlive target) {
        super.enemyDead(enemyMinion, target);
        loot(enemyMinion);
    }

    @Override
    public Enemy getEnemy() {
        if ( enemy.getMinion().isAlive() ) {
            return enemy;
        }
        enemy.setMinion( CombatFactory.createRandomEnemy( ZERG ));
        int l1 = player.getPlayerAsMinion().getLevel();
        int l2 = enemy.getMinion().getLevel();
        enemy.getMinion().setLevel((byte) ( Math.max( l1, l2 )+1 ));
        return enemy;
    }

    public final void showProlog() {
        String prolog = TextIO.loadProlog( getClass() );
        BufferedImage bg = BinaryIO.loadImage(PROLOG_BG);
        showMessageScreen("Prolog", prolog, bg);
    }

    public void showEpilog() {
        String epilog = TextIO.loadEpilog( getClass() );
        BufferedImage bg = BinaryIO.loadImage(PROLOG_BG);
        showMessageScreen("Epilog", epilog, bg);
    }

    public void showMessageScreen(String title, String text, BufferedImage bg) {
        TextFrame textFrame = new TextFrame( new Dimension( 800, 600 ));
        textFrame.getTexter().setFontColor(Color.WHITE);
        textFrame.getTexter().setTypeOverImage(true);
        textFrame.show(title, text, bg);
    }

    private void loot(Combatant enemy) {
        Ammo loot = LootManager.createAmmo(enemy, AmmoType.PROJECTILE);
        player.getInventory().add(loot);
        dialogListener.show( new Message(
            "Da liegt doch was!\n" + loot.toString(), player
        ));
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getTarget().getType() ) {
            case ENVIRONMENT_A -> {
                player.getDialogOutputListener().show( new Message(
                    "Warum liegt hier eigentlich Stroh rum?", player
                ));
            }
        }
    }

}
