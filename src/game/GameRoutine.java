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

import game.combat.CombatFrame;
import game.combat.Combatant;
import graphic.CollisionActionListener;
import graphic.CollisionEvent;
import graphic.io.TextIO;
import graphic.map.BlockType;
import static graphic.map.BlockType.ENEMY;
import graphic.map.GameMap;
import java.awt.Point;
import java.util.*;
import javax.swing.JFrame;
import util.CycleList;

public abstract class GameRoutine implements CollisionActionListener {

    final Player player;
    final DialogOutputListener dialogListener;
    final CycleList<Point> portals = new CycleList<>();
    final Map<BlockType, TextBox> dialogMap = new HashMap<>();
    final List<String> audioTrackList;
    final List<Enemy> enemyList;
    final Random rand = new Random();

    private final JFrame frame;

    public GameRoutine(Player player, List<Enemy> enemyList, String audioTrackListFileString, JFrame frame) {
        this.player         = player;
        this.audioTrackList = TextIO.TEXTIO.loadAudioTrackList(audioTrackListFileString);
        this.dialogListener = player.getDialogOutputListener();
        this.frame          = frame;
        if (enemyList == null) {
            this.enemyList  = new ArrayList<>();
        } else {
            this.enemyList  = enemyList;
        }
    }

    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

    public Enemy getEnemy() {
        if ( !enemyList.isEmpty() ) {
            return enemyList.remove( rand.nextInt( enemyList.size() ));
        } else {
            return null;
        }
    }

    public void addDialog(BlockType tType, TextBox dialog) {
        dialogMap.put(tType, dialog);
    }

    public Player getPlayer() {
        return player;
    }

    abstract void loot(Combatant enemy);

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getCollisionType() ) {
            case TEXT -> dialogListener.show( dialogMap.get( e.block.getType() ));
            case ENEMY -> {
                Combatant enemyMinion  = getEnemy().getMinion();
                Combatant playerMinion = player.getPlayerAsMinion();
                if ( enemyMinion == null || playerMinion == null ) {
                    return;
                }
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
            case PORTAL -> {
                Point target = e.getBlock().getPosition();
                portals.addIfAbsent(target);
                if ( portals.size() < 2 ) {
                    // Wenn weniger als 2 Portale bekannt sind, bleibt der Spieler wo er ist.
                    return;
                }
                ((GameMap) e.getSource()).moveThroughPortal( portals.getNext() );
            }
            case EXIT -> ((GameMap) e.getSource()).deactivate();
        }
    }

}
