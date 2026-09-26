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

import game.combat.Combatant;
import game.resource.Resource;
import game.resource.Resource.ResourceType;
import static game.resource.Resource.ResourceType.HEALTH;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends InteractiveObject implements HasHealth, HasMinion, HasID, HasResource {

    final Map<ResourceType, Resource> resources;

    private final MinionManager minions;
    private final int id;

    public Enemy(int id, String name) {
        this( id, name, new MinionManager(), new Resource( "Gesundheit", HEALTH, 100, 100 ));
    }

    public Enemy(int id, String name, Resource... resources) {
        this( id, name, new MinionManager(), resources );
    }

    public Enemy(int id, String name, MinionManager minions, Resource... resources) {
        super(name);
        this.id        = id;
        this.minions   = minions;
        this.resources = new HashMap<>();
        if (resources != null) {
            for (Resource r : resources) {
                addResource(r);
            }
        }
    }

    public Enemy(Enemy enemy) {
        super(enemy);
        this.id        = enemy.getId();
        this.minions   = enemy.getMinionManager();
        this.resources = enemy.resources;
    }

    @Override
    public final void addResource(Resource resource) {
        resources.put( resource.getType(), resource );
    }

    @Override
    public Resource getResource(ResourceType type) {
        return resources.get(type);
    }

    @Override
    public Combatant getMinion() {
        return minions.getCurrent();
    }

    @Override
    public void setMinion(Combatant minion) {
        minions.add(minion);
    }

    public MinionManager getMinionManager() {
        return minions;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Resource getHealth() {
        return getResource(HEALTH);
    }

    @Override
    public boolean isAlive() {
        return getHealth().getValue() > 0;
    }

    @Override
    public void takeDamage(int damage) {
        if ( damage < 0 ) {
            getHealth().recharge(damage);
        } else {
            getHealth().forceConsume(damage);
        }
    }

    // shallow copy
    @Override
    public Enemy clone() throws CloneNotSupportedException {
        return new Enemy(this);
    }

}
