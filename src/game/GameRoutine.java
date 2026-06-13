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

import graphic.DialogOutputListener;
import graphic.io.TextIO;
import graphic.map.BlockType;
import graphic.map.CollisionActionListener;
import graphic.map.CollisionEvent;
import graphic.map.GameMap;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import util.CycleList;

public abstract class GameRoutine implements CollisionActionListener {

    final DialogOutputListener dialogListener;
    final CycleList<Point> portals = new CycleList<>();
    final Map<BlockType, TextBox> dialogMap = new HashMap<>();
    final Random rand = new Random();
    final GameObjectLoader loader;

    private List<String> audioTrackList;

    public GameRoutine(DialogOutputListener dialogListener) {
        this.dialogListener = dialogListener;
        this.loader         = new GameObjectLoader( getClass() );
    }

    abstract Player getPlayer();

    public GameObjectLoader getLoader() {
        return loader;
    }

    public void setAudioTrackList(String audioTrackListFilePath) {
        this.audioTrackList = TextIO.loadAudioTrackList( audioTrackListFilePath, getClass() );
    }

    public List<String> getAudioTrackList() {
        return audioTrackList;
    }

    public void addDialog(BlockType tType, TextBox dialog) {
        dialogMap.put(tType, dialog);
    }

    @Override
    public void collisionPerformed(CollisionEvent e) {
        System.out.println( "(debug) cType: " + e.getCollisionType() );
        System.out.println( "(debug) bType: " + e.getTarget().getType() );
        switch( e.getCollisionType() ) {
            case TEXT, ENV_IMPASS -> dialogListener.show( dialogMap.get( e.getTarget().getType() ));
            case PORTAL -> {
                Point target = e.getTarget().getPosition();
                portals.addIfAbsent(target);
                if ( portals.size() < 2 ) {
                    // Wenn weniger als 2 Portale bekannt sind, bleibt der Spieler wo er ist.
                    return;
                }
                ((GameMap) e.getSource() ).moveThroughPortal( portals.getNext() );
            }
            case EXIT -> ((GameMap) e.getSource() ).deactivate();
        }
    }

}
