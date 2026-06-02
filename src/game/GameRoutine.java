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

import graphic.CollisionActionListener;
import graphic.CollisionEvent;
import graphic.io.TextIO;
import graphic.map.BlockType;
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
    final GameFrame frame;

    private List<String> audioTrackList;

    public GameRoutine(GameFrame frame) {
        this.frame          = frame;
        this.dialogListener = frame.textFrame;
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
        System.out.println( "(debug) bType: " + e.getBlock().getType() );
        switch( e.getCollisionType() ) {
            case TEXT, ENV_IMPASS -> dialogListener.show( dialogMap.get( e.block.getType() ));
            case PORTAL -> {
                Point target = e.getBlock().getPosition();
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
