/*
 *
 */

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

package game.combat;

public class Attribute {

    private final AttributeType aType;

    private int value = 0;

    public Attribute(AttributeType aType, int value) {
        this.aType = aType;
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public AttributeType getType() {
        return aType;
    }

}
