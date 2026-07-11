/*
 *
 */

package io;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 2.1.1
 *
 */

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

public final class Reader {

    private Reader() {}

    public static BufferedInputStream getBinaryReader(InputStream source) {
        return new BufferedInputStream(source);
    }

    public static BufferedInputStream getBinaryReader(URL source) throws IOException {
        return new BufferedInputStream( source.openStream() );
    }

    public static BufferedInputStream getBinaryReader(File source) throws IOException {
        return new BufferedInputStream( new FileInputStream(source) );
    }

    public static BufferedInputStream getBinaryReader(Socket source) throws IOException {
        return new BufferedInputStream( source.getInputStream() );
    }

    public static BufferedReader getPipedTextReader(PipedWriter source) throws IOException {
        return new BufferedReader( new PipedReader(source) );
    }

    public static BufferedReader getPipedTextReader(PipedReader source) {
        return new BufferedReader(source);
    }

    public static BufferedReader getTextReader(InputStream source) {
        return new BufferedReader( new InputStreamReader(source) );
    }

    public static BufferedReader getTextReader(URL source) throws IOException {
        return new BufferedReader( new InputStreamReader( source.openStream() ));
    }

    public static BufferedReader getTextReader(File source) throws IOException {
       return new BufferedReader( new FileReader(source) );
    }

    public static BufferedReader getTextReader(Socket source) throws IOException {
       return new BufferedReader( new InputStreamReader( source.getInputStream() ));
    }

    public static BufferedReader getConsoleReader() {
        return new BufferedReader( new InputStreamReader(System.in) );
    }

    public static InputStream getZipReader(ZipFile zip, String entry) throws IOException {
        return zip.getInputStream( zip.getEntry( entry ));
    }

    public static InputStream getZipReader(ZipFile zip, ZipEntry source) throws IOException {
        return zip.getInputStream(source);
    }

    public static ArrayList<String> getTextList(File file) throws IOException {
        return getTextList( getTextReader( file ));
    }

    public static ArrayList<String> getTextList(BufferedReader source) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        while ( source.ready() ) {
            list.add( source.readLine() );
        }
        source.close();
        return list;
    }

    public static ArrayList<String> getTextList(ZipFile zip, String entry) throws IOException {
        return getTextList( getTextReader( getZipReader( zip, entry )));
    }

    public static BufferedImage getImage(ZipFile zip, String entry) throws IOException {
        return ImageIO.read( getZipReader( zip, entry ));
    }

}
