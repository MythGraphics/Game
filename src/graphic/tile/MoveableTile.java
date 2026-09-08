/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.AnimationData;
import graphic.AnimationPlayer;
import graphic.Direction;
import graphic.Moveable;
import graphic.map.BlockTile;
import graphic.map.IsBlockType;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

public class MoveableTile extends BlockTile implements Moveable {

    final AnimationPlayer[] aniset;
    final Point maxPoint;
    final int blockSize;

    public MoveableTile(AnimationPlayer[] aniset, IsBlockType bType,
                        int x, int y, int blockSize, Point maxPoint) {
        super(x, y, blockSize, blockSize, bType,
             (aniset != null && aniset.length > 0 ) ? aniset[0] : () -> null
        );
        this.aniset    = aniset;
        this.maxPoint  = maxPoint;
        this.blockSize = blockSize;
    }

    public MoveableTile(AnimationPlayer[] aniset, IsBlockType bType,
                        Point pos, Dimension dim, int blockSize, Point maxPoint) {
        super(pos.x, pos.y, dim.width, dim.height, bType,
             (aniset != null && aniset.length > 0 ) ? aniset[0] : () -> null
        );
        this.aniset    = aniset;
        this.maxPoint  = maxPoint;
        this.blockSize = blockSize;
    }

    public MoveableTile(AnimationData[] dataSet, IsBlockType bType,
                        int x, int y, int blockSize, Point maxPoint) {
        this( createPlayerSet( dataSet ), bType, x, y, blockSize, maxPoint );
    }

    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Erstellt einen MoveableSprite basierend auf einer Map aus Richtungen und AnimationData.
     * Nützlich, wenn die Animationen nicht strikt per Array-Index geordnet sind.
     * @param animMap
     * @param bType
     * @param x
     * @param y
     * @param blockSize
     * @param maxPoint
     * @return
     */
    public static MoveableTile create(Map<Direction, AnimationData> animMap, IsBlockType bType,
                                      int x, int y, int blockSize, Point maxPoint) {
        Direction[] dirs = Direction.values();
        AnimationPlayer[] players = new AnimationPlayer[dirs.length];

        for (int i = 0; i < dirs.length; i++) {
            AnimationData data = animMap.get(dirs[i]);
            if (data != null) {
                players[i] = new AnimationPlayer(data);
            }
        }
        return new MoveableTile(players, bType, x, y, blockSize, maxPoint);
    }

    /**
     * Erstellt einen MoveableTile, der für ALLE Richtungen dieselbe Animation nutzt.
     * @param data
     * @param bType
     * @param x
     * @param y
     * @param blockSize
     * @param maxPoint
     * @return
     */
    public static MoveableTile createSingleAnimation(AnimationData data, IsBlockType bType,
                                                     int x, int y, int blockSize, Point maxPoint) {
        Direction[] dirs = Direction.values();
        AnimationPlayer[] players = new AnimationPlayer[dirs.length];
        AnimationPlayer sharedPlayer = new AnimationPlayer(data);

        for (int i = 0; i < dirs.length; i++) {
            players[i] = sharedPlayer;
        }
        return new MoveableTile(players, bType, x, y, blockSize, maxPoint);
    }

    @Override
    public void move(Direction direction) {
        // Update der aktuellen Animation
        if ( aniset != null && direction.ordinal() < aniset.length && aniset[ direction.ordinal() ] != null ) {
            setImage( aniset[ direction.ordinal() ] );
        }
        // Bewegungslogik
        switch (direction) {
            case UP    -> { if (y - blockSize >= 0)          { y -= blockSize; }}
            case DOWN  -> { if (y + blockSize <= maxPoint.y) { y += blockSize; }}
            case LEFT  -> { if (x - blockSize >= 0)          { x -= blockSize; }}
            case RIGHT -> { if (x + blockSize <= maxPoint.x) { x += blockSize; }}
        }
    }

    /**
     * Erstellt für ein neues Sprite ein frisches Ensemble an AnimationPlayern
     * basierend auf einem Satz gemeinsamer AnimationData.
     * @param dataSet DataSet
     * @return Set of AnimationPlayer
     */
    public static AnimationPlayer[] createPlayerSet(AnimationData[] dataSet) {
        if (dataSet == null) {
            return new AnimationPlayer[0];
        }
        AnimationPlayer[] players = new AnimationPlayer[dataSet.length];
        for (int i = 0; i < dataSet.length; i++) {
            if (dataSet[i] != null) {
                players[i] = new AnimationPlayer(dataSet[i]);
            }
        }
        return players;
    }

}
