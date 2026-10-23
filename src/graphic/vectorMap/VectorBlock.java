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
import graphic.block.IsBlock;
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

public class VectorBlock extends Block implements IsDeadOrAliveTile, VectorMoveable, IsMoveableTile, HasMissileLauncher {

    public static final float TWO_PI    = (float) (2.0 * Math.PI);

    protected float px                  = 0;
    protected float py                  = 0;
    protected float vx                  = 0;
    protected float vy                  = 0;
    protected float vmax                = 0;
    protected float rotAngle            = 0; // rotationAngle, Radiant, 0.0f: RIGHT
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

    public static float normalize(float angle) {
        angle %= TWO_PI;
        return angle < 0 ? angle + TWO_PI : angle;
    }

    public static float getAngleFromVelocity(float vx, float vy) {
        float angle = (float) Math.atan2(vy, vx);
        return normalize(angle);
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
    public IsBlockType getBlockType() {
        return alive ? aliveTile.blockType() : deadTile.blockType();
    }

    public void setDeadTile(VectorTileRecord deadTile) {
        this.deadTile = deadTile;
    }

    public boolean isHeavy() {
        return heavy;
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
        rotAngle  = normalize(rotAngle);
    }

    @Override
    public float getViewAngle() {
        return rotAngle;
    }

    @Override
    public float getMoveAngle() {
        return getAngleFromVelocity(vx, vy);
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
     * @param dv delta-v
     */
    @Override
    public void accelerate(float dv) {
        this.vx = Math.clamp((float) (vx + Math.cos(rotAngle) * dv), -vmax, vmax);
        this.vy = Math.clamp((float) (vy + Math.sin(rotAngle) * dv), -vmax, vmax);
    }

    public float getSpeedX() {
        return vx;
    }

    public float getSpeedY() {
        return vy;
    }

    public float getSpeed() {
        return (float) Math.sqrt(vx*vx + vy*vy);
    }

    public float getMaxSpeed() {
        return vmax;
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

    public static void applyImpulseCollision(VectorBlock source, VectorBlock target) {
        // Normalenvektor berechnen (Kollisionsrichtung 'this' -> 'other')
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float distance = (float) Math.sqrt(dx*dx + dy*dy); // Satz des Pythagoras

        // verhindert Division durch 0
        if (distance == 0) {
            return;
        }

        // normalisieren des Vektors in Stoßrichtung
        float nx = dx / distance;
        float ny = dy / distance;

        // nur wenn mindestens eines der beiden schwer ist, wird Impuls übertragen
        if (source.heavy || target.heavy) {
            // Relativgeschwindigkeit in Richtung der Normalen berechnen
            float dvx = target.vx - source.vx;
            float dvy = target.vy - source.vy;
            float velocityAlongNormal = dvx*nx + dvy*ny;
            // nur wenn sich die Objekte auf einander zu bewegen, weiter machen, andernfals ist die Kollision bereits passiert
            if (velocityAlongNormal <= 0) {
                float restitution = 0.9f; // Elastizitätskoeffizient (<1 für etwas Dämpfung/Energieverlust)

                // Impulsübertragung
                float impulseScalar = -(1 + restitution) * velocityAlongNormal / 2.0f;
                if (source.heavy && !target.heavy) {
                    // 'this' ist schwer und schiebt 'other' weg
                    target.vx += impulseScalar * nx * 2.0f;
                    target.vy += impulseScalar * ny * 2.0f;
                } else if (!source.heavy && target.heavy) {
                    // 'other' ist schwer und stößt 'this' zurück
                    source.vx -= impulseScalar * nx * 2.0f;
                    source.vy -= impulseScalar * ny * 2.0f;
                } else {
                    // beide sind schwer: Klassischer elastischer Stoß zweier gleichgroßer Massen
                    source.vx -= impulseScalar * nx;
                    source.vy -= impulseScalar * ny;
                    target.vx += impulseScalar * nx;
                    target.vy += impulseScalar * ny;
                }
            }
        }

        // nach der Positionskollision: Korrektur der Überlappung
        float minDist = (source.width + target.width) / 2.0f; // quadratische Tiles annehmen
        if (distance < minDist && distance > 0) {
            float overlap = minDist - distance; // gesamte Überlappung auflösen
            if (source.heavy && !target.heavy) {
                // nur 'this' ist schwer, 'other' weicht komplett aus
                target.px += nx * overlap;
                target.py += ny * overlap;
            } else if (!source.heavy && target.heavy) {
                // nur 'other' ist schwer, 'this' weicht komplett aus
                source.px -= nx * overlap;
                source.py -= ny * overlap;
            } else {
                // beide gleich (beide heavy oder beide !heavy): 50:50 aufteilen
                float halfOverlap = 0.5f * overlap;
                source.px -= nx * halfOverlap;
                source.py -= ny * halfOverlap;
                target.px += nx * halfOverlap;
                target.py += ny * halfOverlap;
            }
        }
    }

    public static void applyWallCollision(VectorBlock source, IsBlock target) {
        float restitution = 0.9f; // Elastizitätskoeffizient (<1 für etwas Dämpfung/Energieverlust)

        // Mittelpunkte berechnen, um die Kollisionsseite (horizontal oder vertikal) zu bestimmen
        float sourceCenterX = source.px + source.width / 2.0f;
        float sourceCenterY = source.py + source.height / 2.0f;
        float wallCenterX   = target.getX() + target.getWidth() / 2.0f;
        float wallCenterY   = target.getY() + target.getHeight() / 2.0f;

        // Differenzen auf der Achse
        float dx = sourceCenterX - wallCenterX;
        float dy = sourceCenterY - wallCenterY;

        // Erlaubte Minimal-Abstände (halbe Ausdehnung addiert)
        float minDistX = ( source.width  + target.getWidth()  ) / 2.0f;
        float minDistY = ( source.height + target.getHeight() ) / 2.0f;

        // Überlappung auf beiden Achsen ermitteln
        float overlapX = minDistX - Math.abs(dx);
        float overlapY = minDistY - Math.abs(dy);

        // prüfen, ob Kollision von der Seite oder von oben/unten
        if (overlapX < overlapY) {
            // horizontale Kollision (links oder rechts) -> X-Geschwindigkeit umkehren
            source.vx = -source.vx * restitution;
            // Position korrigieren, damit das Objekt nicht in der Wand stecken bleibt
            if (dx > 0) {
                source.px = target.getX() + target.getWidth();
            } else {
                source.px = target.getX() - source.width;
            }
        } else {
            // vertikale Kollision (oben oder unten) -> Y-Geschwindigkeit umkehren
            source.vy = -source.vy * restitution;
            // Position korrigieren
            if (dy > 0) {
                source.py = target.getY() + target.getHeight();
            } else {
                source.py = target.getY() - source.height;
            }
        }

        source.setPos( Math.round( source.px ), Math.round( source.py ));
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
            applyImpulseCollision(this, block);
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
