/*
 *
 */

package graphic;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.image.BufferedImage;
import java.util.Map;

public class AnimationPlayer implements HasImage {

    private final AnimationData data;
    private int currentIndex    = 0;
    private int step            = 1; // 1 = vorwärts, -1 = rückwärts
    private int tickCounter     = 0;
    private int ticksPerFrame   = 1;

    public AnimationPlayer(AnimationData data) {
        this.data = data;
    }

    public static AnimationPlayer[] createSet(AnimationData[] dataSet) {
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

    public void setSpeed(int ticks) {
        this.ticksPerFrame = Math.max(1, ticks);
    }

    public void slowDown() {
        this.ticksPerFrame *= 2;
    }

    @Override
    public BufferedImage getImage() {
        if (data == null || data.getFrameCount() == 0) {
            return null;
        }

        BufferedImage img = data.getFrame(currentIndex);

        // Verzögerungs-Logik
        tickCounter++;
        if (tickCounter >= ticksPerFrame) {
            updateIndex();
            tickCounter = 0;
        }

        return img;
    }

    private void updateIndex() {
        int count = data.getFrameCount();
        // bei nur einem Bild gibt es nichts zu berechnen
        if (count <= 1) {
            return;
        }

        if ( data.isReverseLoop() ) {
            currentIndex += step;
            // Richtungswechsel an den Grenzen
            if (currentIndex == count-1 || currentIndex == 0) {
                step *= -1;
            }
        } else {
            // Index im Kreis bewegen: (0 -> 1 -> 2 -> 0 -> ...)
            currentIndex = (currentIndex+1) % count;
        }
    }

    public void reset() {
        this.currentIndex = 0;
        this.tickCounter  = 0;
        this.step         = 1;
    }

    public AnimationData getData() {
        return data;
    }

    /**
     * Erstellt ein neues Set von AnimationPlayer basierend auf einem Satz AnimationData.
     * @param dataSet DataSet
     * @return Set of AnimationPlayer
     */
    public static AnimationPlayer[] createAnimationPlayerSet(AnimationData[] dataSet) {
        if (dataSet == null || dataSet.length < 4) {
            throw new IllegalArgumentException("dataSet is null or from insufficient length.");
        }
        AnimationPlayer[] players = new AnimationPlayer[dataSet.length];
        for (int i = 0; i < dataSet.length; i++) {
            if (dataSet[i] != null) {
                players[i] = new AnimationPlayer(dataSet[i]);
            }
        }
        return players;
    }

    /**
     * Erstellt ein neues Set von AnimationPlayer basierend auf einem Satz AnimationData.
     * @param aniMap AnimationMap
     * @return Set of AnimationPlayer
     */
    public static AnimationPlayer[] createAnimationPlayerSet(Map<Direction, AnimationData> aniMap) {
        if (aniMap == null || aniMap.size() < 4) {
            throw new IllegalArgumentException("aniMap is null or from insufficient length.");
        }
        Direction[] directionSet = Direction.values();
        AnimationPlayer[] playerSet = new AnimationPlayer[directionSet.length];
        for (int i = 0; i < directionSet.length; i++) {
            AnimationData data = aniMap.get(directionSet[i]);
            if (data != null) {
                playerSet[i] = new AnimationPlayer(data);
            } else {
                throw new IllegalArgumentException("AnimationData at index " + i + " is null.");
            }
        }
        return playerSet;
    }

}
