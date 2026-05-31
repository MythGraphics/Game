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

import game.Message;
import game.item.IsItem;
import java.util.List;

public class Quest extends AbstractQuest {

    private final List<Message> msgList;
    private final IsItem reward;

    public Quest(int id, List<Message> msgList, IsItem reward) {
        super(id);
        this.msgList = msgList;
        this.reward  = reward;
    }

    @Override
    public void accept() {
        super.accept();
        System.out.println( "Quest #" + getId() + " angenommen." ); // debug
    }

    @Override
    List<Message> getMessageList() {
        return msgList;
    }

    @Override
    IsItem getReward() {
        return reward;
    }

}
