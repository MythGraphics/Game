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

import static game.GameObject.*;
import game.combat.CombatFactory.Group;
import game.combat.*;
import game.item.Item;
import game.item.ItemActionType;
import static game.item.ItemActionType.FIND;
import static game.item.ItemActionType.USE;
import game.item.ReUsableItem;
import game.item.UsableItem;
import game.quest.Quest;
import static graphic.io.BinaryIO.loadImage;
import static graphic.io.ImageUtility.scale;
import graphic.io.TextIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static util.EnumHelper.getEnumFromString;

public class GameObjectLoader {

    public record PropertiesHelper(int index, boolean isPlayer, ItemActionType itemActionType, String value)
    implements Comparable<PropertiesHelper> {
        @Override
        public int compareTo(PropertiesHelper other) {
            return Integer.compare(this.index, other.index);
        }
    }

    public final static String FILEXT = ".properties";

    // Gruppe 1: (\\d*) fängt die Ziffern, Gruppe 2: (player)? fängt das Wort
    public final static Pattern PATTERN_FIND = Pattern.compile("^onFind(\\d*)(player)?$");
    public final static Pattern PATTERN_USE  = Pattern.compile("^onUse(\\d*)(player)?$");

    private final Class<?> clazz;

    public GameObjectLoader(Class<?> clazz) {
        this.clazz = clazz;
    }

    private static String buildFileString(GameObject gObj, int id) {
        return gObj.toString() + id + FILEXT;
    }

    public Properties loadProperties(String filename) throws IOException {
        return TextIO.loadProperties(filename, clazz);
    }

    public Item loadNextItem(Player player) throws IOException {
        int id = ID.getNextItemId();
        Properties p = loadProperties( buildFileString( ITEM, id ));
        String type = p.getProperty("type");
        Item item;
        switch ( type.toLowerCase() ) {
            case "usable"   -> item = new UsableItem( id, p.getProperty( "name" ));
            case "reusable" -> item = new ReUsableItem( id, p.getProperty( "name" ));
            default         -> item = new Item( id, p.getProperty( "name" ));
        }
        if ( p.containsKey( "description" )) {
            item.setDescription( p.getProperty( "description" ));
        }
        item.setPrice( Integer.parseInt( p.getProperty( "price" )));
        item.setImg( scale( loadImage( p.getProperty( "img" )), 200 ));
        item.setIcon( loadImage( p.getProperty( "uiImg" )));

        List<PropertiesHelper> helperList = new ArrayList<>();
        for ( Map.Entry<Object, Object> entry : p.entrySet() ) {
            String key = entry.getKey().toString();
            Matcher matcherFind = PATTERN_FIND.matcher(key);
            Matcher matcherUse  = PATTERN_USE.matcher(key);
            if ( matcherFind.matches() ) {
                helperList.add( getHelper( entry, matcherFind, FIND ));
            } else if ( matcherUse.matches() ) {
                if ( !( item instanceof UsableItem )) {
                    System.err.println( "\"onUse\" auf Item-Klasse " + item.getClass() + " nicht anwendbar." );
                    continue;
                }
                helperList.add( getHelper( entry, matcherUse, USE ));
            }
        }
        Collections.sort(helperList);
        for (PropertiesHelper helper : helperList) {
            switch (helper.itemActionType) {
                case FIND ->
                    item.addMessageOnFind( new Message( helper.value(), helper.isPlayer() ? player : item ));
                case USE ->
                    ((UsableItem) (item)).addMessageOnUse(
                        new Message( helper.value(), helper.isPlayer() ? player : item )
                    );
            }
        }

        return item;
    }

    private PropertiesHelper getHelper(Map.Entry<Object, Object> entry, Matcher matcher, ItemActionType type) {
        // Ziffer auslesen (wenn keine da ist, -1 für die Reihenfolge)
        String digits = matcher.group(1);
        int index = digits.isEmpty() ? -1 : Integer.parseInt(digits);

        // prüfen ob "player" gematcht hat
        boolean isPlayer = matcher.group(2) != null;
        String value = entry.getValue().toString();

        return new PropertiesHelper( index, isPlayer, type, value );
    }

    public Npc loadNextNpc(Player player) throws IOException {
        int id = ID.getNextNpcId();
        Properties p = loadProperties( buildFileString( NPC, id ));
        Npc npc = new Npc( id, p.getProperty( "name" ));
        npc.setImg( scale( loadImage( p.getProperty( "img" )), 200 ));
        npc.getDialog().add( new Message( p.getProperty( "text" ), npc ));
        return npc;
    }

    public TextBox loadNextDialog(Player player) throws IOException {
        int id = ID.getNextSignId();
        Properties p = loadProperties( buildFileString( TEXT, id ));
        TextBox text = new TextBox(
            id,
            p.getProperty("name"),
            p.getProperty("text")
        );
        text.setImg( scale( loadImage( p.getProperty( "img" )), 200 ));
        loadText(p, text, player);
        return text;
    }

    public AbstractWeapon loadNextWeapon() throws IOException {
        int id = ID.getNextWeaponId();
        Properties p = loadProperties( buildFileString( WEAPON, id ));
        String name = p.getProperty("name");
        String description = p.getProperty("description");
        BufferedImage img = scale( loadImage( p.getProperty( "img" )), 100 );
        WeaponType wType = getEnumFromString( WeaponType.class, p.getProperty( "type" ));
        AbstractWeapon w;
        if ( wType.needAmmo() ) {
            AmmoType aType = getEnumFromString( AmmoType.class, p.getProperty( "needAmmo" ));
            w = new AmmoWeapon(id, name, wType, aType);
        } else {
            int dmg = Integer.parseInt( p.getProperty( "dmg" ));
            DamageType dType = getEnumFromString( DamageType.class, p.getProperty( "dmgtype" ));
            w = new Weapon(id, name, wType, dType, dmg);
        }
        w.setDescription(description);
        w.setImg(img);
        return w;
    }

    public Ammo loadNextAmmo() throws IOException {
        // ToDo Ammo-Id fehlt in der Ammo-Klasse
        int id = ID.getNextAmmoId();
        Properties p = loadProperties( buildFileString( AMMO, id ));
        String name = p.getProperty("name");
        String description = p.getProperty("description");
        AmmoType aType = getEnumFromString( AmmoType.class, p.getProperty( "type" ));
        DamageType dType = getEnumFromString( DamageType.class, p.getProperty( "dmgtype" ));
        int size = Integer.parseInt( p.getProperty( "size" ));
        int dmg = Integer.parseInt( p.getProperty( "dmg" ));
        BufferedImage img = scale( loadImage( p.getProperty( "img" )), 100 );
        Ammo ammo = new Ammo( name, aType, size, 0, new Damage( dType, dmg ));
        ammo.setImg(img);
        ammo.setDescription(description);
        return ammo;
    }

    public Enemy loadRandomEnemy() throws IOException, NoSuchElementException {
        int lvl = ID.getNextEnemyId(); // als ID und Level
        Properties p = loadProperties( buildFileString( ENEMY, 0 )); // ID=0
        Group group  = getEnumFromString( Group.class, p.getProperty( "group" ));
        Combatant minion = CombatFactory.createRandomEnemy(group);
        String name = p.getProperty( "name", minion.getName() );
        BufferedImage img = scale( loadImage( p.getProperty( "img" )), 100 );
        if (img == null) {
            img = minion.getImg();
        }
        Enemy e = new Enemy(lvl, name, (game.combat.Enemy) minion );
        e.getMinion().setLevel((byte) lvl);
        e.setImg(img);
        return e;
    }

    public Enemy loadNextEnemy() throws IOException, NoSuchElementException {
        int id = ID.getNextEnemyId();
        Properties p = loadProperties( buildFileString( ENEMY, id ));
        Group group  = getEnumFromString( Group.class, p.getProperty( "group" ));
        int lvl = Integer.parseInt( p.getProperty( "lvl", "1" ));
        int minionId = Integer.parseInt( p.getProperty( "minion-id", "-2" ));
        Combatant minion = null;
        if (minionId >= 0) {
            minion = loadMinion( loadProperties( buildFileString( MINION, id )));
        }
        if (minion == null) {
            minion = CombatFactory.createRandomEnemy(group);
        }
        minion.setLevel((byte) lvl);
        String name = p.getProperty( "name", minion.getName() );
        BufferedImage img = scale( loadImage( p.getProperty( "img" )), 200 );
        if (img == null) {
            img = minion.getImg();
        }
        if ( minion instanceof game.combat.Enemy enemy ) {
            Enemy e = new Enemy(id, name, enemy);
            e.setImg(img);
            return e;
        } else {
            throw new NoSuchElementException(
                "Einheit " + minion.getName() + " hat falschen Typ.\n" +
                "Erwartet:  game.combat.Enemy\n" +
                "Ermittelt: " + minion.getClass().getName()
            );
        }
    }

    public Combatant loadNextMinion() throws IOException, NoSuchElementException {
        int id = ID.getNextMinionId();
        Properties p = loadProperties( buildFileString( MINION, id ));
        return loadMinion(p);
    }

    private static Combatant loadMinion(Properties p) throws IOException, NoSuchElementException {
        Combatant minion;
        if ( p.containsKey( "default" )) {
            minion = getEnumFromString( CombatFactory.DefaultEnemy.class, p.getProperty( "default" )).create();
        } else {
            minion = new Combatant(
                p.getProperty( "name" ),
                getEnumFromString( CombatantType.class, p.getProperty( "type" ))
            );
        }
        minion.setLevel( Byte.parseByte( p.getProperty( "lvl" )));
        if ( p.containsKey( "img" )) {
            // optional, da auch über das Default-Objekt das Bild geliefert werden kann
            minion.setImg( scale( loadImage( p.getProperty( "img" )), 100 ));
        }
        return minion;
    }

    public Point loadNextPortal() throws IOException {
        int id = ID.getNextPortalId();
        Properties p = loadProperties( buildFileString( PORTAL, id ));
        try {
            int x = Integer.parseInt( p.getProperty( "dest_x" ));
            int y = Integer.parseInt( p.getProperty( "dest_y" ));
            return new Point(x, y);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public Item loadQuest(int id, Npc questProvider, Item questItem, Player player) throws IOException {
        Properties p = loadProperties( buildFileString( QUEST, id ));
        ArrayList<String> list = new ArrayList<>(5);
        list.add( p.getProperty( "quest_prolog" ));
        list.add( p.getProperty( "quest_text" ));
        list.add( p.getProperty( "quest_epilog" ));
        list.add( p.getProperty( "quest_complete" ));
        list.add( p.getProperty( "questobj_found" ));
        questProvider.setQuest( new Quest(
            id,
            Quest.newMessageList( list, questProvider ),
            questItem
        ));
        Item qObj = questProvider.getQuest().getQuestObjective();
        qObj.addMessage( new Message( list.get(4), player ));
        return qObj;
    }

    public void loadQuestObjectiveDialog(Item qObj, Player player) throws IOException {
        Properties p = loadProperties( buildFileString( QUEST_OBJECTIVE, qObj.getId() ));
        qObj.setImg( scale( loadImage( p.getProperty( "img" )), 200 ));
        loadText(p, qObj, player);
    }

    private static void loadText(Properties p, TextBox gObj, Player player) {
        for (int i = 0; p.containsKey( String.valueOf( i )) || p.containsKey(i+"p"); ++i) {
            try { gObj.addMessage( new Message( p.getProperty( String.valueOf( i )), gObj )); }
            catch (NullPointerException e) {}
            try { gObj.addMessage( new Message( p.getProperty( i+"p" ), player )); }
            catch (NullPointerException e) {}
        }
    }

}
