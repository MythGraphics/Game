/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.HasImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TileBuilder {

    private final List<BufferedImage> tileList;

    public record Tile(BufferedImage getImage) implements HasImage {}

    public TileBuilder() {
        this( new ArrayList<>() );
    }

    public TileBuilder(List<BufferedImage> tileList) {
        this.tileList = tileList;
    }

    public void add(BufferedImage image) {
        tileList.add(image);
    }

    public HasImage getRandom() {
        return new Tile( getRandomImage() );
    }

    public BufferedImage getRandomImage() {
        if ( tileList.isEmpty() ) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt( tileList.size() );
        return tileList.get(index);
    }

    public HasImage getBySpatialHash(int x, int y) {
        BufferedImage img = getImageBySpatialHash(x, y);
        return img != null ? new Tile(img) : null;
    }

    public BufferedImage getImageBySpatialHash(int x, int y) {
        if ( tileList.isEmpty() ) {
            return null;
        }

        float hash = getSpatialHash(x, y);
        int index = (int) ( hash * tileList.size() );
        // fängt den seltenen Spezialfall ab, dass hash exakt 1.0f ist
        if ( index >= tileList.size() ) {
            index = tileList.size()-1;
        }

        return tileList.get(index);
    }

    /**
     * Unveränderliche Sicht auf die internen Kacheln.
     * @return tileList
     */
    public List<BufferedImage> getTileList() {
        return Collections.unmodifiableList(tileList);
    }


    /**
     * Berechnet einen stabilen, positiven Pseudo-Zufallswert zwischen 0.0 und 1.0.
     * basierend auf den Kachelkoordinaten (x, y).
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return  Hash-Wert
     */
    public static float getSpatialHash(int x, int y) {
        // Bit-Mixing mittels Primzahlen (große Primzahlen sorgen für eine gute Streuung der Bitmuster) und XOR
        int n = x*73856093 ^ y*19349663;

        // zusätzlicher Bit-Shift für bessere Durchmischung
        n = (n << 13) ^ n;
        n = n * (n * n * 15731 + 789221) + 1376312589;

        // In einen positiven Wert umwandeln und auf [0.0, 1.0) normieren
        int positiveHash = n & 0x7FFFFFFF;
        return (float) positiveHash / Integer.MAX_VALUE;
    }

}
