/*
 *
 */

package graphic.vectorMap;

import graphic.block.IsBlock;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public interface IsVectorBlock {

    final static float TWO_PI        = (float) (2.0 * Math.PI);
    final static float RESTITUTION   = 0.9f; // Elastizitätskoeffizient (<1 für etwas Dämpfung/Energieverlust)

    float getPx();
    float getPy();

    void setPx(float px);
    void setPy(float py);

    float getVx();
    float getVy();

    void setVx(float vx);
    void setVy(float vy);

    int getWidth();
    int getHeight();

    boolean isHeavy();

    /**
     * Berechnet den elastischen Impulsstoß zwischen zwei Vektor-Entitäten.
     * @param source
     * @param target
     */
    static void applyImpulseCollision(IsVectorBlock source, IsVectorBlock target) {
        // Normalenvektor berechnen
        float dx = target.getPx() - source.getPx();
        float dy = target.getPy() - source.getPy();
        float distance = (float) Math.sqrt(dx*dx + dy*dy);

        if (distance == 0) {
            return;
        }

        // normalisieren des Vektors
        float nx = dx / distance;
        float ny = dy / distance;

        // nur wenn mindestens eines der beiden schwer ist, wird Impuls übertragen
        if ( source.isHeavy() || target.isHeavy() ) {
            // Relativgeschwindigkeit in Richtung der Normalen berechnen
            float dvx = target.getVx() - source.getVx();
            float dvy = target.getVy() - source.getVy();
            float velocityAlongNormal = dvx * nx + dvy * ny;
            // nur wenn sich die Objekte auf einander zu bewegen, weiter machen, andernfals ist die Kollision bereits passiert
            if (velocityAlongNormal <= 0) {
                // Impulsübertragung
                float impulseScalar = -(1 + RESTITUTION) * velocityAlongNormal / 2.0f;

                if ( source.isHeavy() && !target.isHeavy() ) {
                    // source ist schwer und schiebt target weg
                    target.setVx(target.getVx() + impulseScalar * nx * 2.0f);
                    target.setVy(target.getVy() + impulseScalar * ny * 2.0f);
                } else if ( !source.isHeavy() && target.isHeavy() ) {
                    // target ist schwer und stößt source zurück
                    source.setVx(source.getVx() - impulseScalar * nx * 2.0f);
                    source.setVy(source.getVy() - impulseScalar * ny * 2.0f);
                } else {
                    // beide sind schwer: klassischer elastischer Stoß zweier gleichgroßer Massen
                    source.setVx(source.getVx() - impulseScalar * nx);
                    source.setVy(source.getVy() - impulseScalar * ny);
                    target.setVx(target.getVx() + impulseScalar * nx);
                    target.setVy(target.getVy() + impulseScalar * ny);
                }
            }
        }

        // Überlappungskorrektur
        float minDist = ( source.getWidth() + target.getWidth() ) / 2.0f; // quadratische Tiles annehmen
        if (distance < minDist) {
            float overlap = minDist - distance; // gesamte Überlappung auflösen
            if ( source.isHeavy() && !target.isHeavy() ) {
                // nur source ist schwer, target weicht komplett aus
                target.setPx(target.getPx() + nx * overlap);
                target.setPy(target.getPy() + ny * overlap);
            } else if ( !source.isHeavy() && target.isHeavy() ) {
                // nur target ist schwer, source weicht komplett aus
                source.setPx(source.getPx() - nx * overlap);
                source.setPy(source.getPy() - ny * overlap);
            } else {
                // beide gleich: 50:50 aufteilen
                float halfOverlap = 0.5f * overlap;
                source.setPx(source.getPx() - nx * halfOverlap);
                source.setPy(source.getPy() - ny * halfOverlap);
                target.setPx(target.getPx() + nx * halfOverlap);
                target.setPy(target.getPy() + ny * halfOverlap);
            }
        }
    }

    static void applyWallCollision(IsVectorBlock source, IsBlock target) {
        // Mittelpunkte berechnen, um die Kollisionsseite (horizontal oder vertikal) zu bestimmen
        float sourceCenterX = source.getPx() + source.getWidth()  / 2.0f;
        float sourceCenterY = source.getPy() + source.getHeight() / 2.0f;
        float wallCenterX   = target.getX()  + target.getWidth()  / 2.0f;
        float wallCenterY   = target.getY()  + target.getHeight() / 2.0f;

        // Differenzen auf der Achse
        float dx = sourceCenterX - wallCenterX;
        float dy = sourceCenterY - wallCenterY;

        // Erlaubte Minimal-Abstände (halbe Ausdehnung addiert)
        float minDistX = ( source.getWidth()  + target.getWidth()  ) / 2.0f;
        float minDistY = ( source.getHeight() + target.getHeight() ) / 2.0f;

        // Überlappung auf beiden Achsen ermitteln
        float overlapX = minDistX - Math.abs(dx);
        float overlapY = minDistY - Math.abs(dy);

        // prüfen, ob Kollision von der Seite oder von oben/unten
        if (overlapX < overlapY) {
            // horizontale Kollision (links oder rechts) -> X-Geschwindigkeit umkehren
            source.setVx(-source.getVx() * RESTITUTION);
            // Position korrigieren, damit das Objekt nicht in der Wand stecken bleibt
            if (dx > 0) {
                source.setPx( target.getX() + target.getWidth() );
            } else {
                source.setPx( target.getX() - source.getWidth() );
            }
        } else {
            // vertikale Kollision (oben oder unten) -> Y-Geschwindigkeit umkehren
            source.setVy(-source.getVy() * RESTITUTION);
            // Position korrigieren
            if (dy > 0) {
                source.setPy( target.getY() + target.getHeight() );
            } else {
                source.setPy( target.getY() - source.getHeight() );
            }
        }
    }

}
