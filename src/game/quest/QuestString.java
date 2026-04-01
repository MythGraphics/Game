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

public enum QuestString {

    PROLOG      ("quest_prolog"),
    TEXT        ("quest_text"),
    EPILOG      ("quest_epilog"),
    COMPLETE    ("quest_complete"),
    OBJ_FOUND   ("questobj_found");

    private final String name;

    QuestString(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
