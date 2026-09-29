/*
 *
 */

package audio;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 2.0.0
 *
 */

import java.io.*;
import javax.sound.sampled.*;

public class AudioPlayer implements LineListener, Runnable {

    public final static String NAME     = "MythGraphics AudioPlayer";
    public final static String VERSION  = "2.0.0";

    public final static AudioFormat FFMPEG_FORMAT = new AudioFormat(48000, 16, 2, true, false);

	private volatile boolean stopped    = false;
    private volatile boolean paused     = false;
    private volatile boolean loop       = false;
    private FloatControl volumeControl;
    private String audioFilePath;
    private File audioFile;
    private AudioFormat decodedFormat   = FFMPEG_FORMAT; // fail-safe
    private SourceDataLine line;
    private AudioInputStream audioStream, decodedAudioStream;
    private byte[] audioCache;
    private Process process;

    public static void main(String[] args) throws Exception {
        if ( args == null || args.length == 0 ) {
            System.out.println("Keine Datei zum abspielen übergeben.");
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
        new AudioPlayer(args[0]).run(); // Ausführung im aktuellen Thread in diesem Fall beabsichtigt.
    }

    private static void printHelp() {
        System.out.println("Unterstützt WAV und OGG nativ, alles Weitere via FFmpeg.\n");
        System.out.println("Parameter:");
        System.out.println("  [File]        spielt die Datei ab.");
        System.out.println("  --help        zeigt diese Hilfe an");
        System.out.println("  --version     zeigt Programm-Version an");
        System.out.println();
    }

    public AudioPlayer() {}

    public AudioPlayer(String audioFilePath)
    throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this(audioFilePath, false);
    }

    public AudioPlayer(String audioFilePath, boolean loopEndless)
    throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.loop = loopEndless;
        this.audioFilePath = audioFilePath;
    }

    public static AudioPlayer playAudioFile(String audioFilePath) {
        try {
            AudioPlayer player = new AudioPlayer(audioFilePath);
            player.run();
            return player;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println( "Wiedergabe fehlgeschlagen: " + e.getMessage() );
            return null;
        }
    }

    public static void playOggFile(String audioFilePath) {
        try {
            // Die Datei als AudioInputStream laden
            File audioFile = new File(audioFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Informationen zum Audioformat holen
            AudioFormat format = audioStream.getFormat();
            System.out.println("Audio-Format: " + format);

            // Das gewünschte PCM-Format für die Wiedergabe festlegen
            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // Typ der Codierung
                format.getSampleRate(),          // Abtastrate beibehalten
                16,                              // 16-Bit-Auflösung (Standard für hohe Qualität)
                format.getChannels(),            // Anzahl der Kanäle beibehalten
                format.getChannels() * 2,        // Frame Size (Stereo: 2 Kanäle * 2 Bytes = 4)
                format.getSampleRate(),          // Frame Rate beibehalten
                false                            // Big-Endian-Byte-Ordnung (meist false)
            );

            // Den ursprünglichen Stream in das gewünschte PCM-Format konvertieren
            AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            System.out.println( "Dekodiertes Audio-Format: " + decodedAudioStream.getFormat() );

            // Datenzeile (DataLine) für die Wiedergabe festlegen
            DataLine.Info info = new DataLine.Info( SourceDataLine.class, decodedAudioStream.getFormat() );

            // Eine SourceDataLine für die Wiedergabe erstellen
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            // Line öffnen und Daten empfangen
            line.open( decodedAudioStream.getFormat() );
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;
            // Die Daten vom Stream in die Line schreiben
            while (( bytesRead = decodedAudioStream.read( buffer, 0, buffer.length )) != -1 ) {
                line.write(buffer, 0, bytesRead);
            }
            line.drain(); // Sicherstellen, dass alle Daten wiedergegeben wurden

            // alle Ressourcen zurückgeben
            line.stop();
            line.close();
            decodedAudioStream.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Dateiformat wird nicht unterstützt.");
            System.err.println( e.getMessage() );
        } catch (LineUnavailableException e) {
            System.err.println("Audio-Line nicht verfügbar.");
            System.err.println( e.getMessage() );
        } catch (IOException e) {
            System.err.println("Lesen der Datei fehlgeschlagen.");
            System.err.println( e.getMessage() );
        }
    }

    private AudioInputStream loadAudioStream()
    throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Dateisystem
        if (audioFile == null) {
            audioFile = new File(audioFilePath);
        }
        if ( audioFile.exists() ) {
            return AudioSystem.getAudioInputStream(audioFile);
        }
        // Jar
        if (audioCache == null) {
            InputStream is = getClass().getResourceAsStream(audioFilePath);
            if (is == null) {
                // Fallback: Falls '/' vergessen wurde
                is = getClass().getResourceAsStream("/"+audioFilePath);
            }
            if (is == null) {
                throw new IOException("Audio-Quelle nicht gefunden: " + audioFilePath);
            }
            // den ganzen Quatsch cachen
            audioCache = is.readAllBytes();
            is.close();
        }

        // Erzeuge einen Stream aus dem Cache
        InputStream bais = new ByteArrayInputStream(audioCache);
        // BufferedInputStream ist essenziell für Mark/Reset-Support im AudioSystem
        return AudioSystem.getAudioInputStream( new BufferedInputStream( bais ));
    }

    /**
     * Lautstärke von 0 (stumm) bis 100 (voll).
     * @param volume Lautstärke von 0-100
     */
    public void setVolume(int volume) {
        if (volumeControl == null) {
            return;
        }
        // Begrenzung auf den gültigen Bereich
        int value = Math.max(0, Math.min(100, volume));
        // in linearen Faktor 0.0 bis 1.0 umrechnen
        float linearVolume = value/100.0f;
        /* Umrechnung von linearer Skala in Dezibel: 20*log10(linearVolume)
           In der Akustik entspricht eine Verdoppelung des Schalldrucks einer Erhöhung um 6 dB
           0.0001f verhindert den Logarithmus von 0 (-Unendlich)
         */
        float dB = (float) ( Math.log10( linearVolume <= 0.0f ? 0.0001f : linearVolume ) * 20.0 );
        // Hardware-Limits einhalten (meist -80.0 bis 6.0206)
        dB = Math.max( volumeControl.getMinimum(), Math.min( volumeControl.getMaximum(), dB ));
        volumeControl.setValue(dB);
    }

    /**
     * Gibt die aktuelle Lautstärke als Wert von 0 bis 100 zurück oder
     * -1, wenn nicht unterstützt.
     * @return Lautstärke (0-100)
     */
    public int getVolume() {
        if (volumeControl == null) {
            return -1;
        }
        // Aktuellen dB-Wert von der Hardware abrufen
        float dB = volumeControl.getValue();
        // Umrechnung von Dezibel in linearen Faktor (0.0 bis 1.0)
        // Formel: 10^(dB/20)
        float linearVolume = (float) Math.pow(10.0, dB/20.0);
        // Auf 0-100 skalieren und runden
        int volume = Math.round(linearVolume*100.0f);
        // Sicherheits-Check für die Grenzen
        return Math.max(0, Math.min(100, volume));
    }

    /**
     * Ändert die Lautstärke (-100 bis +100) um diesen Wert.
     * @param amount Lautstärkeänderung
     */
    public void changeVolume(int amount) {
        if (volumeControl == null) {
            return;
        }
        amount = Math.max(-100, Math.min(100, amount));
        setVolume( getVolume()+amount );
        System.out.println( "Current volume: " + getVolume() );
    }

    public void dispose() {
        stop();
        if (line != null) {
            line.stop();
            line.close();
        }
        try {
            if (decodedAudioStream != null) { decodedAudioStream.close(); }
            if (audioStream != null) { audioStream.close(); }
        } catch (IOException e) { /* ignore */ }
        audioCache = null; // Speicher freiräumen
    }

	public void stop() {
		stopped = true;
        paused  = false;
        Thread.currentThread().interrupt(); // Unterbricht den Sleep-Zustand, falls der Thread pausiert ist
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

	@Override
	public void update(LineEvent evt) {
		System.out.println( "Audio-Playback: " + evt.getType() );
	}

    @Override
    public void run() {
        try {
            prepareLine(); // einmalige Vorbereitung der Hardware-Line

            while ((loop && !stopped) || !stopped) {
                // Streams für diesen einen Durchlauf öffnen
                if ( openIterationStreams() ) {
                    try { play(); }
                    finally { closeIterationStreams(); }
                }
                if (!loop) {
                    break;
                }
                // kurze Pause, um CPU-Last bei Fehlern zu begrenzen
                if (!stopped) {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException | LineUnavailableException e) {
            System.err.println( "Fehler bei Audio-Wiedergabe: " + e.getMessage() );
        } finally {
            dispose();
        }
    }

    private void prepareLine() throws LineUnavailableException, IOException {
        // temporären Stream öffnen, um nur das Format zu lesen; bei Fehler FFmpeg nutzen
        try ( AudioInputStream in = loadAudioStream() ) {
            // versuche, natives Format zu erkennen

            if (in == null) {
                throw new IOException("Datei/Quelle nicht gefunden: " + audioFilePath);
            }

            AudioFormat format = in.getFormat();
            System.out.println("Audio-Format: " + format);
            String enc = format.getEncoding().toString().toUpperCase();
            System.out.println("Audio-Encoding: " + enc);

            // Logik für Encoder-Bestimmung
            if ( enc.contains( "VORBIS" )) {
                float sampleRate = format.getSampleRate() > 0 ? format.getSampleRate() : 48000f;
                int channels     = format.getChannels()   > 0 ? format.getChannels()   : 2;
                decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sampleRate,
                    16,
                    channels,
                    channels * 2,
                    sampleRate,
                    false
                );
            } else {
                decodedFormat = format;
            }
        } catch (UnsupportedAudioFileException e) {
            // FFmpeg Fallback
            System.out.println("Nativ nicht unterstützt, wechsle zu FFmpeg ...");
            decodedFormat = FFMPEG_FORMAT;
        }

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        if ( !AudioSystem.isLineSupported( info )) {
            throw new LineUnavailableException("Keine Audio-Line gefunden.");
        }
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(decodedFormat);
        if ( line.isControlSupported( FloatControl.Type.MASTER_GAIN )) {
            volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        }
        line.addLineListener(this);
    }

    private boolean openIterationStreams() {
        try {
            audioStream = loadAudioStream();
            decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            return true;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // Fallback: FFmpeg
            try {
                ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg",
                    "-nostats",                         // Text-Ausgabe reduzieren
                    "-i", audioFile.getAbsolutePath(),  // Quell-Datei
                    "-f", "s16le",                      // PCM signed 16-bit Little Endian
                    "-acodec", "pcm_s16le",
                    "-ac", "2",                         // Stereo
                    "-ar", "48000",                     // Samplerate 48 kHz
                    "-"                                 // Ausgabe an STDOUT
                );
                builder.redirectError(ProcessBuilder.Redirect.INHERIT);
                process = builder.start();
/*              // ErrorStream von FFmpeg an STOUT leiten
                new Thread( () -> {
                    try ( BufferedReader bufReader = new BufferedReader( new InputStreamReader( process.getErrorStream() ))) {
                        String errorLine;
                        while (( errorLine = bufReader.readLine() ) != null ) {
                            System.out.println(errorLine);
                        }
                    } catch (IOException ignored) {}
                }).start();
 */
                // STDOUT von FFmpeg als AudioInputStream tarnen
                InputStream ffmpegOut = process.getInputStream();
                // Wrapper, damit der AudioInputStream weiß, wie viele Bytes ein Frame hat
                decodedAudioStream = new AudioInputStream(ffmpegOut, decodedFormat, AudioSystem.NOT_SPECIFIED);
                return true;
            } catch (IOException ex) {
                System.err.println( "FFmpeg Fallback fehlgeschlagen: " + ex.getMessage() );
                System.err.println( "Stream-Reset fehlgeschlagen: " + e.getMessage() );
                return false;
            }
        }
    }

    private void closeIterationStreams() {
        try {
            if (decodedAudioStream != null) {
                decodedAudioStream.close();
            }
            if (audioStream != null) {
                audioStream.close();
            }
            if (process != null) {
                process.destroy(); // FFmpeg-Prozess hart beenden
                process = null;
            }
        } catch (IOException ignore) {}
    }

    private void play() throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        line.start();

        while (( bytesRead = decodedAudioStream.read( buffer, 0, buffer.length )) != -1 ) {
            if (stopped) {
                break;
            }
            while (paused) {
                line.stop();
                try { Thread.sleep(100); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            if ( !line.isRunning() && !stopped ) {
                line.start();
            }
            line.write(buffer, 0, bytesRead);
        }
        line.drain();
        line.stop(); // stoppen, damit der nächste Loop sauber startet
    }

}
