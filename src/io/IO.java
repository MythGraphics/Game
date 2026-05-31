/*
 *
 */

package io;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 2.3.1
 *
 */

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class IO {

    /**
     * Stream --> eine Folge von Bytes --> wenn Char-Encoding notwenig
     * Writer --> eine Folge von Zeichen
     */

    public final static int BUFFERSIZE = 0x80000; // 512 kB
    public final static int TIMEOUT    = 60*1000; // 60 sec

    private IO() {}

    public static void copy(File source, File target) throws IOException {
        if ( !target.exists() ) { target.createNewFile(); }
        Writer.write( new FileInputStream(source), new FileOutputStream(target) );
    }

    public static void copy(String sourceFile, String targetFile) throws IOException {
        Writer.write( new FileInputStream(sourceFile), new FileOutputStream(targetFile) );
    }

    /*
     * File will be overridden
     */
    public static boolean writeToFile(String line, File target) {
        return writeToFile(line, target, false);
    }

    public static boolean appendToFile(String line, File target) {
        return writeToFile(line, target, true);
    }

    public static boolean writeToFile(String line, File target, boolean append) {
        PrintWriter out = null;
        boolean state = false;
        try {
            out = new PrintWriter( new FileWriter(target, append), true );                                              // autoflush
            out.println(line);
            state = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            state = false;
        }
        finally {
            if (out != null) {out.close();}
        }
        return state;
    }

    public static boolean zip(File zip, File source) {
        FileInputStream in  = null;
        ZipOutputStream out = null;
        boolean state = false;
        try {
            out = Writer.getZipWriter(zip);
            in  = new FileInputStream(source);
            out.putNextEntry( new ZipEntry( source.getName() ));

            byte buffer[] = new byte[IO.BUFFERSIZE];
            int nbytes;
            while (( nbytes = in.read(buffer) ) != -1 ) {
                out.write(buffer, 0, nbytes);
            }
            out.flush();

            state = true;
        } catch (IOException e) {
            e.printStackTrace();
            state = false;
        } finally {
            try {
                if (in  != null) {in.close(); }
                if (out != null) {out.close();}
            } catch (IOException e) { e.printStackTrace(); }
        }
        return state;
    }

    public static boolean extract(ZipFile zip, ZipEntry source, File target) {
        InputStream in = null;
        boolean state = false;
        try {
            target.createNewFile();
            in = zip.getInputStream(source);
            Writer.write( in, new FileOutputStream(target) );
            state = true;
        }
        catch (NullPointerException e) { state = false; }
        catch (IOException e) {
            e.printStackTrace();
            state = false;
        }
        finally {
            try { if (in != null) { in.close(); }}
            catch (IOException e) { e.printStackTrace(); }
        }
        return state;
    }

    public static boolean mkFile(File file) {
        File parent = file.getAbsoluteFile().getParentFile();
        if ( (parent == null) || ( mkPath(parent) )) { return false; }
        try { return file.createNewFile(); }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * im Prinzip redundant, macht auch nichts anderes als File.mkdirs()
     *
     * @param path
     * @return TRUE, if the path exists or could successfully created, otherwise FALSE
     * @see java.io.File#mkdirs()
     */
    public static boolean mkPath(File path) {
        if ( path.exists() ) { return true; }
        else { return path.mkdirs(); }
    }

    public static int input(String s) {
        System.out.print(s);
        try { return System.in.read(); }
        catch (IOException e ) { return -1; }
    }

}
