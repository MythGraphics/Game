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

import game.InteractiveObject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class TextOverImageRenderer extends TextRenderer {

    final LinkedList<Message> msgs = new LinkedList<>();

    private BufferedImage bg;
    private Message msg;
    private Dimension imgSize; // default: null
    private boolean typeOverImage = false;
    private boolean keepAspectRatio = true;

    public TextOverImageRenderer(Point p) {
        super(p);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (typeOverImage) {
            setImageSize( new Dimension( width, height ));
        }
    }

    public void setTypeOverImage(boolean typeOverImage) {
        this.typeOverImage = typeOverImage;
    }

    public boolean typeOverImage() {
        return typeOverImage;
    }

    public Message getCurrentMessage() {
        return msg;
    }

    public void show(List<Message> msgs) {
        msgs.addAll(msgs);
        showNext();
    }

    public void show(String text, InteractiveObject source) {
        show( new Message( text, source ));
    }

    public void show(Message msg) {
        msgs.add(msg);
        showNext();
    }

    private void showNext() {
        if ( !super.isRunning() ) {
            printNext();
        }
    }

    private void scaleImageOnSize() {
        typeOverImage = true;
        imgSize = new Dimension( super.getSize().width, super.getSize().height );
        setTextPosition( new Point( TextRenderer.D, TextRenderer.D ));
    }

    private void setBackground(BufferedImage bg) {
        this.bg = bg;
        if (imgSize == null) {
            scaleImageOnSize();
        }
    }

    public void setBackground(BufferedImage bg, Dimension imgSize) {
        setImageSize(imgSize);
        setBackground(bg);
    }

    public static Dimension getScaledDimension(BufferedImage image, Dimension targetDim) {
        if (image == null) {
            return targetDim;
        }
        int originalWidth  = image.getWidth();
        int originalHeight = image.getHeight();
        double widthRatio  = (double) targetDim.width  / originalWidth;
        double heightRatio = (double) targetDim.height / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        int width  = (int) (originalWidth  * ratio);
        int height = (int) (originalHeight * ratio);
        return new Dimension(width, height);
    }

    public void keepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }

    /**
     * Sets the size of the background image.
     * NULL for full size.
     * @param imgSize target image size
     */
    public void setImageSize(Dimension imgSize) {
        this.imgSize = imgSize;
        if (imgSize == null) {
            // als Hintergrundbild -> einfach auf dem Bild schreiben
            scaleImageOnSize();
        } else if (!typeOverImage) {
            // neben dem Bild schreiben
            setTextPosition( new Point( imgSize.width+TextRenderer.D, getTextPosition().y ));
        }
    }

    @Override
    protected void draw(Graphics2D g2d) {
        if ( bg != null ) {
            if (typeOverImage || imgSize == null) {
                // volle Panel-Größe nutzen
                g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            } else {
                Dimension size = keepAspectRatio ? getScaledDimension(bg, this.imgSize) : this.imgSize;
                g2d.drawImage(bg, 0, 0, size.width, size.height, null);
            }
        }
        super.draw(g2d);
    }

    public boolean printNext() {
        super.removeText();
        msg = msgs.poll();
        if ( msg == null ) {
            super.stop();
            return false;
        }
        setBackground( msg.getSource().getImg() );
        super.printCharByChar( msg.getText() );
        return true;
    }

}
