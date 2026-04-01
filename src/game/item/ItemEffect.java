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

    public enum TYPE {
        PRÄFIX,
        SUFFIX;
    }

    final String name;
    final TYPE type;
    final ResourceType resource;
    final int value2add; // absolute Menge an aufzuladender Ressource
    final int buff_max; // in %

    public ItemEffect(String name, TYPE type, ResourceType resource, int value2add) {
        this(name, type, resource, value2add, 0);
    }

    public ItemEffect(String name, TYPE type, ResourceType resource, int value2add, int buff_max) {
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.value2add = value2add;
        this.buff_max = buff_max;
    }

}
