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
import game.combat.CombatFactory;
import game.combat.Combatant;
import game.combat.CombatantType;
import game.item.*;
import game.quest.Quest;
import static graphic.io.BinaryIO.BINARYIO;
import static graphic.io.ImageUtility.scale;
import static graphic.io.TextIO.TEXTIO;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;

public class GameObjectLoader {

    private final static String FILEXT  = ".properties";

    private GameObjectLoader() {}

    private static String buildFileString(GameObject gobj, int id) {
        return gobj.toString() + id + FILEXT;
    }

    public static Properties loadProperties(String filepath) throws IOException {
        Properties p = new Properties();
        try (BufferedReader stream = TEXTIO.getTextReader( filepath )) {
            p.load(stream);
        }
        return p;
    }

    public static Item loadNextItem(Player player) throws IOException {
        int id = ID.getNextItemID();
        Properties p = loadProperties( buildFileString( ITEM, id ));
        Item item;
        String type = p.getProperty("type");
        switch ( type.toLowerCase() ) {
            case "usable"   -> item = new UsableItem( id, p.getProperty( "name" ));
            case "reusable" -> item = new ReUsableItem( id, p.getProperty( "name" ));
            default         -> item = new Item( id, p.getProperty( "name" ));
        }
        item.setPrice( Integer.parseInt( p.getProperty( "price" )));
        item.setImg( scale( BINARYIO.loadImage( p.getProperty( "img" )), 200 ));
        item.setIcon( BINARYIO.loadImage( p.getProperty( "uiImg" )));
        if (item instanceof UsableItem uitem) {
            try { uitem.addMessageOnUse( new Message( p.getProperty( "onUse" ), item )); }
            catch (NullPointerException e) {}
            for (int i = 0; i < 10; ++i) {
                try { uitem.addMessageOnUse( new Message( p.getProperty( i+"u" ), item )); }
                catch (NullPointerException e) {}
                try { uitem.addMessageOnUse( new Message( p.getProperty( i+"up" ), player )); }
                catch (NullPointerException e) {}
            }
        }
        try { item.addMessageOnFind( new Message( p.getProperty( "onFind" ), item )); }
        catch (NullPointerException e) {}
        for (int i = 0; i < 10; ++i) {
            try { item.addMessageOnFind( new Message( p.getProperty( i+"f" ), item )); }
            catch (NullPointerException e) {}
            try { item.addMessageOnFind( new Message( p.getProperty( i+"fp" ), player )); }
            catch (NullPointerException e) {}
        }
        return item;
    }

    public static Npc loadNextNpc(Player player) throws IOException {
        int id = ID.getNextNpcID();
        Properties p = loadProperties( buildFileString( NPC, id ));
        Npc npc = new Npc( id, p.getProperty( "name" ));
        npc.setImg( scale( BINARYIO.loadImage( p.getProperty( "img" )), 200 ));
        npc.getDialog().add( new Message( p.getProperty( "text" ), npc ));
        return npc;
    }

    public static TextBox loadNextDialog(Player player) throws IOException {
        int id = ID.getNextSignID();
        Properties p = loadProperties( buildFileString( TEXT, id ));
        TextBox text = new TextBox(
            id,
            p.getProperty( "name" ),
            p.getProperty( "text" )
        );
        text.setImg( scale( BINARYIO.loadImage( p.getProperty( "img" )), 200 ));
        loadText(p, text, player);
        return text;
    }

    public static Enemy loadNextEnemy() throws IOException, NoSuchElementException {
        int id = ID.getNextEnemyID();
        Properties p = loadProperties( buildFileString( ENEMY, id ));
        Combatant minion = loadMinion(p);
        if ( minion instanceof game.combat.Enemy e ) {
            return new Enemy( id, minion.getName(), e );
        } else if (minion != null) {
            throw new NoSuchElementException(
                "Einheit " + minion.getName() + " hat falschen Typ.\n" +
                "Erforderlich: game.combat.Enemy\n" +
                "Ermittelt: " + minion.getClass().getName()
            );
        } else {
            return null;
        }
    }

    public static Combatant loadNextMinion() throws IOException {
        int id = ID.getNextMinionID();
        Properties p = loadProperties( buildFileString( MINION, id ));
        return loadMinion(p);
    }

    private static Combatant loadMinion(Properties p) throws IOException {
        Combatant minion;
        if ( p.containsKey( "default" )) {
            minion = CombatFactory.DefaultMinion.getByName( p.getProperty( "default" )).orElseThrow().getMinion();
        } else {
            minion = new Combatant(
                p.getProperty( "name" ),
                CombatantType.getByName( p.getProperty( "type" )).orElseThrow()
            );
        }
        minion.setLevel( Byte.parseByte( p.getProperty( "lvl" )));
        if ( p.containsKey( "img" )) {
            // optional, da auch über das Default-Objekt das Bild geliefert werden kann
            minion.setImg( scale( BINARYIO.loadImage( p.getProperty( "img" )), 100 ));
        }
        return minion;
    }

    public static Point loadNextPortal() throws IOException {
        int id = ID.getNextPortalID();
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

    public static Item loadQuest(int id, Npc questProvider, Item questItem, Player player) throws IOException {
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

    public static void loadQuestObjectiveDialog(Item qObj, Player player) throws IOException {
        Properties p = loadProperties( buildFileString( QUEST_OBJECTIVE, qObj.getId() ));
        qObj.setImg( scale( BINARYIO.loadImage( p.getProperty( "img" )), 200 ));
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
