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

import graphic.texter.Message;
import game.item.Item;
import java.util.List;

public class Quest extends AbstractQuest {

    private final List<Message> msgList;
    private final Item reward;

    public Quest(int id, List<Message> msgList, Item reward) {
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
    Item getReward() {
        return reward;
    }

}
