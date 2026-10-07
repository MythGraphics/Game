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
import graphic.map.*;
import graphic.tile.IsDeadOrAliveTile;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class VectorBlock extends Block implements IsDeadOrAliveTile, AutoMoveable {

    private float vx   = 0;
    private float vy   = 0;
    private float rota = 0; // rotationAngle, 0: RIGHT
    private boolean heavy;  // "schwere" Blöcke übertagen Impuls, leichte nicht
    private boolean alive = true;
    private boolean drawOverlay = false;
    private Resource overlayResource;

    public VectorBlock(VectorRecord vec, int x, int y, IsBlockType bType) {
        super(x, y, vec.width(), vec.height(), bType);
        init(vec);
    }

    public VectorBlock(VectorRecord vec, int x, int y, int tileSize, IsBlockType bType) {
        super(x, y, tileSize, tileSize, bType);
        init(vec);
    }

    public static float normalize(float angle) {
        angle %= 1.0f;
        return angle < 0 ? angle+1.0f : angle;
    }

    private void init(VectorRecord vec) {
        this.vx    = vec.vx();
        this.vy    = vec.vy();
        this.rota  = vec.rota();
        this.heavy = vec.heavy();
    }

    public boolean isHeavy() {
        return heavy;
    }

    @Override
    public void rotate(float delta_phi) {
        rota += delta_phi;
        rota  = normalize(rota); // Winkel normalisieren
    }

    @Override
    public void accelerate(float vx, float vy) {
        this.vx += vx;
        this.vy += vy;
    }

    /**
     * Accelerates in view direction.
     * @param dv
     */
    @Override
    public void accelerate(float dv) {
        vx += Math.cos(rota)*dv;
        vy += Math.sin(rota)*dv;
    }

    public float getSpeedX() {
        return vx;
    }

    public float getSpeedY() {
        return vy;
    }

    public float getSpeed() {
        return (float) Math.sqrt( vx*vx + vy*vy );
    }

    public void drawOverlay(boolean b) {
        this.drawOverlay = b;
    }

    @Override
    public void drawOverlay(Graphics2D g2d, int offsetX, int offsetY) {
        if (drawOverlay) {
            OverlayUtil.drawResourceBar(g2d, offsetX, offsetY, this, this);
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setDead() {
        alive = false;
        // ToDo implementieren
    }

    @Override
    public void setAlive() {
        alive = true;
        // ToDo implementieren
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
    public boolean onCollision(GameMap map, Block initiator, IsCollisionHandler handler) {
        // ToDo implementieren
        if (overlayResource != null && overlayResource.getValue() <= 0) {
            setDead();
        }
        return super.onCollision(map, initiator, handler);
    }

    @Override
    public void start() {
        // ToDo implementieren
    }

    @Override
    public void stop() {
        // ToDo implementieren
    }

    @Override
    public BufferedImage getImage() {
        // ToDo implementieren
        return null;
    }

    @Override
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        g2d.drawImage( getImage(), x-offsetX, y-offsetY, width, height, null );
        drawOverlay(g2d, offsetX, offsetY);
    }

}
