/*
 *
 */

package graphic.block;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;

public interface Collidable extends IsBlock {

    /**
     * Wird für das Kollisionsziel aufgerufen, sobald eine statt findet.
     * @param initiator Das Objekt, das die Kollision augelöst hat
     * @param map       Die GameMap, auf welche die Kollision dedektiert wurde.
     * @param handler   Ermöglicht das Feuern von Events oder den Zugriff auf Aktionen.
     * @return          TRUE, wenn der Initiator sich auf das Feld bewegen darf (passable).
     */
    boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler);

    /**
     * Wird für die Kollisionsquelle aufgerufen, sobald eine statt findet.
     * @param target    Das Objekt, mit dem kollidiert wurde.
     * @param map       Die GameMap, auf welche die Kollision dedektiert wurde.
     */
    default void collidesWith(Collidable target, GameMap map) {}

}
