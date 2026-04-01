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

import java.awt.Image;

public class Animation {

    // ToDo: Klasse in AnimationData und AnimationPlayer trennen, um das copy() zu vermeiden

    /*
     * defaultLoop: 1 -> 2 -> 3 -> 1 -> 2 -> 3 -> ...
     * reverseLoop: 1 -> 2 -> 3 -> 2 -> 1 -> 2 -> 3 -> 2 -> ...
     */

    private final Image[] spriteSet;
    private final boolean reverseLoop;
    private int currentIndex    = 0;
    private int step            = 1; // 1 vorwärts, -1 rückwärts
    private int tickCounter     = 0;
    private int ticksPerFrame   = 1;

    public Animation(Image[] spriteSet) {
        this(spriteSet, false);
    }

    public Animation(Image[] spriteSet, boolean reverseLoop) {
        this.spriteSet = spriteSet;
        this.reverseLoop = reverseLoop;
    }

    private Animation(Animation other) {
        this.spriteSet = other.spriteSet; // Referenz auf die Bilder bleibt gleich
        this.reverseLoop = other.reverseLoop;
        this.ticksPerFrame = other.ticksPerFrame;
        this.currentIndex = 0;
        this.tickCounter = 0;
        this.step = 1;
    }

    public Animation copy() {
        return new Animation(this);
    }


/*
    public Animation(Image[] spriteSet, boolean reverseLoop) {
        if (!reverseLoop) {
            this.spriteSet = spriteSet;
        } else {
            this.spriteSet = new Image[spriteSet.length*2-2]; // -2, da der erste und letzte Frame nicht doppelt gebraucht wird
            int i = 0;
            for (; i < spriteSet.length; ++i) {
                this.spriteSet[i] = spriteSet[i];
            }
            for (int k = 0, j = spriteSet.length-2; (i+k) < this.spriteSet.length; --j, ++k) {
                this.spriteSet[i+k] = spriteSet[k];
            }
        }
    }
 */

    public void setSpeed(int ticks) {
        this.ticksPerFrame = Math.max(1, ticks); // min. 1
    }

    /**
     * Halbiert die Animationsgeschwindigkeit.
     */
    public void slowDown() {
        this.ticksPerFrame *= 2;
    }

    public Image getImage() {
        if (spriteSet.length == 0) {
            return null;
        }

        // aktuelles Sprite holen
        Image img = spriteSet[currentIndex];

        // Verzögerungs-Logik
        tickCounter++;
        if (tickCounter >= ticksPerFrame) {
            updateIndex();
            tickCounter = 0; // Zähler zurücksetzen
        }

        return img;
    }

    private void updateIndex() {
        // Bei nur einem Bild gibt es nichts zu berechnen
        if (spriteSet.length <= 1) {
            return;
        }
        if (reverseLoop) {
            // Pendel-Logik
            currentIndex += step;
            // Richtungswechsel an den Grenzen
            if (currentIndex == spriteSet.length-1 || currentIndex == 0) {
                step *= -1;
            }
        } else {
            // Index im Kreis bewegen: (0 -> 1 -> 2 -> 0 -> ...)
            currentIndex = (currentIndex + 1) % spriteSet.length;
        }
    }

    public void reset() {
        this.currentIndex   = 0;
        this.tickCounter    = 0;
        this.step           = 1;
    }

    public static Animation[] buildDirectionalImageSet(Image[] imageset) {
        Animation[] set = new Animation[imageset.length];
        for (int i = 0; i < imageset.length; ++i) {
            set[i] = new Animation( new Image[] { imageset[i] });
        }
        return set;
    }

    public static Animation[] buildDirectionalAnimationSet(Image[][] imageset) {
        Animation[] set = new Animation[imageset.length];
        for (int i = 0; i < imageset.length; ++i) {
            set[i] = new Animation(imageset[i]);
        }
        return set;
    }

}
