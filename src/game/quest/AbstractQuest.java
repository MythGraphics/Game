/*
 *
 */

package game.quest;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.HasID;
import game.InteractiveObject;
import game.item.Item;
import static game.quest.QuestStatus.*;
import graphic.texter.HasDialog;
import graphic.texter.Message;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractQuest implements HasDialog, HasID {

    private final int id;
    private final Item objective;

    private QuestStatus status = INACTIVE;

    public AbstractQuest(int id) {
        this(id, "Quest Objective");
    }

    public AbstractQuest(int id, String objectiveName) {
        this.id = id;
        objective = new Item( id, objectiveName + " #" + id );
    }

    abstract List<Message> getMessageList();
    abstract Item getReward();

    public static List<Message> newMessageList(ArrayList<String> list, InteractiveObject source) {
        return newMessageList( list.get(0), list.get(1), list.get(2), list.get(3), source );
    }

    public static List<Message> newMessageList(
        String prolog, String questText, String epilog, String completed, InteractiveObject source
    ) {
        ArrayList<Message> msgList = new ArrayList<>(4);
        msgList.add( new Message( prolog, source ));
        msgList.add( new Message( questText, source ));
        msgList.add( new Message( epilog, source ));
        msgList.add( new Message( completed, source ));
        return msgList;
    }

    public Item getObjective() {
        return objective;
    }

    @Override
    public int getId() {
        return id;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public boolean accept() {
        if (status == INACTIVE) {
            status = ACTIVE;
            System.out.println( "Quest #" + getId() + " angenommen." ); // debug
            return true;
        }
        return false;
    }

    public void cancel() {
        if (status == ACTIVE) {
            status = INACTIVE;
        }
    }

    public String getQuestText() {
        return getMessageList().get(1).getText();
    }

    @Override
    public LinkedList<Message> getDialog() {
        LinkedList<Message> list = new LinkedList<>();
        return switch (status) {
            case INACTIVE -> {
                list.addAll( getMessageList().subList( 0, 3 ));
                yield list;
            }
            case ACTIVE -> {
                list.addAll( getMessageList().subList( 1, 3 ));
                yield list;
            }
            case COMPLETE -> {
                list.add( getMessageList().get( 3 ));
                yield list;
            }
            case DELIVERED -> null;
        };
    }

    public boolean isQuestObjective(Item item) {
        // QuestObjective und item zeigen auf das selbe Objekt oder itemId stimmt mit questId überein
        return item == objective || item.getId() == getId();
    }

    public void update(Item item) {
        if ( isQuestObjective( item )) {
            status = COMPLETE;
        }
    }

    /**
     * Spieler erhält die Belohnung.
     * @return Belohnung
     */
    public Item deliver() {
        if (status == COMPLETE) {
            status = DELIVERED;
            return getReward();
        }
        return null;
    }

    /**
     * Spieler erhält das Belohnungsitem sofort mit auffinden des Quest Objektes.
     * @param item QuestObjective
     * @return Belohnung
     */
    public Item deliver(Item item) {
        update(item);
        if (status == COMPLETE) {
            return deliver();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Quest #" +
               getId() +
               ", QuestObjective: " +
               getObjective() +
               ", " +
               getReward()
        ;
    }

}
