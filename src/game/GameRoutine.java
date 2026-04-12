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

import game.item.Item;
import graphic.CollisionActionListener;
import graphic.CollisionEvent;
import static graphic.CollisionType.*;
import graphic.io.TextIO;
import graphic.map.BlockType;
import graphic.map.GameMap;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import util.CycleList;

public class GameRoutine implements CollisionActionListener {

    final Player player;
    final DialogOutputListener dialogListener;
    final CycleList<Point> portals;
    final Map<BlockType, TextBox> dialogMap = new HashMap<>();
    final List<String> audioTrackList;
    final Random rand = new Random();

    public GameRoutine(Player player, List<Item> lootPool, String audioTrackListFileString) {
        this.player = player;
        this.audioTrackList = TextIO.TEXTIO.loadAudioTrackList(audioTrackListFileString);
        this.dialogListener = player.getDialogOutputListener();
        this.portals = new CycleList<>();
    }

    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

    public void addDialog(BlockType tType, TextBox dialog) {
        dialogMap.put(tType, dialog);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        switch( e.getCollisionType() ) {
            case TEXT -> dialogListener.show( dialogMap.get( e.block.getType() ));
            case PORTAL -> {
                Point target = e.getBlock().getPosition();
                portals.addIfAbsent(target);
                if ( portals.size() < 2 ) {
                    // wenn weniger als 2 Portale bekannt sind, bleibt der Spieler wo er ist
                    return;
                }
                ((GameMap) e.getSource()).moveThroughPortal( portals.getNext() );
            }
            case EXIT -> ((GameMap) e.getSource()).deactivate();
        }
    }


}
