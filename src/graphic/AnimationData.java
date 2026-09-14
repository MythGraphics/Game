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

public class AnimationData {

    /*
     * defaultLoop: 1 -> 2 -> 3 -> 1 -> 2 -> 3 -> ...
     * reverseLoop: 1 -> 2 -> 3 -> 2 -> 1 -> 2 -> 3 -> 2 -> ...
     */

    private final BufferedImage[] spriteSet;
    private final boolean reverseLoop;

    public AnimationData(BufferedImage[] spriteSet) {
        this(spriteSet, false);
    }

    public AnimationData(BufferedImage[] spriteSet, boolean reverseLoop) {
        this.spriteSet   = spriteSet != null ? spriteSet : new BufferedImage[0];
        this.reverseLoop = reverseLoop;
    }

    public BufferedImage[] getSpriteSet() {
        return spriteSet;
    }

    public boolean isReverseLoop() {
        return reverseLoop;
    }

    public int getFrameCount() {
        return spriteSet.length;
    }

    public BufferedImage getFrame(int index) {
        if (index < 0 || index >= spriteSet.length) {
            return null;
        }
        return spriteSet[index];
    }

    public static AnimationData[] buildDirectionalImageSet(BufferedImage[] imageset, String directionalString) {
        if (imageset == null || directionalString == null || imageset.length < 4 || directionalString.length() < 4) {
            throw new IllegalArgumentException("At least one parameter is null or from insufficient length.");
        }
        AnimationData[] set = new AnimationData[imageset.length];
        Direction[] d = Direction.parseDirection(directionalString);
        for (int i = 0; i < imageset.length; ++i) {
            set[d[i].ordinal()] = new AnimationData( new BufferedImage[] { imageset[i] });
        }
        return set;
    }

    public static AnimationData[] buildDirectionalImageSet(BufferedImage[] imageset) {
        return buildDirectionalImageSet(imageset, Direction.DEFAULT_ORIENTATION);
    }

    public static AnimationData[] buildDirectionalAnimationSet(BufferedImage[][] imageset, String directionalString) {
        if (imageset == null || directionalString == null || imageset.length < 4 || directionalString.length() < 4) {
            throw new IllegalArgumentException("At least one parameter is null or from insufficient length.");
        }
        AnimationData[] set = new AnimationData[imageset.length];
        Direction[] d = Direction.parseDirection(directionalString);
        for (int i = 0; i < imageset.length; ++i) {
            set[d[i].ordinal()] = new AnimationData(imageset[i]);
        }
        return set;
    }

    public static AnimationData[] buildDirectionalAnimationSet(BufferedImage[][] imageset) {
        return buildDirectionalAnimationSet(imageset, Direction.DEFAULT_ORIENTATION);
    }

}
