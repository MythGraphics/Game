/*
 *
 */

package graphic.vectorMap;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.resource.Resource;
import graphic.block.Block;
import graphic.block.Collidable;
import static graphic.block.DefaultBlockType.CORPSE_PLAYER;
import static graphic.block.DefaultBlockType.NONE;
import graphic.block.IsBlockType;
import graphic.map.GameMap;
import graphic.map.IsCollisionHandler;
import graphic.map.OverlayUtil;
import graphic.missileLauncher.HasMissileLauncher;
import graphic.missileLauncher.MissileLauncher;
import graphic.tile.IsDeadOrAliveTile;
import graphic.tile.IsMoveableTile;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import util.vector.Vector2f;

public class VectorBlock extends Block implements IsDeadOrAliveTile, VectorMoveable, IsMoveableTile, HasMissileLauncher,
                                                  IsVectorBlock {

    protected float px                  = 0;
    protected float py                  = 0;
    protected float vx                  = 0;
    protected float vy                  = 0;
    protected float vmax                = 0;
    protected float rotAngle            = 0; // rotationAngle, radiant, 0.0f: RIGHT
    protected boolean heavy             = false; // "schwere" Blöcke übertragen Impuls, "leichte" nicht

    private boolean alive               = true;
    private boolean drawOverlay         = false;
    private boolean moveable            = true;
    private VectorTileRecord aliveTile  = new VectorTileRecord(NONE,          null, true);
    private VectorTileRecord deadTile   = new VectorTileRecord(CORPSE_PLAYER, null, true);
    private Resource overlayResource;
    private MissileLauncher launcher;

    public VectorBlock(VectorRecord vec, VectorTileRecord aliveTile, int x, int y) {
        super( x, y, vec.width(), vec.height(), aliveTile.blockType() );
        init(vec, aliveTile, x, y);
    }

    public VectorBlock(VectorRecord vec, VectorTileRecord aliveTile, int x, int y, int tileSize) {
        super( x, y, tileSize, tileSize, aliveTile.blockType() );
        init(vec, aliveTile, x, y);
    }

    private void init(VectorRecord vec, VectorTileRecord aliveTile, int x, int y) {
        if (vec == null || aliveTile == null) {
            throw new IllegalArgumentException("Records cannot be null.");
        }
        this.px         = x;
        this.py         = y;
        this.vx         = vec.vx();
        this.vy         = vec.vy();
        this.vmax       = vec.vmax();
        this.rotAngle   = vec.rotAngle();
        this.heavy      = vec.heavy();
        this.aliveTile  = aliveTile;
    }

    @Override
    public float getPx() {
        return px;
    }

    @Override
    public float getPy() {
        return py;
    }

    @Override
    public void setPx(float px) {
        this.px = px;
    }

    @Override
    public void setPy(float py) {
        this.py = py;
    }

    @Override
    public float getVx() {
        return vx;
    }

    @Override
    public float getVy() {
        return vy;
    }

    @Override
    public void setVx(float vx) {
        this.vx = vx;
    }

    @Override
    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getVmax() {
        return vmax;
    }

    @Override
    public boolean isHeavy() {
        return heavy;
    }

    @Override
    public IsBlockType getBlockType() {
        return alive ? aliveTile.blockType() : deadTile.blockType();
    }

    public void setDeadTile(VectorTileRecord deadTile) {
        this.deadTile = deadTile;
    }

    @Override
    public void stopMovement() {
        // vx = 0; vy = 0;
        moveable = false;
    }

    @Override
    public void startMovement() {
        moveable = true;
    }

    @Override
    public void setX(int x) {
        super.x = x;
        this.px = x;
    }

    @Override
    public void setY(int y) {
        super.y = y;
        this.py = y;
    }

    @Override
    public void setPos(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void rotate(float phi) {
        rotAngle += phi;
        rotAngle  = Vector2f.normalizeAngle(rotAngle);
    }

    @Override
    public float getViewAngle() {
        return rotAngle;
    }

    @Override
    public float getMoveAngle() {
        return Vector2f.getAngle(vx, vy);
    }

    @Override
    public void accelerate(float dvx, float dvy) {
//      this.vx = Math.clamp(vx + dvx, -vmax, vmax);
//      this.vy = Math.clamp(vy + dvy, -vmax, vmax);
        this.vx += dvx;
        this.vy += dvy;
        float speedSquare = vx*vx + vy*vy;
        if (speedSquare > vmax*vmax) {
            float speed = (float) Math.sqrt(speedSquare);
            this.vx = (vx / speed) * vmax;
            this.vy = (vy / speed) * vmax;
        }
    }

    /**
     * Beschleunigt in Sicht-Richtung.
     * @param dv delta-velocity
     */
    @Override
    public void accelerate(float dv) {
        this.vx = Math.clamp((float) (vx + Math.cos(rotAngle) * dv), -vmax, vmax);
        this.vy = Math.clamp((float) (vy + Math.sin(rotAngle) * dv), -vmax, vmax);
    }

    public float getSpeed() {
        return (float) Math.sqrt(vx*vx + vy*vy);
    }

    public void drawOverlay(boolean b) {
        this.drawOverlay = b;
    }

    @Override
    public MissileLauncher getMissileLauncher() {
        return launcher;
    }

    public void setMissileLauncher(MissileLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void drawOverlay(Graphics2D g2d, int offsetX, int offsetY) {
        if (alive && drawOverlay) {
            OverlayUtil.drawResourceBar(g2d, offsetX, offsetY, this, this);
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setDead() {
        alive    = false;
        moveable = false;
//      super.setBlockType( deadTile.blockType() );
    }

    @Override
    public void setAlive() {
        alive    = true;
        moveable = true;
//      super.setBlockType( aliveTile.blockType() );
    }

    @Override
    public Resource getOverlayResource() {
        return overlayResource;
    }

    public void setOverlayResource(Resource overlayResource) {
        this.overlayResource = overlayResource;
        drawOverlay = overlayResource != null;
    }

    @Override
    public boolean onCollision(Collidable initiator, GameMap map, IsCollisionHandler handler) {
        if (initiator instanceof VectorBlock block) {
            IsVectorBlock.applyImpulseCollision(this, block);
        }
        if (overlayResource != null && overlayResource.getValue() <= 0) {
            setDead();
        }
        return super.onCollision(initiator, map, handler);
    }

    @Override
    public void move() {
        if (!moveable) {
            return;
        }
        px += vx;
        py += vy;
        super.x = Math.round(px);
        super.y = Math.round(py);
    }

    @Override
    public BufferedImage getImage() {
        VectorTileRecord tile = alive ? aliveTile : deadTile;
        return tile.getImage();
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        VectorTileRecord tile = alive ? aliveTile : deadTile;
        BufferedImage img = tile.getImage();
        if (img == null) {
            return;
        }

        int drawX = x-offsetX;
        int drawY = y-offsetY;

        if ( tile.isSymmetric() ) {
            g2d.drawImage(img, drawX, drawY, width, height, null);
        } else {
            // Rotationspunkt in die Mitte legen
            float centerX = drawX + width/2.0f;
            float centerY = drawY + height/2.0f;

            AffineTransform old = g2d.getTransform(); // speichert den Zustand
            g2d.rotate(rotAngle, centerX, centerY);
            g2d.drawImage(img, drawX, drawY, width, height, null);
//          g2d.rotate(-rotAngle, centerX, centerY); // zurück drehen, damit nachfolgende Renderings nicht verdreht werden
            g2d.setTransform(old); // stellt den alten Zustand wieder her
        }

        drawOverlay(g2d, offsetX, offsetY);
    }

}
