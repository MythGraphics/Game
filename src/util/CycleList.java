/*
 *
 */

package util;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.ArrayList;

public class CycleList<T> extends ArrayList<T> {

    private int currentIndex = 0;

    public CycleList() {}

    public T getNext() {
        T item = super.get(currentIndex);
        // Modulo sorgt dafür, dass der Index bei size wieder auf 0 springt
        currentIndex = (currentIndex + 1) % super.size();
        return item;
    }

    public T getPrevious() {
        // + size verhindert negative Werte vor dem Modulo
        currentIndex = ( currentIndex-1 + super.size() ) % super.size();
        return super.get(currentIndex);
    }

    public void addIfAbsent(T e) {
        if ( !super.contains( e )) {
            super.add(e);
        }
    }

}
