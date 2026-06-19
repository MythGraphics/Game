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
import java.util.List;

public class Resource implements HasName {

    public enum ResourceType {
        HEALTH,     // Primär-Ressource; Gesundheitsressource für Gegner-Interaktion
        MANA,       // Sekundär-Ressource ohne spezifischen Zweck
        AIR,        // map-spezifische Ressource
        STAMINA,    // allgemeine Ressource für Map-/Environment-Interaktion
        REPUTATION, // Ressource für NPC-Interaktion, Freischaltung von Ereignissen, Quests
        CREDIT,     // Handelsressource
    }

    final String name;
    final int baseMax;

    private final ResourceType type;
    private final List<ResourceChangeListener> changeListeners     = new ArrayList<>();
    private final List<ResourceConsumeListener> consumeListeners   = new ArrayList<>();
    private final List<ResourceRechargeListener> rechargeListeners = new ArrayList<>();

    private int max   = 0;
    private int value = 0;

    public Resource(String name, ResourceType type) {
        this(name, type, 100);
    }

    public Resource(String name, ResourceType type, int max) {
        this(name, type, max, max);
    }

    public Resource(String name, ResourceType type, int max, int value) {
        this.name       = name;
        this.type       = type;
        this.baseMax    = max;
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

    public void addValue(int value) {
        if (value == 0) {
            return;
        }
        this.value += value;
        fireChangeEvent();
        fireRechargeEvent(value, 0);
    }

    public void setValue(int value) {
        if (this.value == value) {
            return;
        }
        int oldValue = this.value;
        this.value = value;
        fireChangeEvent();
        if (oldValue < value) {
            fireRechargeEvent(value-oldValue, 0);
        }
    }

    public void setValueAsMaxPercentage(int percentMax) {
        if (value == max*percentMax/100) {
            return;
        }
        int oldValue = value;
        value = max*percentMax/100;
        fireChangeEvent();
        if (oldValue < value) {
            fireRechargeEvent(value-oldValue, 0);
        }
    }

    public void setMax(int value) {
        max = value;
        fireChangeEvent();
    }

    public void buffMax(int value) {
        max += max;
        fireChangeEvent();
    }

    public void setMaxAndFill(int max) {
        int oldValue = this.value;
        this.max = max;
        this.value = max;
        fireChangeEvent();
        if (oldValue < this.value) {
            fireRechargeEvent(this.value-oldValue, 0);
        }
    }

    public void setMaxAsPercentage(int percentMax) {
        max += baseMax*percentMax/100;
        fireChangeEvent();
    }

    public void resetMax() {
        max = baseMax;
        fireChangeEvent();
    }

    public int getMax() {
        return max;
    }

    public int getBaseMax() {
        return baseMax;
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
        fireChangeEvent();
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
            fireChangeEvent();
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
        fireChangeEvent();
        fireRechargeEvent(value-oldValue, overCharge);
    }

    /**
     * Recharges the resource up to percent max.
     * Does NOT fire any event, if value is already at max.
     * @param percentCharge The amount of resource to be (re-)charged in % of max.
     */
    public void rechargeAsPercentageMax(int percentCharge) {
        if (value == max) {
            return;
        }
        int oldValue = value;
        int overCharge = 0;
        value += value*(percentCharge/100);
        if ( value > max ) {
            overCharge = value-max;
            value = max;
        }
        fireChangeEvent();
        fireRechargeEvent(value-oldValue, overCharge);
    }

    private void fireConsumeEvent(int use, int overuse) {
        consumeListeners.forEach( listener -> listener.resourceConsumePerformed( this, use, overuse ));
    }

    private void fireRechargeEvent(int charge, int overCharge) {
        rechargeListeners.forEach( listener -> listener.resourceRechargePerformed( this, charge, overCharge ));
    }

    private void fireChangeEvent() {
        changeListeners.forEach( listener -> listener.resourceChangePerformed( this ));
    }

    @Override
    public String toString() {
        return name + " " + getValue() + "/" + getMax();
    }

}
