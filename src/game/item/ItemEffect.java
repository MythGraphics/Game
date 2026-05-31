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

import game.Resource.ResourceType;

public class ItemEffect {

    public enum ItemEffectType {
        PRÄFIX,
        SUFFIX;
    }

    final String name;
    final ItemEffectType type;
    final ResourceType resource;
    final int value2add; // absolute Menge an aufzuladender Ressource
    final int buffMax; // in %

    public ItemEffect(String name, ItemEffectType type, ResourceType resource, int value2add) {
        this(name, type, resource, value2add, 0);
    }

    public ItemEffect(String name, ItemEffectType type, ResourceType resource, int value2add, int buffMax) {
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.value2add = value2add;
        this.buffMax = buffMax;
    }

    public String getName() {
        return name;
    }

    public ItemEffectType getType() {
        return type;
    }

    public ResourceType getResourceType() {
        return resource;
    }

    public int getValue() {
        return value2add;
    }

    public int getBuffMax() {
        return buffMax;
    }

}
