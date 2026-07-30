/*
 *
 */

package graphic.texter;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class ChoiceDialog<T> extends JDialog {

    private final JFrame parentFrame;

    private JList<T> choiceList;
    private int selectedIndex = 0; // standardmäßig den ersten Eintrag auswählen
    private T selectedValue;

    // unterstützt Varargs
    @SafeVarargs
    public ChoiceDialog(JFrame parentFrame, T... options) {
        this( parentFrame, options != null ? Arrays.asList(options) : List.of() );
    }

    // unterstützt Collections/Lists
    public ChoiceDialog(JFrame parentFrame, List<T> options) {
        super(parentFrame, null, ModalityType.DOCUMENT_MODAL);
        this.parentFrame = parentFrame;

        if (options == null || options.size() <= 0) {
            return;
        }

        selectedValue = options.get(0);

        DefaultListModel<T> model = new DefaultListModel<>();
        model.addAll(options);
        choiceList = new JList<>(model);
        choiceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        choiceList.setVisibleRowCount( options.size() );
        if (parentFrame != null) {
            choiceList.setBackground( parentFrame.getBackground() );
        }

        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout( new BorderLayout( 15, 15 ));
        setResizable(false);
        getRootPane().setBorder( BorderFactory.createLineBorder( Color.LIGHT_GRAY, 1 )); // Rahmen hinzufügen, um sich abzuheben

        // Tastatur-Event: Enter wählt das aktuell markierte Element aus
        choiceList.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    confirmSelection();
                }
            }
        });

        // Maus-Event: Einfacher Klick oder Doppelklick wählt Element aus
        choiceList.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 || e.getClickCount() == 2) {
                    confirmSelection();
                }
            }
        });

        // ScrollPane für viele Auswahlmöglichkeiten
        JScrollPane scrollPane = new JScrollPane(choiceList);
        scrollPane.setBorder( BorderFactory.createEmptyBorder() );
        add(scrollPane, BorderLayout.CENTER); // CENTER füllt ohne Ränder den gesamten verfügbaren Platz
        pack(); // berechnet die finale Größe des Fensters
    }

    /**
     * Positioniert den Dialog an der oberen rechten Ecke des Parent-Frames.
     */
    public void updatePosition() {
        if ( parentFrame != null && parentFrame.isShowing() ) {
            Point p = parentFrame.getLocationOnScreen();
            p.x += parentFrame.getWidth();
            setLocation(p.x, p.y);
        } else {
            // Fallback
            setLocationRelativeTo(parentFrame);
        }
    }

    private void confirmSelection() {
        selectedIndex = choiceList.getSelectedIndex();
        selectedValue = choiceList.getSelectedValue();
        dispose();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updatePosition();
            SwingUtilities.invokeLater(choiceList::requestFocusInWindow); // Fokus auf Liste legen
        }
        super.setVisible(visible);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public T getSelectedValue() {
        return selectedValue;
    }

}
