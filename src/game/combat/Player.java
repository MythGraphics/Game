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
import game.resource.Resource;
import java.util.HashMap;
import java.util.Map;

public class Player extends Combatant {

    public final static int XP_PER_LEVEL = 100;

    private final Map<AttributeType, DamageAttribute> attributes;
    private int xp = 0;

    public Player(String name, CombatantType cType, Resource health, int xp) {
        super(name, cType, health);
        this.attributes = new HashMap<>();
        addXP(xp);
        buffDmgAttr();
    }

    public int getXP() {
        return xp;
    }

    public final void addXP(int xp) {
        this.xp += xp;
        setLevel(( byte ) ( this.xp/XP_PER_LEVEL + 1 ));
    }

    public int getAttributeValue(AttributeType aType) {
        return attributes.get(aType).getValue();
    }

    private void buffDmgAttr() {
        Map<DamageAttribute, Integer> lvlBuff = getBuffMap(cType); // Anzahl der Punkte, um die das Attribut pro Level steigt
        for ( DamageAttribute a : lvlBuff.keySet() ) {
            a.setValue( getLevel()*lvlBuff.get( a )); // DamageAttribute an Level anpassen
            attributes.put( a.getType(), a ); // DamageAttribute in sinnige Map ablegen
        }
    }

    @Override
    public void buffDamage(Damage damage) {
        buffDmgAttr();
        for ( HashMap.Entry<AttributeType, DamageAttribute> entry : attributes.entrySet() ) {
            damage.buffDamage( entry.getValue() );
        }
    }

    public Map<AttributeType, DamageAttribute> getAttributes() {
        return attributes;
    }

    private static Map<DamageAttribute, Integer> getBuffMap(CombatantType cType) {
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

    private static Map<DamageAttribute, Integer> getChemiker() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, SÄURE, 50 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ, SÄURE, 50 ), 2 );
        map.put( new DamageAttribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<DamageAttribute, Integer> getSchamane() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, ELEKTRIZITÄT, 50 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ, ELEKTRIZITÄT, 50 ), 2 );
        map.put( new DamageAttribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<DamageAttribute, Integer> getApotheker() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, GIFT, 50 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ, GIFT, 50 ), 2 );
        map.put( new DamageAttribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<DamageAttribute, Integer> getKrieger() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, PHYSISCH, 50 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ ), 1 );
        map.put( new DamageAttribute( STÄRKE, PHYSISCH, 50 ), 2 );
        return map;
    }

    private static Map<DamageAttribute, Integer> getMagier() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, FEUER, 100 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ, FEUER, 100 ), 2 );
        map.put( new DamageAttribute( STÄRKE ), 1 );
        return map;
    }

    private static Map<DamageAttribute, Integer> getSoldat() {
        HashMap<DamageAttribute, Integer> map = new HashMap<>();
        map.put( new DamageAttribute( GESCHICKLICHKEIT, NUKLEAR, 100 ), 2 );
        map.put( new DamageAttribute( INTELLIGENZ, NUKLEAR, 100), 2 );
        map.put( new DamageAttribute( STÄRKE ), 1 );
        return map;
    }

    @Override
    public Combatant clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
