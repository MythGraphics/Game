/*
 *
 */

package game.combat;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.Resource;
import static game.combat.AmmoType.PROJECTILE;
import static game.combat.ArmorType.*;
import static game.combat.CombatantType.*;
import static game.combat.DamageType.*;
import static game.combat.WeaponType.*;
import static graphic.io.BinaryIO.loadImage;
import static graphic.io.ImageUtility.scale;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CombatFactory {

    private static final Random RAND = new Random();

    public enum DefaultEnemy {

        GIFTZERG(CombatFactory::getDefaultPoisonZerg, Group.ZERG),
        SÄUREZERG(CombatFactory::getDefaultAcidZerg, Group.ZERG),
        FEUERZERG(CombatFactory::getDefaultFireZerg, Group.ZERG),
        SCHAMANENZERG(CombatFactory::getDefaultShamanZerg, Group.ZERG),
        NUKLEARZERG(CombatFactory::getDefaultNukeZerg, Group.ZERG),
        KRIEGERZERG(CombatFactory::getDefaultWarriorZerg, Group.ZERG),

        HASE(CombatFactory::getDefaultRabbit, Group.ANIMAL),
        KATZE(CombatFactory::getDefaultCat, Group.ANIMAL),
        RATTE(CombatFactory::getDefaultRat, Group.ANIMAL);

        private final Supplier<Enemy> factory;
        private final Group group;

        DefaultEnemy(Supplier<Enemy> factory, Group group) {
            this.factory = factory;
            this.group = group;
        }

        public Enemy create() {
            return factory.get();
        }

    }

    public enum Group {
        ZERG,
        ANIMAL;
    }

    public static List<Enemy> createRandomZergSquad(int size) {
        return createRandomSquad(Group.ZERG, size);
    }

    public static List<Enemy> createRandomAnimalSquad(int size) {
        return createRandomSquad(Group.ANIMAL, size);
    }

    public static Enemy createRandomEnemy(Group group) {
        List<DefaultEnemy> blueprints = getBlueprintsByGroup(group);
        return blueprints.get( RAND.nextInt( blueprints.size() )).create();
    }

    public static List<Enemy> createRandomSquad(Group group, int size) {
        List<DefaultEnemy> blueprints = getBlueprintsByGroup(group);
        // per Zufall 'size'-oft einen Blueprint wählen und eine neue Instanz erzeugen
        return IntStream.range(0, size)
                        .mapToObj( i -> blueprints.get( RAND.nextInt( blueprints.size() )).create() )
                        .toList();
    }

    private static List<DefaultEnemy> getBlueprintsByGroup(Group group) {
        // alle verfügbaren Blueprints dieser Gruppe sammeln
        return Arrays.stream( DefaultEnemy.values() )
                     .filter(e -> e.group == group)
                     .toList();
    }

    private String name;
    private CombatantType cType;
    private Weapon weapon1, weapon2;

    private final ArrayList<Armor> armorList;

    public CombatFactory() {
        armorList = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CombatantType cType) {
        this.cType = cType;
    }

    public void addArmor(Armor armor) {
        armorList.add(armor);
    }

    public void setWeapon(Weapon weapon) {
        if ( weapon1 == null ) {
            weapon1 = weapon;
            return;
        }
        if ( weapon2 == null ) {
            weapon2 = weapon;
        }
    }

    public Combatant build() throws NullPointerException {
        checkNull();
        Combatant c = new Combatant(name, cType);
        addArmorWeapon(c);
        return c;
    }

    public Player buildPlayer(Resource health) throws NullPointerException {
        checkNull();
        Player c = new Player(name, cType, health, 0);
        addArmorWeapon(c);
        return c;
    }

    public Enemy buildEnemy() throws NullPointerException {
        checkNull();
        Enemy c = new Enemy(name, cType, 0);
        addArmorWeapon(c);
        return c;
    }

    private void checkNull() throws NullPointerException {
        Objects.requireNonNull(name, "Name darf nicht null sein.");
        Objects.requireNonNull(cType, "Combatant-Typ darf nicht null sein.");
    }

    private void addArmorWeapon(Combatant c) {
        armorList.forEach( armor -> c.addArmor( armor ));
        c.addWeapon(weapon1);
        c.addWeapon(weapon2);
    }

    public static Player getDefaultPlayer(Resource health, CombatantType cType) {
        return switch (cType) {
            case APOTHEKER -> getDefaultPharmacist(health);
            case KRIEGER   -> getDefaultWarrior(health);
            case MAGIER    -> getDefaultMage(health);
            case SCHAMANE  -> getDefaultShaman(health);
            case CHEMIKER  -> getDefaultChemist(health);
            case SOLDAT    -> getDefaultSoldier(health);
        };
    }

    public static Player getDefaultPharmacist(Resource health) {
        Player c = new Player( "Apokalyptischer Apotheker", APOTHEKER, health, 0 );
        c.addArmor( new Armor( "Antikörper", ANTIKÖRPER, 50 ));
        c.addArmor( new Armor( "Kittel", MECHANISCH, 10 ));
        c.addWeapon( new Weapon( -1, "Pistill", KNÜPPEL, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultWarrior(Resource health) {
        Player c = new Player( "Poppeye", KRIEGER, health, 0 );
        c.addArmor( new Armor( "Dicke Muckies", MECHANISCH, 50 ));
        c.addArmor( new Armor( "Matrosenhemd", MECHANISCH, 10 ));
        c.addWeapon( new Weapon( -1, "Pfeife", KNÜPPEL, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultMage(Resource health) {
        Player c = new Player( "Gandalf", MAGIER, health, 0 );
        c.addArmor( new Armor( "Asbestumhang", ASBEST, 50 ));
        c.addArmor( new Armor( "Robe", MECHANISCH, 10 ));
        c.addWeapon( new Weapon( -1, "Elderstab", STAB, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultShaman(Resource health) {
        Player c = new Player( "Wetterfrosch", SCHAMANE, health, 0 );
        c.addArmor( new Armor( "Regenumhang", ISOLATOR, 50 ));
        c.addArmor( new Armor( "Regenumhang", MECHANISCH, 10 ));
        c.addWeapon( new Weapon( -1, "knorriger Stab", STAB, PHYSISCH, 10 ));
        return c;
    }

    public static Player getDefaultChemist(Resource health) {
        Player c = new Player( "Walter White", CHEMIKER, health, 0 );
        c.addArmor( new Armor( "säurefester Kittel", KRISTALL, 25 ));
        c.addArmor( new Armor( "säurefester Kittel", MECHANISCH, 10 ));
        c.addWeapon( new Weapon( -1, "Pipette", DOLCH, SÄURE, 10 ));
        return c;
    }

    public static Player getDefaultSoldier(Resource health) {
        Player c = new Player( "SpaceMarine Johannis Kraut", SOLDAT, health, 0 );
        c.addArmor( new Armor( "Space Suit", ANTIKÖRPER, 100 ));
        c.addArmor( new Armor( "Space Suit", ABSCHIRMEND, 90 ));
        c.addArmor( new Armor( "Space Suit", MECHANISCH, 75 ));
        c.addArmor( new Armor( "Space Suit", ASBEST, 20 ));
        c.addArmor( new Armor( "Space Suit", KRISTALL, 10 ));
        c.addArmor( new Armor( "Space Suit", ISOLATOR, 25 ));
        AmmoWeapon weapon = new AmmoWeapon( -1, "BFG Sturmgewehr", GEWEHR, PROJECTILE );
        weapon.addAmmo( new Ammo( "Plasmaladung", PROJECTILE, 20, 10, new Damage( NUKLEAR, 25 )));
        c.addWeapon(weapon);
        return c;
    }

    public static Enemy getDefaultFireZerg() {
        Enemy c = new Enemy( "Höllenzerg", MAGIER, 1 );
        c.addArmor( new Armor( "Asbestschuppen", ASBEST, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultPoisonZerg() {
        Enemy c = new Enemy( "Schleimtriefender Zerg", APOTHEKER, 1 );
        c.addArmor( new Armor( "Antikörper", ANTIKÖRPER, 50 ));
        c.addArmor( new Armor( "Chitinschuppen", MECHANISCH, 20 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "giftiger Stachel", SCHWERT, GIFT, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultShamanZerg() {
        Enemy c = new Enemy( "Elektrisierter Zerg", SCHAMANE, 1 );
        c.addArmor( new Armor( "Dielektrischer Schleim", ISOLATOR, 50 ));
        c.addArmor( new Armor( "Chitinschuppen", MECHANISCH, 20 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultAcidZerg() {
        Enemy c = new Enemy( "Ätzender Zerg", CHEMIKER, 1 );
        c.addArmor( new Armor( "Kristallschuppen", KRISTALL, 50 ));
        c.addArmor( new Armor( "Dicke Haut", MECHANISCH, 20 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 25 ));
        c.addWeapon( new Weapon( -1, "säurehaltiger Stachel", SCHWERT, SÄURE, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultNukeZerg() {
        Enemy c = new Enemy( "Strahlender Zerg", SOLDAT, 1 );
        c.addArmor( new Armor( "Wolfram-Iridium-Schuppen", ABSCHIRMEND, 50 ));
        c.addArmor( new Armor( "Wolfram-Iridium-Schuppen", MECHANISCH, 30 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultWarriorZerg() {
        Enemy c = new Enemy( "Mucki-Zerg", KRIEGER, 1 );
        c.addArmor( new Armor( "Chitinpanzerplatten", MECHANISCH, 75 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Klauen", DOLCH, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "langer, spitzer Stachel", SCHWERT, PHYSISCH, 10 ));
        c.setImg( scale( loadImage( "sprites/minion/zerg.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultRat() {
        Enemy c = new Enemy( "Ranzige Ratte", APOTHEKER, 1 );
        c.addArmor( new Armor( "verfilztes Fell", MECHANISCH, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Krallen", GLEVE, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "giftiger Biss", DOLCH, GIFT, 15 ));
        c.setImg( scale( loadImage( "sprites/minion/rat.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultCat() {
        Enemy c = new Enemy( "Mucki-Mietze", KRIEGER, 1 );
        c.addArmor( new Armor( "dickes Fell", MECHANISCH, 50 ));
        c.addWeapon( new Weapon( -1, "rasiermesserscharfe Krallen", GLEVE, PHYSISCH, 5 ));
        c.addWeapon( new Weapon( -1, "kräftiger Biss", DOLCH, PHYSISCH, 5 ));
        c.setImg( scale( loadImage( "sprites/minion/cat.png" ), 100 ));
        return c;
    }

    public static Enemy getDefaultRabbit() {
        Enemy c = new Enemy( "Heißer Hoppler", MAGIER, 1 );
        c.addArmor( new Armor( "Asbestfell", ASBEST, 50 ));
        c.addWeapon( new Weapon( -1, "brennende Krallen", GLEVE, FEUER, 5 ));
        c.addWeapon( new Weapon( -1, "brennender Biss", DOLCH, FEUER, 5 ));
        c.setImg( scale( loadImage( "sprites/minion/rabbit3.png" ), 100 ));
        return c;
    }

}
