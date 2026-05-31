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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class MultiTextRenderer extends TextRenderer {

    private final LinkedList<String> textList;

    public MultiTextRenderer() {
        this( new Point( D, D ));
    }

    public MultiTextRenderer(Point p) {
        super(p);
        textList = new LinkedList<>();
    }

    @Override
    public void drawCharByChar(String text) {
        if (text == null) { return; }
        textList.add(text);
        // wenn die Basisklasse gerade nichts macht, starte die Animation sofort
        if ( super.ready() ) {
            processNextText();
        }
    }

    private void processNextText() {
        if ( textList.isEmpty() ) {
            super.stop(); // stoppt den Timer, wenn die Warteschlange leer ist
            return;
        }
        String nextText = textList.poll();
        super.drawCharByChar(nextText); // explizit die Methode der super-Klasse aufrufen um Text zu rendern
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if ( super.ready() && !textList.isEmpty() ) {
            processNextText();
        }
    }

}
