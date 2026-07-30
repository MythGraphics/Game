/*
 *
 */

package graphic.texter;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ChoiceFrame<T> extends TextFrame {

    private final ChoiceDialog<T> choice;

    @SafeVarargs
    public ChoiceFrame(boolean standalone, T... options) {
        this( standalone, options != null ? Arrays.asList(options) : List.of() );
    }

    public ChoiceFrame(boolean standalone, List<T> options) {
        this(standalone, DEFAULT_SIZE, DEFAULT_IMG_SIZE, DEFAULT_TEXT_POS, options);
    }

    @SafeVarargs
    public ChoiceFrame(boolean standalone, Dimension size, Dimension imgSize, Point textPos, T... options) {
        this( standalone, size, imgSize, textPos, options != null ? Arrays.asList(options) : List.of() );
    }

    public ChoiceFrame(boolean standalone, Dimension size, Dimension imgSize, Point textPos, List<T> options) {
        super(standalone, size, imgSize, textPos);
        this.choice = new ChoiceDialog<>(this, options);
    }

    public static void main(String[] args) {
        if ( args == null || args.length == 0 ) {
            System.out.println("Optionen:");
            System.out.println("  --title[Titel]");
            System.out.println("  --img[Bilddatei]");
            System.out.println("Argumente:");
            System.out.println("  Text");
            System.out.println("  Auswahl (getrennt von Leerzeichen)");
            return;
        }

        String title = "", text = "", imgpath = "";
        int i = 0;
        for (; i < args.length; ++i) {
            if ( args[i].startsWith( "--" )) {
                switch (args[i]) {
                    case "--title" -> title   = args[i].substring(7);
                    case "--img"   -> imgpath = args[i].substring(5);
                }
            } else {
                text = args[i];
                break;
            }
        }
        String[] options = Arrays.copyOfRange(args, i+1, args.length-1);

        ChoiceFrame<String> choiceFrame = new ChoiceFrame<>(true, options);
        choiceFrame.show(title, text, imgpath);
    }

    public ChoiceDialog getChoiceDialog() {
        return choice;
    }

    @Override
    public void show(LinkedList<Message> dialog) {
        super.show(dialog);
        choice.setVisible(true);
    }

    @Override
    public void show(Message msg) {
        super.show(msg);
        choice.setVisible(true);
    }

/*  nicht sinnvoll / funktional
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        choice.setVisible(visible);
    }
 */

}
