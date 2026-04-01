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
import graphic.map.MapType;

public class Main {

    public final static String NAME    = "MythGraphics Game";
    public final static String VERSION = "0.0.1 alpha";

    private static String mapType = MapType.SPACE.toString();

    private Main() {}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ( args == null || args.length == 0 ) {
            runGUI(null);
            return;
        }

        if ( !args[0].startsWith( "--" )) {
            mapType = args[0];
        }

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--player" -> {
                    GameFrame.playerName = args[i+1];
                    if ( args.length > i+1 ) {
                        continue;
                    } else {
                        runGUI(null);
                    }
                }
                case "--cmd-input" -> {
                    GameFrame.loadCmdInput = true;
                    if ( args.length > i+1 ) {
                        continue;
                    } else {
                        runGUI(null);
                    }
                }
                case "--gui" -> {
                    runGUI(null);
                }
                case "--map" -> {
                    String[] tileMap = DescriptorLoader.loadMap(args[i+1]);
                    runGUI(tileMap);
                }
                case "--pathfinder" -> {
                    graphic.io.PathFinder.main(null);
                }
                case "--textframe" -> {
                    String[] args2 = new String[args.length-1-i];
                    System.arraycopy(args, i+1, args2, 0, args2.length);
                    graphic.TextFrame.main(args2);
                }
                case "--version" -> System.out.println(NAME + " v" + VERSION);
                default -> {
                    printHelp();
                }
            }
            return; // standardmäßig nach 1xigem Durchlauf der Schleife beenden
        }
    }

    private static void runGUI(String[] tileMap) {
        java.awt.EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                new GameFrame(tileMap, mapType).setVisible(true);
            }
        });
    }

    private static void printHelp() {
        System.out.println("Parameter:");
        System.out.println("  --? / --help");
        System.out.println("  --player [name]");
        System.out.println("  --cmd-input");
        System.out.println("  --gui");
        System.out.println("  --map [map]");
        System.out.println("  --pathfinder");
        System.out.println("  --textframe [text] [title] [img]");
    }

}
