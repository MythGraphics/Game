/*
 *
 */

package game.item;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.Resource;
import game.Resource.ResourceType;
import static game.item.ItemEffect.ValueType.ABSOLUTE;
import static game.item.ItemEffect.ValueType.PERCENT;

public class ItemEffect {

    public enum ItemEffectType {
        PRÄFIX,
        SUFFIX
    }

    public enum ValueType {
        ABSOLUTE,
        PERCENT
    }

    final String name;
    final ItemEffectType eType;
    final ResourceType rType;
    final ValueType vType;

    private int value2Add = 0;
    private int buffMax   = 0;
    private boolean rechargeAfterBuff = false;
    private boolean active = false;

    public ItemEffect(String name, ItemEffectType eType, ResourceType rType, int value2Add, ValueType vType) {
        this(name, eType, rType, vType);
        this.value2Add = value2Add;
    }

    public ItemEffect(String name, ItemEffectType eType, ResourceType rType, int buffMax, ValueType vType,
                      boolean rechargeAfterBuff) {
        this(name, eType, rType, vType);
        this.buffMax = buffMax;
        this.rechargeAfterBuff = rechargeAfterBuff;
    }

    private ItemEffect(String name, ItemEffectType eType, ResourceType rType, ValueType vType) {
        this.name  = name;
        this.eType = eType;
        this.rType = rType;
        this.vType = vType;
    }

    public String getName() {
        return name;
    }

    public ItemEffectType getEffectType() {
        return eType;
    }

    public ValueType getValueType() {
        return vType;
    }

    public ResourceType getResourceType() {
        return rType;
    }

    public int getValue2Add() {
        return value2Add;
    }

    public int getMaxBuff() {
        return buffMax;
    }

    public boolean rechargeAfterBuff() {
        return rechargeAfterBuff;
    }

    public boolean isBuffActive() {
        return active;
    }

    /**
     * Buffs the resource.
     * @param resource The resource to be buffed.
     * @return TRUE, if buff was successful, otherwise FALSE (buff was already active).
     */
    public boolean buff(Resource resource) {
        if (active) {
            return false;
        }
        int value   = resource.getValue();
        int baseMax = resource.getBaseMax();

        switch (vType) {
            case ABSOLUTE -> value += value2Add;
            case PERCENT  -> value += baseMax*value2Add/100; // % von baseMax
        }
        resource.setValue(value);

        int max = resource.getMax();
        switch (vType) {
            case ABSOLUTE -> max += buffMax;
            case PERCENT  -> max += baseMax*buffMax/100;
        }
        if (rechargeAfterBuff) {
            resource.setMaxAndFill(max);
        } else {
            resource.setMax(max);
        }

        active = true;
        return active;
    }

    /**
     * Revoke the buffed resource.
     * @param resource The resource to be unbuffed.
     * @return TRUE, if revoke was successful, otherwise FALSE (buff was not active).
     */
    public boolean revoke(Resource resource) {
        if (!active) {
            return false;
        }
        int value   = resource.getValue();
        int baseMax = resource.getBaseMax();

        switch (vType) {
            case ABSOLUTE -> value -= value2Add;
            case PERCENT  -> value -= baseMax*value2Add/100; // % von baseMax
        }
        resource.setValue(value);

        int max = resource.getMax();
        switch (vType) {
            case ABSOLUTE -> max -= buffMax;
            case PERCENT  -> max -= baseMax*buffMax/100;
        }
        if (rechargeAfterBuff) {
            resource.setMaxAndFill(max);
        } else {
            resource.setMax(max);
        }

        active = false;
        return !active;
    }

}
