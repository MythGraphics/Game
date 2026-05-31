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
        HEALTH,     // Gesundheitsressource für Gegner-Interaktion
        MANA,       // Sekundär-Resource ohne spezifischen Zweck
        AIR,        // Ressource für Environment-Interaktion
        STAMINA,    // Ressource für Map-Interaktion
        REPUTATION, // Ressource für NPC-Interaktion, Freischaltung von Ereignissen, Quests
        CREDIT,     // Handelsressource
    }

    final String name;

    private final ResourceType type;
    private final ArrayList<ResourceChangeListener> changeListeners     = new ArrayList<>();
    private final ArrayList<ResourceConsumeListener> consumeListeners   = new ArrayList<>();
    private final ArrayList<ResourceRechargeListener> rechargeListeners = new ArrayList<>();

    private int defaultMax  = 100;
    private int max         = 100;
    private int value       = 0;

    public Resource(String name, ResourceType type) {
        this(name, type, 100);
    }

    public Resource(String name, ResourceType type, int max) {
        this(name, type, max, max);
    }

    public Resource(String name, ResourceType type, int max, int value) {
        this.name       = name;
        this.type       = type;
        this.defaultMax = max;
        this.max        = max;
        this.value      = value;
    }

    public void addResourceChangeListener(ResourceChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void addResourceConsumeListener(ResourceConsumeListener consumeListener) {
        consumeListeners.add(consumeListener);
    }

    public void addResourceRechargeListener(ResourceRechargeListener rechargeListener) {
        rechargeListeners.add(rechargeListener);
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

    public void setValueAsPercentage(int percentMax) {
        value = max*percentMax/100;
        fireEvent();
    }

    public void setValue(int value) {
        int oldValue = this.value;
        this.value = value;
        fireEvent();
        if (oldValue < value) {
            fireRechargeEvent(value-oldValue, 0);
        }
    }

    public void setMax(int value) {
        defaultMax = value;
        max = value;
        fireEvent();
    }

    public void setMaxAndFill(int value) {
        int oldValue = this.value;
        defaultMax = value;
        max = value;
        this.value = value;
        fireEvent();
        if (oldValue < this.value) {
            fireRechargeEvent(this.value-oldValue, 0);
        }
    }

    public void buffMaxAsPercentage(int percentMax) {
        max += defaultMax*percentMax/100;
        fireEvent();
    }

    public void buffMax(int value) {
        max += max;
        fireEvent();
    }

    public void resetMax() {
        max = defaultMax;
        fireEvent();
    }

    public int getMax() {
        return max;
    }

    /**
     * Force to consume the resource.
     * Do NOT use this method to restore the resource!
     * @see recharge(int quantity)
     * @param quantity The amount of resource to be used
     * @return resource overuse
     */
    public int forceConsume(int quantity) {
        value -= quantity;
        int overuse = 0;
        if ( value < 0 ) {
            overuse = -value;
            value = 0;
        }
        fireEvent();
        fireConsumeEvent(quantity-overuse, overuse);
        return overuse;
    }

    /**
     * Consume the resource, if enough is available, if not, the resource is not consumed.
     * Do NOT use this method to restore the resource!
     * @see consumeForce(int quantity)
     * @see recharge(int quantity)
     * @param quantity The amount of resource to be used
     * @return TRUE, if the quantity can be used, otherwise FALSE
     */
    public boolean consume(int quantity) {
        if ( value >= quantity ) {
            value -= quantity;
            fireEvent();
            fireConsumeEvent(quantity, 0);
            return true;
        }
        return false;
    }

    /**
     * Recharges the resource up to max.
     * Does NOT fire any event, if value is already at max.
     * @param charge The amount of resource to be (re-)charged.
     */
    public void recharge(int charge) {
        if (value == max) {
            return;
        }
        int oldValue = value;
        int overCharge = 0;
        if (( value += charge ) > max ) {
            overCharge = value-max;
            value = max;
        }
        fireEvent();
        fireRechargeEvent(value-oldValue, overCharge);
    }

    private void fireConsumeEvent(int use, int overuse) {
        consumeListeners.forEach( listener -> listener.resourceConsumePerformed( this, use, overuse ));
    }

    private void fireRechargeEvent(int charge, int overCharge) {
        rechargeListeners.forEach( listener -> listener.resourceRechargePerformed( this, charge, overCharge ));
    }

    private void fireEvent() {
        changeListeners.forEach( listener -> listener.resourceChangePerformed( this ));
    }

    @Override
    public String toString() {
        return name + " " + getValue() + "/" + getMax();
    }

}
