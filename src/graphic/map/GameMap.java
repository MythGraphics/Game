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

import graphic.HasImage;
import graphic.Moveable;
import graphic.block.Block;
import graphic.block.Collidable;
import graphic.block.DefaultBlockType;
import graphic.block.IsBlock;
import graphic.block.IsBlockType;
import graphic.block.IsMoveableBlock;
import graphic.block.Renderable;
import graphic.missileLauncher.IsMissile;
import static graphic.block.DefaultBlockType.*;
import static graphic.io.BinaryIO.loadImage;
import static graphic.io.ImageUtility.scale;
import static graphic.io.ImageUtility.stretch;
import graphic.tile.IsBlockTile;
import graphic.tile.IsMoveableTile;
import graphic.tile.Portal;
import graphic.tile.PortalList;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class GameMap extends JPanel implements ActionListener, IsCollisionHandler {

    public final static int DEFAULT_TILE_SIZE = 32;
    public final static DefaultBlockType[] DEFAULT_EXCLUDE = { PLAYER, ENEMY, NPC };

    public final int fps;
    public final int tileSize, rowCount, columnCount;
    public final int visibleWidth, visibleHeight, boardWidth, boardHeight;
    public final Dimension board, visibleBoard;
    public final Point lastPlayerPos = new Point();

    public static boolean prerenderMap = true;
    public static int defaultMissileSpeed = DEFAULT_TILE_SIZE/4; // pixel per frame

    protected final Collection<Collidable>      staticCollidables   = new HashSet<>(); // statische Collidables
    protected final Collection<IsMoveableTile>  scurryables         = new HashSet<>(); // sich selbstständig bewegende Entitäten
    protected final Collection<IsMissile>       missiles            = new HashSet<>();
    protected final Collection<Renderable>      renderables         = new ArrayList<>();
    protected final Collection<Collidable>      garbageC            = new HashSet<>(); // statische Collidables, die entfernt werden sollen
    protected final Collection<IsMoveableTile>  garbageS            = new HashSet<>(); // MoveableTiles, die entfernt werden sollen
    protected final Collection<IsMissile>       garbageM            = new HashSet<>(); // Missiles, die entfernt werden sollen
    protected final Collection<Renderable>      garbageR            = new HashSet<>(); // Renderables, die entfernt werden sollen
    protected final PortalList                  portals             = new PortalList();

    protected IsBlockType[] excludedBlockTypes = DEFAULT_EXCLUDE;
    protected IsBlockTile emptyTile;
    protected Collidable lastImpassableCollisionTarget;

    private final char[][] tileMap;
    private final List<CollisionActionListener> collisionListeners = new ArrayList<>();
    private final RandomGenerator rangen = RandomGenerator.getDefault();

    private boolean active  = false;
    private boolean ready   = false;
    private Timer renderLoop;

    public GameMap(char[][] tileMap, MapRecord rec) {
        this.tileMap    = tileMap;
        this.fps        = rec.fps();
        this.tileSize   = rec.tileSize();
        visibleBoard    = rec.visibleSize();
        visibleHeight   = visibleBoard.height;
        visibleWidth    = visibleBoard.width;
        rowCount        = tileMap.length;
        columnCount     = tileMap[0].length;
        boardWidth      = columnCount*tileSize;
        boardHeight     = rowCount*tileSize;
        board           = new Dimension(boardWidth, boardHeight);
    }

    public GameMap(char[][] tileMap, int fps) {
        this(
            tileMap,
            new MapRecord(
                fps,
                DEFAULT_TILE_SIZE,
                new Dimension( // visibleSize auf mapSize, aber max. 800x600
                    Math.min( tileMap[0].length*DEFAULT_TILE_SIZE, 800 ),
                    Math.min( tileMap.length*DEFAULT_TILE_SIZE,    600 )
        )));
    }

    protected abstract void loadSprites();
    protected abstract IsBlockTile getBlockTile(int x, int y, IsBlockType bType);

    public abstract Color getAmbientColor();
    public abstract void movePlayer(KeyEvent evt);
    public abstract IsMoveableTile getPlayer();

    public static BufferedImage loadStretchedImage(String imgPath) {
        return stretch( loadImage( imgPath ), DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, false );
    }

    public static BufferedImage loadScaledImage(String imgPath) {
        return scale( loadImage( imgPath ), DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, false );
    }

    public void init() {
        if (ready) {
            System.err.println("Map already initialised.");
            return;
        }

        loadSprites();
        initMap();
        if (prerenderMap) {
            renderMapImage(excludedBlockTypes);
        }
        renderLoop = new Timer(1000/fps, this);
        ready = true;
    }

    public boolean isInitialized() {
        return ready;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getFPS() {
        return fps;
    }

    @Override
    public Dimension getPreferredSize() {
        return visibleBoard;
    }

    public Point getMaxPoint() {
        return new Point(boardWidth, boardHeight);
    }

    public void renderMapImage(IsBlockType... excludedTypes) {
        BufferedImage prerenderedMap = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = prerenderedMap.createGraphics();
        draw(g2d, 0, 0, excludedTypes);
        g2d.dispose();
        try {
            File file = new File("currentMap.png");
            ImageIO.write(prerenderedMap, "png", file);
            System.out.println( "Map gespeichert: " + file.getAbsolutePath() );
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }
    }

    /**
     * Berechnet den X-Offset für den Scroll-Buffer, um den Spieler zu zentrieren.
     * Stellt sicher, dass das Ansichtsfenster innerhalb der Kartengrenzen bleibt.
     * @return x-offset
     */
    protected int getOffsetX() {
        // Ziel-Offset: Spieler zentrieren
        int targetX = getPlayer().getX() - (visibleWidth / 2) + (tileSize / 2);
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
     * @return y-offset
     */
    protected int getOffsetY() {
        // Ziel-Offset: Spieler zentrieren
        int targetY = getPlayer().getY() - (visibleHeight / 2) + (tileSize / 2);
        // Begrenzungen prüfen:
        // Oberer Rand: Offset kann nicht kleiner als 0 sein.
        int offsetY = Math.max(0, targetY);
        // Unterer Rand: Viewport darf nicht über die Gesamthöhe der Karte hinausragen.
        int maxOffsetY = boardHeight - visibleHeight;
        if (maxOffsetY < 0) { maxOffsetY = 0; } // Für den Fall, dass die Karte kleiner als der Viewport ist
        offsetY = Math.min(offsetY, maxOffsetY);
        return offsetY;
    }

    protected void loadTileMapChar(char tileMapChar, int x, int y, int tileSize) {
        IsBlockType bType = DefaultBlockType.getByChar(tileMapChar);
        IsBlockTile tile  = getBlockTile(x, y, bType);
        switch (bType) {
            case null -> {
                System.err.println(
                    "TileMapChar '" + tileMapChar + "' auf " + x + ", " + y +
                    " ungültig (BlockType ist null) -> wird ignoriert."
                );
            }
            case SPACE          -> emptyTile = tile;
            case SPACEHOLDER    -> {}
            case PORTAL         -> add( new Portal( getRandomSpace(), tile ));
            default             -> add(tile);
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
        if (getPlayer() != null) {
            saveCurrentPlayerPosition();
            renderables.add( getPlayer() ); // sollte letzter Eintrag sein, damit Player über allen anderen Fliesen liegt
        }
        if (emptyTile == null) {
            emptyTile = getBlockTile(0, 0, SPACE);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        if (!ready) {
            System.err.println("Map cannot be activated - not initialized.");
            return;
        }
        if (getPlayer() == null) {
            System.err.println("Map cannot be activated - player not set.");
            return;
        }
        if ( !renderLoop.isRunning() ) {
            active = true;
            renderLoop.start();
            System.out.println("Map rendering active.");
        }
    }

    public void deactivate() {
        active = false;
        renderLoop.stop();
        repaint();
        System.out.println("Map rendering deactivated.");
    }

    public void dispose() {
        if (active) {
            deactivate();
        }
        System.out.println("Map is closing ...");
        renderLoop.stop();
        ready = false;
    }

    @Override
    // wird vom Timer ausgelöst
    public void actionPerformed(ActionEvent evt) {
        // DeltaTime berechnen (vergangene Zeit in Sekunden seit dem letzten Frame)
/*      long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime    = currentTime;
 */
        repaint();
    }

    public void addCollisionActionListener(CollisionActionListener actionListener) {
        collisionListeners.add(actionListener);
    }

    public List<Portal> getPortalList() {
        return portals;
    }

    /**
     * Holt den Block an gegebener Position aus den staticCollidables und nur von dort.
     * @param col   Spalte
     * @param row   Zeile
     * @return      Block oder NULL, wenn keiner gefunden wurde
     */
    public IsBlock getBlockByColRow(int col, int row) {
        // 0 ist ungültig
        if (col <= 0 || row <= 0 || col > boardWidth/tileSize || row > boardHeight/tileSize) {
            return null;
        }

        int x = (col-1)*tileSize;
        int y = (row-1)*tileSize;
        for (Collidable c : staticCollidables) {
            if ( x == c.getX() && y == c.getY() ) {
                return c;
            }
        }
        return null;
    }

    /**
     * Holt den Block an gegebener Position aus den staticCollidables und nur von dort.
     * @param p     Punkt der x-y-Koordinate
     * @return      Block oder NULL, wenn keiner gefunden wurde
     */
    public IsBlock getBlockByPosition(Point p) {
        return getBlockByXY(p.x, p.y);
    }

    /**
     * Holt den Block an gegebener Position aus den staticCollidables und nur von dort.
     * @param x     x-Koordinate
     * @param y     y-Koordinate
     * @return      Block oder NULL, wenn keiner gefunden wurde
     */
    public IsBlock getBlockByXY(int x, int y) {
        if (x < 0 || y < 0 || x > boardWidth || y > boardHeight) {
            return null;
        }

        Point pa = new Point(x, y);
        Point pb = new Point();
        Dimension d = new Dimension(tileSize, tileSize);
        for (Collidable c : staticCollidables) {
            pb.x = c.getX();
            pb.y = c.getY();
            if ( collision( pa, d, pb, d )) {
                return c;
            }
        }
        return null;
    }

    public Point getRandomSpace() {
        Point pa = new Point();
        Point pb = new Point();
        boolean occupied;
        Dimension d = new Dimension(tileSize, tileSize);
        do {
            occupied = false;
            pa.x = rangen.nextInt(1, boardWidth/tileSize)  * tileSize;
            pa.y = rangen.nextInt(1, boardHeight/tileSize) * tileSize;
            for (Collidable c : staticCollidables) {
                pb.x = c.getX();
                pb.y = c.getY();
                if ( collision( pa, d, pb, d )) {
                    occupied = true;
                    break;
                }
            }
        } while (occupied);
        return pa;
    }

    /**
     * Bewegt den Block durch ein Portal zu angegebenen x-y-Koordninate.
     * Der Block wird nach dem Versetzen einen Schritt bewegt (Kollisionsvermeidung),
     * bevor die Kollisionsprüfung erfolgt.
     * @param moveableBlock zu versetzender, beweglicher Block
     * @param x             x-Koordinate
     * @param y             y-Koordinate
     */
    public void moveThroughPortal(IsMoveableBlock moveableBlock, int x, int y) {
        moveableBlock.setPos(x, y);
        moveableBlock.move();
        detectCollision(moveableBlock);
    }

    /**
     * Bewegt die Block-Position um die angegebene Anzahl Spalten und Zeilen weiter.
     * @param moveableBlock zu versetzender, beweglicher Block
     * @param deltaCol      Spaltendifferenz (x)
     * @param deltaRow      Zeilendifferenz  (y)
     */
    public void movePosition(IsMoveableBlock moveableBlock, int deltaCol, int deltaRow) {
        int destinationX = moveableBlock.getX() + deltaCol*tileSize;
        int destinationY = moveableBlock.getY() + deltaRow*tileSize;
        setPositionXY(moveableBlock, destinationX, destinationY);
    }

    /**
     * Setzt die Block-Position auf die angegebene Spalte und Zeile.
     * @param moveableBlock zu versetzender, beweglicher Block
     * @param column        Spalte (x)
     * @param row           Zeile  (y)
     */
    public void setPositionColRow(IsMoveableBlock moveableBlock, int column, int row) {
        // 0 ist ungültig
        if (column == 0 || row == 0) {
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

        setPositionXY(moveableBlock, targetColumn*tileSize, targetRow*tileSize);
    }

    /**
     * Setzt den Block an die angegebene x-y-Koordinate.
     * Die Kollisionserkennung wird sofort nach dem Umsetzen ausgeführt.
     * @param moveableBlock zu versetzender, beweglicher Block
     * @param x             x-Koordinate
     * @param y             y-Koordinate
     */
    public void setPositionXY(IsMoveableBlock moveableBlock, int x, int y) {
        moveableBlock.setPos(x, y);
        detectCollision(moveableBlock);
    }

    /**
     * Bewegt den Spieler um die angegebene Anzahl Spalten und Zeilen weiter.
     * @param deltaCol Spaltendifferenz (x)
     * @param deltaRow Zeilendifferenz  (y)
     */
    public void movePlayer(int deltaCol, int deltaRow) {
        int destinationX = getPlayer().getX() + deltaCol*tileSize;
        int destinationY = getPlayer().getY() + deltaRow*tileSize;
        setPlayerPositionXY(destinationX, destinationY);
    }

    /**
     * Setzt die Spieler-Position auf die angegebene Spalte und Zeile.
     * @param column Spalte (x)
     * @param row    Zeile  (y)
     */
    public void setPlayerPositionColRow(int column, int row) {
        setPlayerPositionXY(column*tileSize, row*tileSize);
    }

    /**
     * Setzt die Spieler-Position auf diese X-Y-Koordinaten.
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    public void setPlayerPositionXY(int x, int y) {
        if (x < 0 || y < 0 || x > boardWidth-tileSize || y > boardHeight-tileSize) {
            System.err.println("Koordinaten für Spielerposition " + x + ", " + y + " ungültig -> wird ignoriert.");
            return;
        }

        saveCurrentPlayerPosition();
        getPlayer().setX(x);
        getPlayer().setY(y);
        detectCollision( getPlayer() );
    }

    protected void saveCurrentPlayerPosition() {
        lastPlayerPos.x = getPlayer().getX();
        lastPlayerPos.y = getPlayer().getY();
    }

    /**
     * Setzt die Position des Spielers auf die Position vor dem Bewegungsbefehl zurück.
     */
    protected void resetPlayerPosition() {
        getPlayer().setX(lastPlayerPos.x);
        getPlayer().setY(lastPlayerPos.y);
    }

    protected boolean isOutOfMap(Collidable collider) {
        if (collider.getX() < 0) {
            return true;
        } else if (collider.getX() >= boardWidth) {
            return true;
        } else if (collider.getY() < 0) {
            return true;
        } else if (collider.getY() >= boardHeight) {
            return true;
        }
        return false;
    }

    protected void detectCollision(Collidable source) {
        if (!(source instanceof Moveable)) {
            // ist die Quelle nicht beweglich, keine Kollisionsprüfung
            return;
        }

        if ( isOutOfMap( source )) {
            if ( source != getPlayer() ) {
                // Objekte (außer Spieler) außerhalb der Map entfernen
                System.err.println(source + " is out of map: removing from map ...");
                remove(source);
            } else {
                Block target = new Block(source.getX(), source.getY(), source.getWidth(), source.getHeight(), BOUNDARY);
//              fireEvent(this, source, target); // über IsCollisionHandler (Block) ausgelöst
                lastImpassableCollisionTarget = target;
                resetPlayerPosition();
            }
            return;
        }
/*      Eine Rakete ist normalerweise Quelle, nicht Ziel, einer Kollision: Wir ignorieren sie daher an dieser Stelle:
        Da Raketen keine Ziele von Kollisionen sind, kollidieren sie auch nicht miteinander.
        // Kollisionsprüfung der Missiles
        for (Collidable target : missiles) {
            if ( collision( source, target )) {
                processCollision(source, target);
                return;
            }
        }
 */
        // Kollisionsprüfung der umherwandernden Collidables
        for (Collidable target : scurryables) {
            if ( collision( source, target )) {
                processCollision(source, target);
                return;
            }
        }

        // Kollisionsprüfung der statischen Collidables
        for (Collidable target : staticCollidables) {
            if ( collision( source, target )) {
                processCollision(source, target);
                return;
            }
        }
    }

    protected void processCollision(Collidable source, Collidable target) {
        // fireEvent(this, source, target); // wird über IsCollisionHander von Collidable.onCollision ausgelöst
        boolean passable = target.onCollision(source, this, this); // löst das Collision-Ereignis am Ziel aus
        source.collidesWith(target, this); // Kollisions-Ereignis an die Quelle übergeben (kann optional abgefangen werden)
        if ( !passable && source == getPlayer() ) {
            lastImpassableCollisionTarget = target;
            resetPlayerPosition();
        }
    }

    @Override
    public void fireEvent(GameMap map, Collidable source, Collidable target) {
        // collider/initiator (bewegliches Objekt), obstacle (statisches Hindernis)
        System.out.println(
            "Collision of block "   + source.getBlockType() +
            " with block "          + target.getBlockType() +
            " at pixel "            + target.getX() + ", " + target.getY() +
            " (column "             + ( target.getX() / target.getWidth()  + 1 ) + // +1, um keine Indizes auszugeben
            ", row "                + ( target.getY() / target.getHeight() + 1 ) + // +1, um keine Indizes auszugeben
            ")."
        );
        CollisionEvent event = new CollisionEvent(this, source, target);
        collisionListeners.forEach( actionListener -> actionListener.collisionPerformed( event ));
    }

    public final boolean collision(Point pa, Dimension da, Point pb, Dimension db) {
        if (pa == pb) {
            return false;
        }
        if ( pa.getX() == pb.getX() && pa.getY() == pb.getY() ) {
            return false;
        }
        return  pa.getX() < pb.getX() + db.getWidth()  &&
                pa.getX() + da.getWidth() > pb.getX()  &&
                pa.getY() < pb.getY() + db.getHeight() &&
                pa.getY() + da.getHeight() > pb.getY();
    }

    public final boolean collision(Collidable a, Collidable b) {
        if (a == null || b == null || a == b) {
            return false;
        }
        return  a.getX() < b.getX() + b.getWidth()  &&
                a.getX() + a.getWidth() > b.getX()  &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    private void update(Graphics2D g2d) {
        setRenderingHints(g2d);
        draw(g2d);
        move();
        clean();
    }

    protected void setRenderingHints(Graphics2D g2d) {
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
        g2d.setClip(0, 0, visibleWidth, visibleHeight); // nur sichtbarer Bereich, nicht die volle Kartengröße
        if (active) {
            update(g2d);
        }
    }

    protected void draw(Graphics2D g2d) {
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        draw(g2d, offsetX, offsetY, renderables);
    }

    protected void draw(Graphics2D g2d, int offsetX, int offsetY, IsBlockType... excludedTiles) {
        Set<IsBlockType> excludedTypes =
            excludedTiles == null ? Set.of() : Arrays.stream(excludedTiles)
                                                     .filter(Objects::nonNull)
                                                     .collect( Collectors.toSet() );
        Collection<Renderable> currentBlocks =
            renderables.stream()
                       .filter(r -> {
                           if (r instanceof Block block) {
                               return !excludedTypes.contains( block.getBlockType() );
                           }
                           return false; // Renderables, die keine Blöcke sind, ausschließen: nackte Map
                       })
                       .collect( Collectors.toList() );
        draw(g2d, offsetX, offsetY, currentBlocks);
    }

    protected void draw(Graphics2D g2d, int offsetX, int offsetY, Collection<Renderable> renderables) {
        drawSpace(g2d, offsetX, offsetY);
        for (Renderable r : renderables) {
            r.draw(g2d, offsetX, offsetY);
        }
    }

    /**
     * Bewegt alles selbstständig Bewegliche auf der Map einen Schritt weiter.
     */
    protected void move() {
        for (IsMissile missile : missiles) {
            missile.move();
            detectCollision(missile);
        }
        for (IsMoveableTile tile : scurryables) {
            tile.move();
            detectCollision(tile);
        }
    }

    /**
     * Bereinigt die Map von allen nicht mehr darzustellenden Renderables.
     */
    protected void clean() {
        renderables.removeAll(garbageR);
        garbageR.clear();
        missiles.removeAll(garbageM);
        garbageM.clear();
        scurryables.removeAll(garbageS);
        garbageS.clear();
        staticCollidables.removeAll(garbageC);
        garbageC.clear();
    }

    protected void drawSpace(Graphics2D g2d, int offsetX, int offsetY) {
        if (emptyTile != null && emptyTile.getImage() != null) {
            BufferedImage spaceImage = emptyTile.getImage();
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    g2d.drawImage(
                        spaceImage,
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
        return getBlockCount(ENEMY);
    }

    /**
     * Gibt die Anzahl der Collidables des angegebenen Typs zurück.
     * Es werden nur statische und umherwandernde Collidables (staticCollidables, scurryables) gezählt,
     * keine Raketen (missiles).
     * @param bType BlockType
     * @return      aktuelle Anzahl der Collidables auf der Map
     */
    public int getBlockCount(IsBlockType bType) {
        int value = 0;
        for (Collidable c : staticCollidables) {
            if ( c.getBlockType() == bType ) {
                ++value;
            }
        }
        for (Collidable c : scurryables) {
            if ( c.getBlockType() == bType ) {
                ++value;
            }
        }
        return value;
    }

    public void add(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof HasImage img && obj instanceof Renderable r) {
            if (img.getImage() != null) {
                renderables.add(r);
            }
        }
        if (obj instanceof Portal portal) {
            portals.add(portal);
        }
        switch (obj) {
            // CAVE: Reihenfolge ist essentiell - nicht ändern!
            case IsMissile missile       -> missiles.add(missile);
            case IsMoveableTile moveable -> scurryables.add(moveable);
            case Collidable collidable   -> staticCollidables.add(collidable);
            default                      -> {}
        }
    }

    public void remove(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Renderable renderable) {
            garbageR.add(renderable);
        }
        if (obj instanceof IsMissile missile) {
            garbageM.add(missile);
        }
        if (obj instanceof IsMoveableTile moveable) {
            garbageS.add(moveable);
        }
        if (obj instanceof Collidable collidable) {
            garbageC.add(collidable);
        }
        if (obj instanceof Portal portal) {
            portals.remove(portal);
        }
    }

}
