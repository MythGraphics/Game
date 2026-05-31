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

import game.Resource;
import static game.combat.AttributeType.*;
import static game.combat.CombatantType.*;
import static game.combat.DamageType.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends Combatant {

    public final static int XP_PER_LEVEL = 100;

    private final Map<AttributeType, Attribute> attributes;
    private int xp = 0;

    public Player(String name, CombatantType cType, Resource health, int xp) {
        super(name, cType, health);
        this.attributes = new HashMap<>();
        addXP(xp);
        buffAttr();
    }

    public int getXP() {
        return xp;
    }

    public final void addXP(int xp) {
        this.xp += xp;
        setLevel((byte) ( this.xp/XP_PER_LEVEL + 1 ));
    }

    public int getAttributeValue(AttributeType aType) {
        return attributes.get(aType).getValue();
    }

    private void buffAttr() {
        Map<Attribute, Integer> lvlBuff = getType(cType); // Anzahl der Punkte, um die das Attribut pro Level steigt
        for ( Attribute a : lvlBuff.keySet() ) {
            a.setValue( getLevel()*lvlBuff.get( a )); // Attribute an Level anpassen
            attributes.put( a.getAttributeType(), a ); // Attribute in sinnige Map ablegen
        }
    }

    @Override
    public void buffDamage(Damage damage) {
        buffAttr();
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
            case SOLDAT:
                return getSoldat();
            case CHEMIKER:
                return getChemiker();
            case SCHAMANE:
                return getSchamane();
        }
        return null;
    }

    private static Map<Attribute, Integer> getChemiker() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, SÄURE, 50 ), 2 );
        map.put( new Attribute( INTELLIGENZ, SÄURE, 50 ), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getSchamane() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, ELEKTRIZITÄT, 50 ), 2 );
        map.put( new Attribute( INTELLIGENZ, ELEKTRIZITÄT, 50 ), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getApotheker() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, GIFT, 50 ), 2 );
        map.put( new Attribute( INTELLIGENZ, GIFT, 50 ), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getKrieger() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, PHYSISCH, 50 ), 2 );
        map.put( new Attribute( INTELLIGENZ ), 1 );
        map.put( new Attribute( STÄRKE, PHYSISCH, 50 ), 2 );
        return map;
    }

    private static Map<Attribute, Integer> getMagier() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, FEUER, 100 ), 2 );
        map.put( new Attribute( INTELLIGENZ, FEUER, 100 ), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<Attribute, Integer> getSoldat() {
        HashMap<Attribute, Integer> map = new HashMap<>();
        map.put( new Attribute( GESCHICKLICHKEIT, NUKLEAR, 100 ), 2 );
        map.put( new Attribute( INTELLIGENZ, NUKLEAR, 100), 2 );
        map.put( new Attribute( STÄRKE ), 1 );
        return map;
    }

}
