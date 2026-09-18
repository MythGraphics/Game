/*
 *
 */

package graphic.map;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import graphic.CanFireMissile;
import graphic.Direction;
import static graphic.Direction.*;
import static graphic.io.BinaryIO.loadImage;
import static graphic.io.ImageUtility.scale;
import static graphic.io.ImageUtility.stretch;
import static graphic.map.DefaultBlockType.*;
import graphic.tile.AutoMoveableTile;
import graphic.tile.BlockTile;
import graphic.tile.Missile;
import graphic.tile.MoveableTile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class GameMap extends JPanel implements ActionListener, IsCollisionHandler {

    public final static int DEFAULT_TILE_SIZE = 32;
    public final static int FPS = 15;

    public final int visibleWidth, visibleHeight, boardWidth, boardHeight;
    public final Dimension board, visibleBoard;

    public static boolean prerenderMap = true;

    protected final int tileSize, rowCount, columnCount;
    protected final char[][] tileMap;

    protected final Collection<Collidable> collidables          = new HashSet<>(); // Collidable, IsBlock
    protected final Collection<Renderable> renderables          = new ArrayList<>();
    protected final Collection<AutoMoveableTile> scurryables    = new HashSet<>(); // sich selbst bewegende Entitäten
    protected final Collection<Collidable> garbageC             = new HashSet<>(); // Collidables, die entfernt werden sollen
    protected final Collection<Renderable> garbageR             = new HashSet<>(); // Renderables, die entfernt werden sollen
    protected final Collection<AutoMoveableTile> garbageS       = new HashSet<>(); // AutoMoveables, die entfernt werden sollen

    protected MoveableTile player;
    protected Renderable spaceTile;

    private final List<CollisionActionListener> collisionListeners = new ArrayList<>();
    private final Point lastPlayerPos = new Point();

    private boolean active = false;
    private boolean ready  = false;
    private Timer renderLoop;

    public GameMap(char[][] tileMap, int tileSize, Dimension visibleSize) {
        this.tileMap    = tileMap;
        this.tileSize   = tileSize;
        visibleBoard    = visibleSize;
        visibleHeight   = visibleSize.height;
        visibleWidth    = visibleSize.width;
        rowCount        = tileMap.length;
        columnCount     = tileMap[0].length;
        boardWidth      = columnCount*tileSize;
        boardHeight     = rowCount*tileSize;
        board           = new Dimension(boardWidth, boardHeight);
    }

    public GameMap(char[][] tileMap, Dimension visibleSize) {
        this(tileMap, DEFAULT_TILE_SIZE, visibleSize);
    }

    public GameMap(char[][] tileMap) {
        // setzt visibleSize auf mapSize, aber max. 800x600
        this(
            tileMap,
            DEFAULT_TILE_SIZE,
            new Dimension(
                Math.min( tileMap[0].length*DEFAULT_TILE_SIZE, 800 ),
                Math.min( tileMap.length*DEFAULT_TILE_SIZE,    600 )
            )
        );
    }

    protected abstract void loadSprites();
    protected abstract BlockTile getBlockTile(int x, int y, IsBlockType bType);
    public abstract Color getAmbientColor();

    public void init() {
        loadSprites();
        initMap();
        if (prerenderMap) {
            renderMapImage(ENEMY, NPC, PLAYER);
        }
        renderLoop = new Timer(1000/FPS, this);
        ready = true;
    }

    public void renderMapImage(DefaultBlockType... bTypes) {
        BufferedImage prerenderedMap = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = prerenderedMap.createGraphics();
        drawMapImage(g2d, 0, 0, bTypes);
        g2d.dispose();
        try {
            File file = new File("currentMap.png");
            ImageIO.write(prerenderedMap, "png", file);
            System.out.println( "Map gespeichert: " + file.getAbsolutePath() );
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return visibleBoard;
    }

    public Point getMaxPoint() {
        return new Point(boardWidth, boardHeight);
    }

    public BufferedImage loadStretchedImage(String imgPath) {
        return stretch( loadImage( imgPath ), DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, false );
    }

    public BufferedImage loadScaledImage(String imgPath) {
        return scale( loadImage( imgPath ), DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, false );
    }

    public MoveableTile getPlayer() {
        return player;
    }

    /**
     * Berechnet den X-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass das Ansichtsfenster innerhalb der Kartengrenzen bleibt.
     */
    private int getOffsetX() {
        // Ziel-Offset: Spieler zentrieren
        int targetX = player.x - (visibleWidth / 2) + (tileSize / 2);
        // Begrenzungen prüfen:
        // Linker Rand: Offset kann nicht kleiner als 0 sein.
        int offsetX = Math.max(0, targetX);
        // Rechter Rand: Viewport darf nicht über die Gesamtbreite der Karte hinausragen.
        int maxOffsetX = boardWidth - visibleWidth;
        if (maxOffsetX < 0) { maxOffsetX = 0; } // Für den Fall, dass die Karte kleiner als der Viewport ist
        offsetX = Math.min(offsetX, maxOffsetX);
        return offsetX;
    }

    /**
     * Berechnet den Y-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass das Ansichtsfenster innerhalb der Kartengrenzen bleibt.
     */
    private int getOffsetY() {
        // Ziel-Offset: Spieler zentrieren
        int targetY = player.y - (visibleHeight / 2) + (tileSize / 2);
        // Begrenzungen prüfen:
        // Oberer Rand: Offset kann nicht kleiner als 0 sein.
        int offsetY = Math.max(0, targetY);
        // Unterer Rand: Viewport darf nicht über die Gesamthöhe der Karte hinausragen.
        int maxOffsetY = boardHeight - visibleHeight;
        if (maxOffsetY < 0) { maxOffsetY = 0; } // Für den Fall, dass die Karte kleiner als der Viewport ist
        offsetY = Math.min(offsetY, maxOffsetY);
        return offsetY;
    }

    void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        IsBlockType bType = DefaultBlockType.getByChar(tileMapChar);
        BlockTile tile    = getBlockTile(x, y, bType);

        switch (bType) {
            case null -> {
                System.err.println(
                    "TileMapChar '" + tileMapChar + "' auf " + x + ", " + y +
                    " ungültig (BlockType ist null) -> wird ignoriert."
                );
            }
            case PLAYER -> {
                if (tile instanceof MoveableTile moveable) {
                    this.player = moveable;
                } else {
                    System.err.println(
                        "Player vom Klassen-Typ " +
                        ( tile == null ? "null" : tile.getClass() ) +
                        " inkompatibel: muss MoveableSprite sein."
                    );
                }
                lastPlayerPos.x = x;
                lastPlayerPos.y = y;
            }
            case SPACE -> {
                spaceTile = tile;
            }
            case SPACEHOLDER -> {}
            default -> {
                collidables.add(tile);
                if ( tile.getImage() != null ) {
                    renderables.add(tile);
                }
            }
        }
    }

    private void initMap() {
        int x, y;
        char tileMapChar;
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                try {
                    tileMapChar = tileMap[r][c];
                } catch (ArrayIndexOutOfBoundsException e) {
                    tileMapChar = ' ';
                }
                x = c*tileSize;
                y = r*tileSize;
                loadTileMapChar(tileMapChar, x, y, tileSize);
            }
        }
        if (spaceTile == null) {
            spaceTile = getBlockTile(0, 0, SPACE);
        }
    }

    public void activate() {
        if (!ready) {
            System.err.println("Map cannot activated - not initialized!");
            return;
        }
        if ( !renderLoop.isRunning() ) {
            renderLoop.start();
            active = true;
            System.out.println("Map rendering active.");
        }
    }

    public void deactivate() {
        active = false;
        renderLoop.stop();
        super.repaint();
        System.out.println("Map rendering deactivated.");
    }

    public void dispose() {
        System.out.println("Map is closing ...");
        ready = false;
        renderLoop.stop();
    }

    @Override
    // wird vom Timer ausgelöst
    public void actionPerformed(ActionEvent evt) {
/*
        // DeltaTime berechnen (vergangene Zeit in Sekunden seit dem letzten Frame)
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = currentTime;
 */
        super.repaint();
    }

    public void addCollisionActionListener(CollisionActionListener actionListener) {
        collisionListeners.add(actionListener);
    }

    private void fireEvent(Collidable source, Collidable target) {
        fireEvent(this, source, target);
    }

    @Override
    public void fireEvent(GameMap map, Collidable source, Collidable target) {
        // collider/initiator (bewegliche Objekt), obstacle (statisches Hindernis)
        System.out.println(
            "Collision of block "   + source.getBlockType() +
            " with block "          + target.getBlockType() +
            " at pixel "            + target.getX() + ", " + target.getY() +
            " (column "             + ( target.getX() / target.getWidth()  + 1 ) + // +1, um keine Indizes auszugeben
            ", row "                + ( target.getY() / target.getHeight() + 1 ) + // +1, um keine Indizes auszugeben
            ")."
        );
        collisionListeners.forEach( actionListener -> actionListener.collisionPerformed(
            new CollisionEvent(this, source, target)
        ));

        // aufräumen
        if (source instanceof Missile missile) {
            remove(missile);
        }
        if (target instanceof Missile missile) {
            remove(missile);
        }
    }

    public void moveThroughPortal(Point target) {
        int destinationX = target.x + player.x - lastPlayerPos.x;
        int destinationY = target.y + player.y - lastPlayerPos.y;
        setPlayerPositionXY(destinationX, destinationY);
    }

    public IsBlock getBlock(int col, int row) {
        // 0 ist ungültig
        if ( col <= 0 || row <= 0 ) {
            return null;
        }

        int x = (col-1)*tileSize;
        int y = (row-1)*tileSize;
        for (Collidable c : collidables) {
            if ( x == c.getX() && y == c.getY() ) {
                return c;
            }
        }
        return null;
    }

    public void movePlayer(int deltaCol, int deltaRow) {
        int targetX = player.x + deltaCol*tileSize;
        int targetY = player.y + deltaRow*tileSize;
        setPlayerPositionXY(targetX, targetY);
    }

    /**
     * Setzt die Position des Spielers auf die gegebene Position.
     * @param column Spalte (x)
     * @param row Zeile (y)
     */
    public void setPlayerPosition(int column, int row) {
        // 0 ist ungültig
        if ( column == 0 || row == 0 ) {
            return;
        }

        int targetColumn, targetRow;
        if (column < 0) {
            targetColumn = (column + columnCount) % columnCount;
        } else {
            targetColumn = (column-1 + columnCount) % columnCount;
        }
        if (row < 0) {
            targetRow = (row + rowCount) % rowCount;
        } else {
            targetRow = (row-1 + rowCount) % rowCount;
        }

        setPlayerPositionXY(targetColumn*tileSize, targetRow*tileSize);
    }

    /**
     * Setzt die Position des Spielers auf die gegebenen X-Y-Koordinaten.
     * @param x
     * @param y
     */
    public void setPlayerPositionXY(int x, int y) {
        setLastPlayerPosition();
        player.x = x;
        player.y = y;
        detectCollision(player);
    }

    public void setPlayerPosition(Point target) {
        setPlayerPosition(target.x, target.y);
    }

    private void setLastPlayerPosition() {
        lastPlayerPos.x = player.x;
        lastPlayerPos.y = player.y;
    }

    /**
     * Setzt die Position des Spielers auf die Position vor dem Bewegungsbefehl zurück.
     */
    private void resetPlayerPosition() {
        player.x = lastPlayerPos.x;
        player.y = lastPlayerPos.y;
    }

    public void movePlayer(KeyEvent evt) {
        if (!active) {
            return;
        }

        setLastPlayerPosition();
        switch ( evt.getKeyCode() ) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W, KeyEvent.VK_8 -> player.move(UP);
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S, KeyEvent.VK_2 -> player.move(DOWN);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_6 -> player.move(RIGHT);
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A, KeyEvent.VK_4 -> player.move(LEFT);
        }
        detectCollision(player);
    }

    private boolean detectPanelCollision() {
        if (player.x < 0) {
            player.x = 0;
            return true;
        } else if (player.x >= boardWidth) {
            player.x = boardWidth-tileSize;
            return true;
        } else if (player.y < 0) {
            player.y = 0;
            return true;
        } else if (player.y >= boardHeight) {
            player.y = boardHeight-tileSize;
            return true;
        }
        return false;
    }

    void detectCollision(Collidable source) {
        // Collidable, IsCollider, IsCollisionHandler aktuell nicht genutzt
        if ( detectPanelCollision() ) {
            Block target = new Block(player.x, player.y, player.width, player.height, BOUNDARY);
            fireEvent(source, target);
            return;
        }

        for (Collidable target : collidables) {
            if ( collision( source, target )) {
                fireEvent(source, target);
                boolean passable = target.getBlockType().isPassable();
                if (!passable && source == player) {
                    resetPlayerPosition();
                }
                fireEvent(source, target);
                break;
            }
        }
    }

    private boolean collision(Collidable a, Collidable b) {
        if (a == null || b == null || a == b) {
            return false;
        }
        return  a.getX() < b.getX() + b.getWidth()  &&
                a.getX() + a.getWidth() > b.getX()  &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setClip(0, 0, visibleWidth, visibleHeight); // nur der sichtbare Bereich, nicht die volle Kartengröße
        if (active) {
            setRenderingHints(g2d);
            draw(g2d);
        }
    }

    public final void drawMapImage(Graphics2D g2d, int offsetX, int offsetY, DefaultBlockType... tileExclude) {
        Set<IsBlockType> excludedTypes  = Set.of(tileExclude);
        Collection<Renderable> currentBlocks =
            renderables.stream()
                       .filter(r -> {
                           if (r instanceof Block block) {
                               return !excludedTypes.contains( block.getBlockType() );
                           }
                           return false; // Renderables, die keine Blöcke sind, ausschließen: nackte Map
                       })
                       .collect( Collectors.toList() );
        drawMapImage(g2d, offsetX, offsetY, currentBlocks);
    }

    private void drawMapImage(Graphics2D g2d, int offsetX, int offsetY, Collection<Renderable> renderables) {
        drawSpace(g2d, offsetX, offsetY);
        for (Renderable r : renderables) {
            r.draw(g2d, offsetX, offsetY);
        }
    }

    protected void draw(Graphics2D g2d) {
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();

        drawSpace(g2d, offsetX, offsetY);
        for (Renderable r : renderables) {
            r.draw(g2d, offsetX, offsetY);
        }
        if (player != null) {
            player.draw(g2d, offsetX, offsetY);
        }

        for (AutoMoveableTile tile : scurryables) {
            tile.move();
            detectCollision(tile);
        }

        collidables.removeAll(garbageC);
        garbageC.clear();
        renderables.removeAll(garbageR);
        garbageR.clear();
        scurryables.removeAll(garbageS);
        garbageS.clear();
    }

    protected void drawSpace(Graphics2D g2d, int offsetX, int offsetY) {
        if (spaceTile != null && spaceTile.getImage() != null) {
            BufferedImage spaceImg = spaceTile.getImage();
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    g2d.drawImage(
                        spaceImg,
                        tileSize*c - offsetX,
                        tileSize*r - offsetY,
                        tileSize,
                        tileSize,
                        null
                    );
                }
            }
        }
    }

    public int getEnemyCount() {
        int value = 0;
        for (Collidable c : collidables) {
            if ( c.getBlockType() == ENEMY ) {
                ++value;
            }
        }
        return value;
    }

    public void fireMissile(CanFireMissile initiator, Direction d) {
        int x = initiator.getX();
        int y = initiator.getY();
        switch (d) {
            case RIGHT -> x += tileSize;
            case LEFT  -> x -= tileSize;
            case DOWN  -> y += tileSize;
            case UP    -> y -= tileSize;
        }
        add( new Missile( initiator, d, x, y, MISSILE, tileSize/2 ));
    }

    public void add(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collidable c) {
            collidables.add(c);
        }
        if (obj instanceof Renderable r) {
            renderables.add(r);
        }
        if (obj instanceof AutoMoveableTile amt) {
            scurryables.add(amt);
        }
    }

    public void remove(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collidable c) {
            garbageC.add(c);
        }
        if (obj instanceof Renderable r) {
            garbageR.add(r);
        }
        if (obj instanceof AutoMoveableTile amt) {
            garbageS.add(amt);
        }
    }

}
