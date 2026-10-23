/*
 *
 */

package graphic.tile;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.ArrayList;
import java.util.Collection;

public class PortalList extends ArrayList<Portal> {

    @Override
    public boolean add(Portal e) {
        boolean result = super.add(e);
        updateLinks();
        return result;
    }

    @Override
    public void add(int index, Portal e) {
        super.add(index, e);
        updateLinks();
    }

    @Override
    public boolean addAll(Collection<? extends Portal> c) {
        boolean result = super.addAll(c);
        updateLinks();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Portal> c) {
        boolean result = super.addAll(index, c);
        updateLinks();
        return result;
    }

    @Override
    public Portal remove(int index) {
        Portal removed = super.remove(index);
        updateLinks();
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        updateLinks();
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        updateLinks();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = super.retainAll(c);
        updateLinks();
        return result;
    }

    /**
     * Aktualisiert die Portal-Verbindungen nach jeder Änderung:
     * Jedes Portal ab Index 1 zeigt auf seinen Vorgänger (entspricht e.setTarget( getLast() ) beim Hinzufügen).
     * Das erste Portal (Index 0) zeigt immer auf das letzte Portal.
     */
    private void updateLinks() {
        int size = size();
        if (size < 2) {
            return;
        }

        // ab 2 Elementen verweist jedes Portal auf das Vorherige
        for (int i = 1; i < size; i++) {
            get(i).setTarget( get( i-1 ));
        }

        // das erste Portal verweist immer auf das Letzte
        get(0).setTarget( get( size-1 ));
    }

}
