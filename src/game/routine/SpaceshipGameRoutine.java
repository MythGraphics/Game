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
import graphic.io.BinaryIO;
import static graphic.io.BinaryIO.*;
import graphic.io.TextIO;
import graphic.map.CollisionEvent;
import static graphic.map.DefaultBlockType.ENVIRONMENT0;
import static graphic.map.DefaultBlockType.EXIT;
import graphic.texter.Message;
import graphic.texter.TextFrame;
import graphic.tile.BlockTile;
import graphic.tile.DeadOrAliveTile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpaceshipGameRoutine extends MartialGameRoutine {

    public final static String PROLOG_BG = RESOURCE+"bg/interior_of_a_spaceship_by_parker_west.jpg";

    private final BufferedImage textFrameBG;

    private Enemy enemy;
    private int enemies = 0;
    private boolean victory = false;

    public SpaceshipGameRoutine(GameFrame gameFrame) {
        super(gameFrame);
        setAudioTrackList("SpaceshipAudioTrackList.txt");
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

    protected Player createPlayer(GameFrame gameFrame) {
        Player player = super.createPlayer();
        player.setPlayerAsMinion( CombatFactory.getDefaultSoldier( player.getHealth() ));
        player.setImage( loadImage( SPRITE+"player/man1.png" ));
        return player;
    }

    @Override
    public void enemyDead(Combatant enemyMinion, DeadOrAliveTile tile) {
        super.enemyDead(enemyMinion, tile);
        loot(enemyMinion);
        --enemies;
        if (enemies == 0) {
            showEpilog();
        }
    }

    @Override
    public void playerDead(Combatant player, BlockTile tile) {
        System.err.println("Tot des Spielers aktuell noch nicht implementiert.");
        // ToDo implementieren
    }

    @Override
    public Enemy getEnemy() {
        if ( enemy.getMinion().isAlive() ) {
            return enemy;
        }
        enemy.setMinion( CombatFactory.createRandomEnemy( ZERG ));
        int l1 = getPlayer().getPlayerAsMinion().getLevel();
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
        getPlayer().getInventory().add(loot);
        gameFrame.textFrame.show( new Message(
            "Da liegt doch was!\n" + loot.toString(), getPlayer()
        ));
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getTarget().getBlockType() ) {
            case ENVIRONMENT0 -> {
                getPlayer().getDialogOutputListener().show( new Message(
                    "Warum liegt hier überhaupt Stroh rum?", getPlayer()
                ));
            }
            case EXIT -> {
                showEpilog();
            }
            default -> {
                super.collisionPerformed(e);
            }
        }
    }

}
