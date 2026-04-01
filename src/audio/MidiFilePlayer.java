/*
 *
 */

package audio;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 0.0.0
 *
 */

import java.io.File;
import java.io.IOException;
import javax.sound.midi.*;

public class MidiFilePlayer {

    public final static String NAME    = "MythGraphics MidiFilePlayer";
    public final static String VERSION = "1.0.0";

    private Sequencer sequencer;
    private Sequence seq;

    public static void main(String[] args) {
        if ( args == null || args.length == 0 ) {
            System.out.println("Keine Datei zum abspielen.");
            return;
        }
        if ( args[0].equalsIgnoreCase( "--help" )) {
            printHelp();
            return;
        }
        if ( args[0].equalsIgnoreCase( "--version" )) {
            System.out.println(NAME + " v" + VERSION);
            return;
        }

        new MidiFilePlayer(args);
    }

    public MidiFilePlayer(String[] args) {
        playMidiFile( new File( args[0] ));
    }

    public MidiFilePlayer(File midiFile) {
        init(midiFile);
    }

    public final void start() {
        if ( sequencer == null ) {
            return;
        }
        sequencer.start();
    }

    public final void pause() {
        if ( sequencer == null ) {
            return;
        }
        sequencer.stop();
    }

    public final void stop() {
        pause();
        dispose();
    }

    public void dispose() {
        if ( sequencer == null ) {
            return;
        }
        sequencer.close();
        sequencer = null;
    }

    public final void setLoop(int count) {
        if ( sequencer == null ) {
            return;
        }
        sequencer.setLoopCount(count);
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    private void init(File midiFile) {
        if ( sequencer != null ) {
            System.err.println("Sequencer is already running.");
            return;
        }
        /* A sequencer is an interface that allows you to play, stop, skip and various
         * other operations to be performed on time-stamped MIDI data.
         * A hardware or software device that plays back a MIDI sequence is known as a sequencer.
         */
        try {
            sequencer = MidiSystem.getSequencer();
            if ( sequencer == null ) {
                System.err.println("Error in loading sequencer.");
                return;
            }
            sequencer.open();
        } catch (MidiUnavailableException e) {
            System.err.println( e.getMessage() );
        }
        // load file in sequencer
        try {
            seq = MidiSystem.getSequence(midiFile);
            /* Tie together an existing sequence with the sequencer,
             * which is somewhat analogous to loading a tape onto a tape recorder.
             */
            sequencer.setSequence(seq);
        } catch (IOException | InvalidMidiDataException e) {
            System.err.println( e.getMessage() );
        }
    }

    private void playMidiFile(File midiFile) {
        init(midiFile);
        start();
        while ( sequencer.isRunning() ) {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        stop();
    }

    private static void printHelp() {
        System.out.println("Parameter:");
        System.out.println("  [File]        spielt die Datei ab.");
        System.out.println();
        System.out.println("  --help        zeigt diese Hilfe an");
        System.out.println("  --version     zeigt Programm-Version an");
        System.out.println();
    }

}
