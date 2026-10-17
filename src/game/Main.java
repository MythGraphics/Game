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

import graphic.io.DescriptorLoader;
import static graphic.io.FileExt.MAP;
import graphic.map.TileMap;
import java.awt.EventQueue;

public class Main {

    public final static String NAME    = "MythGraphics Game";
    public final static String VERSION = "0.0.1 alpha";

    public static String defaultMap    = "space";

    private Main() {}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TileMap tileMap = DescriptorLoader.loadMap( MAP.getFilePath( defaultMap ), Main.class );
        if ( args == null || args.length == 0 ) {
            runGUI(tileMap);
            return;
        }

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--player" -> {
                    GameFrame.playerName = args[++i];
                    if (args.length <= i) {
                        runGUI(tileMap);
                    }
                }
                case "--cmd-input" -> {
                    GameFrame.loadCmdInput = true;
                    if (args.length <= i) {
                        runGUI(tileMap);
                    }
                }
                case "--gui" -> {
                    runGUI(tileMap);
                }
                case "--map" -> {
                    tileMap = DescriptorLoader.loadMap(args[++i]);
                    if (args.length <= i) {
                        runGUI(tileMap);
                    }
                }
                case "--defaultMap" -> {
                    defaultMap = args[++i];
                    tileMap = DescriptorLoader.loadMap( MAP.getFilePath( defaultMap ), Main.class );
                    if (args.length <= i) {
                        runGUI(tileMap);
                    }
                }
                case "--pathfinder" -> {
                    graphic.io.PathFinder.main(null);
                    return;
                }
                case "--textframe" -> {
                    String[] args2 = new String[args.length-1-i];
                    System.arraycopy(args, i+1, args2, 0, args2.length);
                    graphic.texter.TextFrame.main(args2);
                    return;
                }
                case "--version" -> {
                    System.out.println(NAME + " v" + VERSION);
                    return;
                }
                default -> {
                    printHelp();
                    return;
                }
            }
        }
    }

    private static void runGUI(TileMap tileMap) {
        if (tileMap != null) {
            EventQueue.invokeLater( () -> {
                new GameFrame(tileMap).setVisible(true);
            });
        } else {
            System.err.println("Kein Map zum Laden angegeben!");
        }
    }

    private static void printHelp() {
        System.out.println(NAME + " v" + VERSION);
        System.out.println("Parameter:");
        System.out.println("  --? / --help");
        System.out.println("  --player [name]");
        System.out.println("  --cmd-input");
        System.out.println("  --gui");
        System.out.println("  --map [map]");
        System.out.println("  --defaultMap [map]");
        System.out.println("  --pathfinder");
        System.out.println("  --textframe [text] [title] [img]");
    }

}
