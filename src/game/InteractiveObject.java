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

import static graphic.io.ImageUtility.scaleImage;
import java.awt.Image;
import javax.swing.ImageIcon;

public class InteractiveObject implements HasName, HasUIImage, Cloneable {

    private String name;
    private Image img; // Bild/Hintergrund für TextBox
    private Image icon;

    public InteractiveObject(String name) {
        this.name = name;
    }

    public InteractiveObject(String name, Image img) {
        this(name);
        this.img = img;
    }

    InteractiveObject(InteractiveObject obj) {
        this.name   = obj.getName();
        this.img    = obj.getImg();
        this.icon   = obj.getIcon().getImage();
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    @Override
    public Image getImg() {
        return img;
    }

    @Override
    public ImageIcon getIcon() {
        if ( icon != null ) {
            return new ImageIcon(icon);
        }
        if ( img != null ) {
            return new ImageIcon( scaleImage( img, DEFAULT_ICON_DIMENSION.width, DEFAULT_ICON_DIMENSION.height ));
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Wichtig: equals und hashCode müssen implementiert werden,
     * damit die Map die Items korrekt vergleichen kann */

    @Override
    public boolean equals(Object o) {
        if ( this == o ) { return true; }
        if ( o == null || getClass() != o.getClass() ) { return false; }
        InteractiveObject env = (InteractiveObject) o;
        return this.getName().equals( env.getName() );
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public InteractiveObject clone() throws CloneNotSupportedException {
        return new InteractiveObject(this);
    }

}
