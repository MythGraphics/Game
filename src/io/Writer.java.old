/*
 *
 */

package io;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 2.2.1
 *
 */

import java.io.*;
import java.net.Socket;
import java.util.zip.ZipOutputStream;

public final class Writer {

    private Writer() {}

    public static BufferedOutputStream getBinaryWriter(OutputStream target) {
        return new BufferedOutputStream(target);
    }

    public static BufferedOutputStream getBinaryWriter(File target) throws IOException {
        return new BufferedOutputStream( new FileOutputStream(target) );
    }

    public static BufferedOutputStream getBinaryWriter(Socket target) throws IOException {
        return new BufferedOutputStream( target.getOutputStream() );
    }

    public static ZipOutputStream getZipWriter(File zipfile) throws IOException {
        return new ZipOutputStream( new FileOutputStream(zipfile) );
    }

    public static PrintWriter getPipedTextWriter(PipedReader target) throws IOException {
        return new PrintWriter( new PipedWriter(target) );
    }

    public static PrintWriter getPipedTextWriter(PipedWriter target) {
        return new PrintWriter(target);
    }

    public static PrintStream getPipedTextStreamWriter() {
        return new PrintStream( new PipedOutputStream() );
    }

    public static PrintStream getPipedTextStreamWriter(PipedInputStream target) throws IOException {
        return new PrintStream( new PipedOutputStream(target) );
    }

    public static PrintWriter getTextWriter(OutputStream target) {
        return new PrintWriter(target);
    }

    public static PrintWriter getTextWriter(File target) throws IOException {
        return new PrintWriter(target);
    }

    public static PrintWriter getTextWriter(Socket target) throws IOException {
        return new PrintWriter( target.getOutputStream() );
    }

    public static void write(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[IO.BUFFERSIZE];
        int nbytes;
        while (( nbytes = in.read(buffer) ) != -1 ) {
            out.write(buffer, 0, nbytes);
        }
        out.flush();
        close(in, out);
    }

    public static void write(BufferedReader in, PrintWriter out) throws IOException {
        while ( in.ready() ) {
            out.println( in.readLine() );
        }
        out.flush();
        close(in, out);
    }

    private static void close(Closeable in, Closeable out) throws IOException {
        // wartet komplette Übertragung ab
        try { Thread.sleep(500); }
        catch (InterruptedException e) {}

        in.close();
        out.close();
    }

}
