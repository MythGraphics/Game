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
import java.util.Arrays;
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

    public Item getQuestObjective() {
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
            case READY, COMPLETE -> {
                list.add( getMessageList().get( 3 ));
                yield list;
            }
        };
    }

    public boolean check(Item... questObjectives) {
        return check( Arrays.asList( questObjectives ));
    }

    public boolean check(List<Item> questObjectives) {
        if (status == READY) {
            return true;
        }
        if ( questObjectives == null || questObjectives.isEmpty() ) {
            return false;
        }
        for (Item i : questObjectives) {
            if ( i.getId() == getId() ) {
                // questId stimmt mit itemId überein
                status = READY;
                return true;
            }
        }
        return false;
    }

    /**
     * Spieler erhält das Belohnungsitem.
     * @return Belohnung
     */
    public Item deliver() {
        if (status == READY) {
            status = COMPLETE;
            return getReward();
        }
        return null;
    }

    /**
     * Spieler erhält das Belohnungsitem sofort mit auffinden des Quest Objektes.
     * @param questObject QuestObjective
     * @return Belohnung
     */
    public Item deliver(Item questObject) {
        if ( check( questObject )) {
            return deliver();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Quest #" +
               getId() +
               ", QuestObjective: " +
               getQuestObjective() +
               ", " +
               getReward()
        ;
    }

}
