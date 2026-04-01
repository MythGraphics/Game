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

import static game.combat.AttributeType.*;
import static game.combat.CombatantType.*;
import static game.combat.DamageType.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends Combatant {

    public final static int XP_PER_LEVEL = 100;

    private final Map<AttributeType, Attribute> attributes;
    private int xp = 0;

    public Player(String name, CombatantType cType, int xp) {
        super(name, cType);
        this.attributes = new HashMap<>();
        addXP(xp);
        Map<Attribute, Integer> lvlBuff = getType(cType); // Anzahl der Punkte, um die das Attribut pro Level steigt
        for ( Attribute a : lvlBuff.keySet() ) {
            // Attribute an Level anpassen
            a.setValue( getLevel()*lvlBuff.get( a ));
            // Attribute in sinnige HashMap ablegen
            attributes.put( a.getAttributeType(), a );
        }
    }

    public int getXP() {
        return xp;
    }

    public final void addXP(int xp) {
        this.xp += xp;
        setLevel( (byte) ( 1 + xp/XP_PER_LEVEL ));
    }

    public int getAttributeValue(AttributeType aType) {
        return attributes.get(aType).getValue();
    }

    @Override
    public void buffDamage(Damage damage) {
        for ( HashMap.Entry<AttributeType, Attribute> entry : attributes.entrySet() ) {
            damage.buffDamage( entry.getValue() );
        }
    }

    public Map<AttributeType, Attribute> getAttributes() {
        return attributes;
    }

    private static Map<Attribute, Integer> getType(CombatantType cType) {
        switch (cType) {
            case APOTHEKER:
                return getApotheker();
            case KRIEGER:
                return getKrieger();
            case MAGIER:
                return getMagier();
            case SPACE_MARINE:
                return getSpaceMarine();
        }
        return null;
    }

    private static Map<Attribute, Integer> getApotheker() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT ), 1 );
        map.put( new Attribute( INTELLIGENZ, GIFT, 200 ), 3 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getKrieger() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, PHYSISCH, 100 ), 2 );
        map.put( new Attribute( INTELLIGENZ ), 1 );
        map.put( new Attribute( STÄRKE, PHYSISCH, 100 ), 2 );
        return map;
    }

    private static Map<Attribute, Integer> getMagier() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, FEUER, 100 ), 2 );
        map.put( new Attribute( INTELLIGENZ, FEUER, 100 ), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getSpaceMarine() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, NUKLEAR, 100 ), 2 );
        map.put( new Attribute( INTELLIGENZ, NUKLEAR, 100), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

}
