/*
 *
 */

package graphic.map;

import graphic.block.IsBlock;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

@FunctionalInterface
public interface IsCollider {

    /**
     * Wird für die Kollisionsquelle aufgerufen, sobald eine statt findet.
     * @param target    Das Objekt, mit dem kollidiert wurde.
     * @param map       Die GameMap, auf welche die Kollision dedektiert wurde.
     * @param handler   Ermöglicht das Feuern von Events oder den Zugriff auf Aktionen.
     */
    void collidesWith(IsBlock target, GameMap map, IsCollisionHandler handler);

}
