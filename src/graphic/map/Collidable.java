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

public interface Collidable extends IsBlock {

    /**
     * Wird aufgerufen, wenn eine Kollision stattfindet.
     * @param source    Die GameMap, auf welche die Kollision dedektiert wurde.
     * @param initiator Das Objekt, das die Kollision verursacht hat.
     * @param handler   Ermöglicht das Feuern von Events oder den Zugriff auf Aktionen.
     * @return          TRUE, wenn der initiator sich auf das Feld bewegen darf (passable).
     */
    boolean onCollision(GameMap source, Block initiator, IsCollisionHandler handler);

}
