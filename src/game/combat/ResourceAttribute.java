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

import game.resource.Resource;
import java.util.HashMap;
import java.util.Map;

public class ResourceAttribute extends Attribute {

    private final Map<Resource, Integer> buffs = new HashMap<>(); // Resource & Buff in % für dieses Attribut

    public ResourceAttribute(AttributeType aType) {
        this(aType, 0);
    }

    public ResourceAttribute(AttributeType aType, int value) {
        super(aType, value);
    }

    public ResourceAttribute(AttributeType aType, int value, Resource resource, int buffPercent) {
        this(aType, value);
        setBuff(resource, buffPercent);
    }

    public void buff(Resource resource, int lvl) {
        int buff = buffs.get(resource);
        resource.setMaxAsPercentage( getValue()*buff*lvl );
    }

    public void buff(Combatant c) {
        buff( c.getHealth(), c.getLevel() );
    }

    public final void setBuff(Resource resource, int buffPercent) {
        buffs.put(resource, buffPercent);
    }

}
