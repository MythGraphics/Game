/*
 *
 */

package game;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.ArrayList;

public class Resource implements HasName {

    public enum ResourceType {
        HEALTH,     // Gesundheitsressource zur Interaktion mit Gegnern, Environment
        MANA,       // Ressource ohne spezifischen Zweck
        CREDIT,     // Handelsressource
        REPUTATION, // Ressource zur NPC-Interaktion, Freischaltung von Ereignissen, Quests
        AIR;        // Ressource zur Map-Interaktion
    }

    final String name;

    private final ResourceType type;
    private final ArrayList<ResourceActionListener> actionListeners = new ArrayList<>();

    private int default_max = 100;
    private int max         = 100;
    private int value       = 0;

    public Resource(String name, ResourceType type) {
        this(name, type, 100);
    }

    public Resource(String name, ResourceType type, int max) {
        this(name, type, max, max);
    }

    public Resource(String name, ResourceType type, int max, int value) {
        this.name               = name;
        this.type               = type;
        this.default_max        = max;
        this.max                = max;
        this.value              = value;
    }

    public void addResourceActionListener(ResourceActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    @Override
    public String getName() {
        return name;
    }

    public ResourceType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int percentMax) {
        value = max*percentMax/100;
        fireEvent();
    }

    public void setMax(int value) {
        default_max = value;
        max = value;
        fireEvent();
    }

    public void buffMax(int percent) {
        max += default_max*percent/100;
        fireEvent();
    }

    public void resetMax() {
        max = default_max;
        fireEvent();
    }

    public int getMax() {
        return max;
    }

    /**
     * Force to use the resource.
     * Do NOT use this method to restore the resource!
     * @see recharge(int quantity)
     * @param quantity The amount of resource to be used
     * @return resource overuse
     */
    public int force(int quantity) {
        value -= quantity;
        if ( value < 0 ) {
            int overuse = -value;
            value = 0;
            fireEvent();
            return overuse;
        }
        fireEvent();
        return 0;
    }

    /**
     * Use the resource, if enough is available. If not, the resource is not reduced.
     * Do NOT use this method to restore the resource!
     * @see recharge(int quantity)
     * @param quantity The amount of resource to be used
     * @return TRUE, if the quantity can be used, otherwise FALSE
     */
    public boolean use(int quantity) {
        if ( value >= quantity ) {
            value -= quantity;
            fireEvent();
            return true;
        }
        return false;
    }

    /**
     * Recharges the resource up to max.
     * @param quantity The amount of resource to be recharged.
     */
    public void recharge(int quantity) {
        if (( value += quantity ) > max ) {
            value = max;
        }
        fireEvent();
    }

    private void fireEvent() {
        actionListeners.forEach( listener -> listener.resourceActionPerformed( this ));
    }

    @Override
    public String toString() {
        return name + " " + getValue() + "/" + getMax();
    }

}
