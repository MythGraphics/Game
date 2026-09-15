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

public interface IsCollider {

    /**
     * Wird für die Kollisionsquelle aufgerufen, sobald eine Kollision stattfindet.
     * @param map       Die GameMap, auf welche die Kollision dedektiert wurde.
     * @param target    Das Objekt, mit dem kollidiert wurde.
     * @param handler   Ermöglicht das Feuern von Events oder den Zugriff auf Aktionen.
     */
    void collides(GameMap map, Block target, IsCollisionHandler handler);

}
