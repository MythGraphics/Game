/*
 *
 */

package game.combat;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.EnumMap;

public class Attribute {

    private final AttributeType aType;
    private final EnumMap<DamageType, Integer> buffs; // DamageType & dmgBuff in % für dieses Attribut

    private int value = 0;

    public Attribute(AttributeType aType) {
        this( aType, 0 );
    }

    public Attribute(AttributeType aType, DamageType dType, int buff) {
        this( aType, 0, new EnumMap<>( DamageType.class ));
        setBuff(dType, buff);
    }

    public Attribute(AttributeType aType, int value) {
        this( aType, value, new EnumMap<>( DamageType.class ));
    }

    public Attribute(AttributeType aType, int value, DamageType dType, int buff) {
        this( aType, value, new EnumMap<>( DamageType.class ));
        setBuff(dType, buff);
    }

    public Attribute(AttributeType aType, int value, EnumMap<DamageType, Integer> buffs) {
        this.aType = aType;
        this.value = value;
        this.buffs = buffs;
    }

    public final void setBuff(DamageType dType, int buff) {
        buffs.put(dType, buff);
    }

    public AttributeType getAttributeType() {
        return aType;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public EnumMap<DamageType, Integer> getBuffs() {
        return buffs;
    }

    public int getBuff(DamageType dType) {
        return buffs.getOrDefault(dType, 0);
    }

}
