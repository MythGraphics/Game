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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TextRenderer extends JPanel implements ActionListener {

    public final static int D = 12; // Distance between text lines

    private final Font font;
    private final Timer renderLoop;

    private Point pos; // text position
    private int max_char_length = 80;
    private int max_line_length = 10;
    private Graphics2D g2d;
    private boolean auto_continue = true;
    private StringBuilder sb = new StringBuilder(); // enthält den momentan angezeigten Text der letzten Zeile
    private ArrayList<String> lines; // die angezeigten Zeilen
    private int textPointer = -1;
    private String text; // aktueller Text in Verarbeitung

    public TextRenderer() {
        this( new Point( D, D ));
    }

    public TextRenderer(Point pos) {
        this.pos = pos;
        font = new Font( "DejaVu Sans Mono", Font.PLAIN, D );
        renderLoop = new Timer(25, this); // 40fps (1000/25)
    }

    private void setDisplayableSize(Dimension size) {
        if (g2d == null) {
            return;
        }

        // Hole die echten Maße der aktuellen Schriftart
        FontMetrics fm = g2d.getFontMetrics(font);

        // Durchschnittliche Breite eines Zeichens (z.B. 'm' bei Monospaced)
        int charWidth = fm.charWidth('m');
        if (charWidth <= 0) {
            charWidth = 7; // fallback
        }

        // Berechne, wie viele Zeichen horizontal Platz haben
        int availableWidth = size.width - pos.x - 2*D; // 2*D als Puffer rechts
        max_char_length = Math.max(1, availableWidth / charWidth);

        // Berechne, wie viele Zeilen vertikal Platz haben
        int availableHeight = size.height - pos.y;
        // D ist hier der Zeilenabstand (Line Height)
        max_line_length = Math.max(1, availableHeight / D);
    }

    public void setTextPosition(Point pos) {
        this.pos = pos;
    }

    public Point getTextPosition() {
        return pos;
    }

    public void setFontColor(Color c) {
        super.setForeground(c);
    }

    public void setAutoContinue(boolean auto_continue) {
        this.auto_continue = auto_continue;
    }

    public void showLine(String text) {
        drawCharByChar(text + "\n");
    }

    public void show(String text) {
        drawCharByChar(text);
    }

    public void printCharByChar(String text) {
        drawCharByChar(text);
    }

    public void stop() {
        renderLoop.stop();
    }

    public void dispose() {
        stop();
    }

    public boolean ready() {
        return text == null;
    }

    public boolean isRunning() {
        return renderLoop.isRunning();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        initRendering(g2d); // Zuerst Font setzen!
        setDisplayableSize( super.getSize() );
        draw(g2d);
    }

    private void initRendering(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setFont(font);
        g2d.setColor( super.getForeground() );
    }

    @Override
    // wird vom Timer ausgelöst; RenderLoop
    public void actionPerformed(ActionEvent evt) {
        if ( text == null || text.isBlank() ) {
            super.repaint();
            return;
        }

        sb.append( text.charAt( textPointer ));
        if ( !auto_continue && ( text.charAt(textPointer) == '\n' )) {
            stop();
        }
        ++textPointer;
        if ( textPointer >= text.length() ) {
            text = null;
            textPointer = -1;
            stop();
        }
        super.repaint();
    }

    /**
     * Main method to be called.
     * @param text Text to render.
     */
    public void drawCharByChar(String text) {
        if (text == null) { return; }
        this.text = text;
        this.textPointer = 0;
        renderLoop.start();
    }

    public void removeText() {
        stop();
        sb = new StringBuilder();
        repaint();
    }

    protected void draw(Graphics2D g2d) {
        prepairLines();
        for (int i = 0; i < lines.size(); ++i) {
            g2d.drawString( lines.get(i), pos.x, pos.y+i*D );
        }
    }

    /**
     * Bei Zeilenumbruch neuen String auf nächster Zeile zeichnen.
     * incl. Scroll-Buffer
     */
    private void prepairLines() {
        lines = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < sb.length(); ++i) {
            if ( sb.charAt(i) == '\n' ) { // bei Zeilenumbruch
                lines.add( sb.substring( start, i ));
                start = i+1; // +1, sonst bleibt \n erhalten
            }
            if ( i-start >= max_char_length ) { // bei Zeilenende
                lines.add( sb.substring( start, i ));
                start = i;
            }
        }
        if ( start < sb.length() ) {
            lines.add( sb.substring( start ));
        }
        while ( lines.size() > max_line_length ) {
            lines.remove(0);
        }
    }

}
