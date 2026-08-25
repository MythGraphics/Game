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

public interface Interactable {

    /**
     * Wird aufgerufen, wenn eine Kollision stattfindet.
     * @param initiator Das Objekt, das die Kollision verursacht hat (meist der Spieler).
     * @param handler Ermöglicht das Feuern von Events oder den Zugriff auf Aktionen.
     * @return TRUE, wenn der initiator sich auf das Feld bewegen darf (passable).
     */
    boolean onCollision(Block initiator, IsCollisionHandler handler);

}
