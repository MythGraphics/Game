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

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.JFrame;

public class ConsoleInputController {

    private final JFrame source;
    private final Map<String, Consumer<String>> commands = new HashMap<>();

    public ConsoleInputController(JFrame source) {
        this.source = source;
        setupDefaultCommands();
        Thread inputThread = new Thread(this::runReader);
        inputThread.setDaemon(true); // beendet sich, wenn die GUI schließt
        inputThread.start();
    }

    private void setupDefaultCommands() {
        addCommand( "close", arg -> source.dispose() );
        addCommand( "exit", arg -> source.dispose() );
        addCommand( "help", arg -> System.out.println( "Befehle: " + commands.keySet() ));
    }

    public void addCommand(String name, Consumer<String> action) {
        commands.put( name.toLowerCase(), action );
    }

    private void runReader() {
        Scanner scanner = new Scanner(System.in);
        while ( scanner.hasNextLine() ) {
            String input = scanner.nextLine();
            processInput(input);
        }
    }

    private void processInput(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String args = "";
        if ( parts.length > 1 ) {
            args = parts[1];
        }
        // String args = parts.length > 1 ? parts[1] : "";
        if ( commands.containsKey( cmd )) {
            commands.get(cmd).accept(args);
        } else {
            System.out.println( "\"" + cmd + "\" unbekannt -> ignoriert.");
        }
    }

}
