/*
 *
 */

package graphic.ui;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.item.IsItem;
import java.awt.Component;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class InventoryCellRenderer extends DefaultListCellRenderer {

    private final Map<IsItem, Integer> inventar;

    public InventoryCellRenderer(Map<IsItem, Integer> inventar) {
        this.inventar = inventar;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Die Standard-Rendering-Komponente holen
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof IsItem item) {
            Integer stack = inventar.get(item);
            label.setText( item.getName() + " (" + stack + ")" );
        }
        return label;
    }

}
