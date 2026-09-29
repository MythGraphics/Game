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

public class DamageAttribute extends Attribute {

    private final EnumMap<DamageType, Integer> buffs = new EnumMap<>(DamageType.class); // DamageType & dmgBuff in % für dieses Attribut

    public DamageAttribute(AttributeType aType) {
        this(aType, 0);
    }

    public DamageAttribute(AttributeType aType, DamageType dType, int buffPercent) {
        this(aType, 0);
        setBuff(dType, buffPercent);
    }

    public DamageAttribute(AttributeType aType, int value) {
        super(aType, value);
    }

    public DamageAttribute(AttributeType aType, int value, DamageType dType, int buffPercent) {
        this(aType, value);
        setBuff(dType, buffPercent);
    }

    public final void setBuff(DamageType dType, int buffPercent) {
        buffs.put(dType, buffPercent);
    }

    public EnumMap<DamageType, Integer> getBuffs() {
        return buffs;
    }

    public int getBuff(DamageType dType) {
        return buffs.getOrDefault(dType, 0);
    }

}
