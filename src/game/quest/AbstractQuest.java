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

import game.HasDialog;
import game.HasID;
import game.InteractiveObject;
import game.Message;
import game.item.Item;
import static game.quest.QuestStatus.*;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public static ArrayList<Message> newMessageList( ArrayList<String> list, InteractiveObject source ) {
        return newMessageList( list.get(0), list.get(1), list.get(2), list.get(3), source );
    }

    public static ArrayList<Message> newMessageList( String prolog, String questText, String epilog, String completed,
                                                     InteractiveObject source ) {
        ArrayList<Message> msgList = new ArrayList<>(4);
        msgList.add( new Message( prolog, source ));
        msgList.add( new Message( questText, source ));
        msgList.add( new Message( epilog, source ));
        msgList.add( new Message( completed, source ));
        return msgList;
    }

    abstract ArrayList<Message> getMessageList();
    abstract Item getReward();

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

    public void accept() {
        status = ACTIVE;
    }

    public void cancel() {
        status = INACTIVE;
    }

    public String getQuestText() {
        return getMessageList().get(1).getText();
    }

    @Override
    public LinkedList<Message> getDialog() {
        LinkedList<Message> list = new LinkedList<>();
        switch (status) {
            case INACTIVE:
                list.addAll( getMessageList().subList( 0, 3 ));
                return list;
            case ACTIVE:
                list.addAll( getMessageList().subList( 1, 3 ));
                return list;
            case READY:
            case COMPLETE:
                list.add( getMessageList().get( 3 ));
                return list;
        }
        return null;
    }

    public boolean check(Item questObjective) {
        if (status == READY) {
            return true;
        }
        if ( questObjective == null ||
             questObjective.getId() != getId()
        ) {
            return false;
        }
        status = READY;
        return true;
    }

    /**
     * Spieler erhält das Belohnungsitem.
     * @return Belohnung
     */
    public Item deliver() {
        if ( status == READY ) {
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
