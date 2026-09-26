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

import game.HasHealth;
import graphic.map.IsBlock;
import java.awt.Color;
import java.awt.Graphics2D;

public class OverlayUtil {

    private OverlayUtil() {}

    public static void drawHealthBar(Graphics2D g2d, HasHealth entity, IsBlock block) {
        int currentPercent = entity.getHealth().getValue() / entity.getHealth().getMax();
        Color barColor = getHealthColor(currentPercent);
        int height = Math.max( 4, block.getHeight()/10 );
        g2d.setColor(barColor);
        int y = block.getY() + block.getHeight() - height;
        g2d.fillRect( block.getX(), y, ( block.getWidth()*currentPercent), height );
    }

    /**
     * Berechnet eine dynamische Farbe basierend auf dem Prozentwert (0.0 bis 1.0).
     *  0% -  10%: Rot
     * 10% -  50%: Übergang von Rot zu Gelb
     * 50% - 100%: Übergang von Gelb zu Grün
     * @param percentage
     * @return
     */
    public static Color getHealthColor(float percentage) {
        // Prozentwert auf [0.0, 1.0] begrenzen
        percentage = Math.max( 0.0f, Math.min( 1.0f, percentage ));

        // Unter 10% festes Rot für kritischen Zustand
        if (percentage <= 0.10f) {
            return Color.RED;
        }

        // Phase 1: 10% bis 50% (Rot -> Gelb)
        if (percentage < 0.50f) {
            // Skaliere den Bereich 0.10..0.50 auf den Faktor 0.0..1.0
            float factor = (percentage - 0.10f) / (0.50f - 0.10f);
            return interpolate(Color.RED, Color.YELLOW, factor);
        }

        // Phase 2: 50% bis 100% (Gelb -> Grün)
        // Skaliere den Bereich 0.50..1.00 auf den Faktor 0.0..1.0
        float factor = (percentage - 0.50f) / (1.00f - 0.50f);
        return interpolate(Color.YELLOW, Color.GREEN, factor);
    }

    /**
     * Lineare Interpolation zwischen zwei Farben.
     * @param c1
     * @param c2
     * @param factor
     * @return
     */
    public static Color interpolate(Color c1, Color c2, float factor) {
        int r = (int) ( c1.getRed()   + factor * ( c2.getRed()   - c1.getRed() ));
        int g = (int) ( c1.getGreen() + factor * ( c2.getGreen() - c1.getGreen() ));
        int b = (int) ( c1.getBlue()  + factor * ( c2.getBlue()  - c1.getBlue() ));
        return new Color(r, g, b);
    }

}
