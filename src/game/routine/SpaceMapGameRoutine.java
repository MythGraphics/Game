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

import game.Enemy;
import game.GameFrame;
import game.Player;
import game.combat.Ammo;
import game.combat.AmmoType;
import game.combat.CombatFactory;
import static game.combat.CombatFactory.Group.ZERG;
import game.combat.Combatant;
import game.item.LootManager;
import game.resource.Resource;
import static game.resource.Resource.ResourceType.*;
import graphic.DeadOrAlive;
import graphic.io.BinaryIO;
import static graphic.io.BinaryIO.*;
import graphic.io.TextIO;
import graphic.map.CollisionEvent;
import graphic.texter.Message;
import graphic.texter.TextFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpaceMapGameRoutine extends MartialGameRoutine {

    public final static String PROLOG_BG = RESOURCE+"bg/interior_of_a_spaceship_by_parker_west.jpg";

    private final BufferedImage textFrameBG;
    private final Player player;

    private Enemy enemy;
    private int enemies = 0;
    private boolean victory = false;

    public SpaceMapGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
        setAudioTrackList("SpaceMapAudioTrackList.txt");
        this.player = initPlayer(gameFrame);
        textFrameBG = BinaryIO.loadImage(PROLOG_BG);
        enemies = gameFrame.getCurrentMap().getEnemyCount();
        try {
            enemy = loader.loadNextEnemy();
        } catch (IOException | NullPointerException e) {
            System.err.println( "Initialisieren der Spiel-Routine fehlgeschlagen - Abbruch!" );
            System.err.println( "Ursache: " + e.getMessage() );
            System.exit(255);
            return;
        }
        gameFrame.textFrame.addCloseListener( () -> {
            if (victory) { gameFrame.dispose(); }
        });
        showProlog();
    }

    private Player initPlayer(GameFrame gameFrame) {
        Resource health  = new Resource( "Gesundheit", HEALTH, 1000, 1000 );
        health.addResourceChangeListener(gameFrame);
        Resource credit  = new Resource( "Münzen", CREDIT, 1000*1000, 0 );
        credit.addResourceChangeListener(gameFrame);
        Resource air     = new Resource( "Sauerstoff", AIR, 1000, 1000 );
        air.addResourceChangeListener(gameFrame);
        Resource stamina = new Resource( "Ausdauer", STAMINA, 100, 100 );
        stamina.addResourceChangeListener(gameFrame);
        Player player = new Player(GameFrame.playerName, gameFrame.textFrame, health, credit, air, stamina);
        player.setPlayerAsMinion( CombatFactory.getDefaultSoldier( player.getHealth() ));
        player.setImg( loadImage( SPRITE+"player/man1.png" ));
        return player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void enemyDead(Combatant enemyMinion, DeadOrAlive target) {
        super.enemyDead(enemyMinion, target);
        loot(enemyMinion);
        --enemies;
        if (enemies == 0) {
            showEpilog();
        }
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
        showMessageScreen("Prolog", prolog, textFrameBG);
    }

    public void showEpilog() {
        victory = true;
        String epilog = TextIO.loadEpilog( getClass() );
        showMessageScreen("Epilog", epilog, textFrameBG);
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
        gameFrame.textFrame.show( new Message(
            "Da liegt doch was!\n" + loot.toString(), player
        ));
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        super.collisionPerformed(e);
        switch( e.getTarget().getType() ) {
            case ENVIRONMENT_A -> {
                player.getDialogOutputListener().show( new Message(
                    "Warum liegt hier überhaupt Stroh rum?", player
                ));
            }
            case EXIT -> {
                showEpilog();
            }
        }
    }

}
