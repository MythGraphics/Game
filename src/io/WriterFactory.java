/*
 * Java8 compatible
 */

package io;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 2.3.0
 *
 */

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipOutputStream;

public class WriterFactory {

    private WriterFactory() {}

    public static BufferedOutputStream getBinaryWriter(OutputStream target) {
        return new BufferedOutputStream(target);
    }

    public static BufferedOutputStream getBinaryWriter(File target) throws IOException {
        return new BufferedOutputStream( new FileOutputStream( target ));
    }

    public static BufferedOutputStream getBinaryWriter(Socket target) throws IOException {
        return new BufferedOutputStream( target.getOutputStream() );
    }

    public static ZipOutputStream getZipWriter(File zipfile) throws IOException {
        return new ZipOutputStream( new FileOutputStream( zipfile ));
    }

    public static PrintWriter getPipedTextWriter(PipedReader target) throws IOException {
        return new PrintWriter( new PipedWriter( target ));
    }

    public static PrintWriter getPipedTextWriter(PipedWriter target) {
        return new PrintWriter(target);
    }

    public static PrintStream getPipedTextStreamWriter() {
        return new PrintStream( new PipedOutputStream() );
    }

    public static PrintStream getPipedTextStreamWriter(PipedInputStream target) throws IOException {
        return new PrintStream( new PipedOutputStream( target ));
    }

    public static PrintWriter getTextWriter(OutputStream target) {
        return new PrintWriter( new OutputStreamWriter( target, StandardCharsets.UTF_8 ));
    }

    public static PrintWriter getTextWriter(File target) throws IOException {
        return new PrintWriter( Files.newBufferedWriter( target.toPath(), StandardCharsets.UTF_8 ));
    }

    public static PrintWriter getTextWriter(Socket target) throws IOException {
        return new PrintWriter( new OutputStreamWriter( target.getOutputStream(), StandardCharsets.UTF_8 ));
    }

    public static void write(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[8192];
        int nbytes;
        try {
            while (( nbytes = in.read( buffer )) != -1 ) {
                out.write(buffer, 0, nbytes);
            }
            out.flush();
        }
        finally {
            close(in, out);
        }
    }

    public static void write(BufferedReader in, PrintWriter out) throws IOException {
        try {
            String line;
            while (( line = in.readLine() ) != null ) {
                out.println(line);
            }
            out.flush();
        } finally {
            close(in, out);
        }
    }

    private static void close(Closeable in, Closeable out) throws IOException {
        in.close();
        out.close();
    }

}
