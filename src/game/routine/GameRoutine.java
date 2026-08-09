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

import game.GameObjectLoader;
import game.Player;
import game.TextBox;
import graphic.io.TextIO;
import graphic.map.BlockType;
import graphic.map.CollisionActionListener;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import graphic.texter.DialogOutputListener;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import util.CycleList;

public abstract class GameRoutine implements CollisionActionListener {

    final CycleList<Point> portals = new CycleList<>();
    final Map<BlockType, TextBox> dialogMap = new HashMap<>();
    final Random rand = new Random();
    final GameObjectLoader loader;

    private List<String> audioTrackList;

    public GameRoutine() {
        this.loader = new GameObjectLoader( getClass() );
    }

    public abstract Player getPlayer();
    public abstract DialogOutputListener getDialogListener(CollisionEvent e);

    public final GameObjectLoader getLoader() {
        return loader;
    }

    public void setAudioTrackList(String audioTrackListFilePath) {
        this.audioTrackList = TextIO.loadAudioTrackList( audioTrackListFilePath, getClass() );
    }

    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

    public void addDialog(BlockType bType, TextBox dialog) {
        dialogMap.put(bType, dialog);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
//      System.out.println( "(debug) InteractionType: " + e.getType() );              // debug
//      System.out.println( "(debug) BlockType: "     + e.getTarget().getType() );  // debug
        getDialogListener(e).show( dialogMap.get( e.getTarget().getType() ));
        switch( e.getType() ) {
            case PORTAL -> {
                Point target = e.getTarget().getPosition();
                portals.addIfAbsent(target);
                if ( portals.size() < 2 ) {
                    // Wenn weniger als 2 Portale bekannt sind, bleibt der Spieler wo er ist.
                    return;
                }
                (( GameMap ) e.getSource() ).moveThroughPortal( portals.getNext() );
            }
        }
    }

}
