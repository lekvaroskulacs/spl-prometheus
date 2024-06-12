package Model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * A Steppable osztály felelős a mozgással és mozgatással kapcsolatos műveletekkel
 */
public abstract class Steppable implements Serializable {
    /**
     * a Steppable elemeken tartózkodó játékosok listája
     */
    protected LinkedList<Player> players = new LinkedList<>();

    /**
     * A függvény az adott játékos belépési szándékát adja meg
     * @param player a mezőre belépni akaró játékos
     * @return sikeres / sikertelen a belépési kísérlet
     * Alapesetben false-szal tér vissza, a leszármazottakban overrideolva van.
     */
    public boolean PlayerEnter(Player player) { return false; }

    /**
     * A függvény az adott játékos lelépési szándékát adja meg, leszármazottakban történik a megvalósítása.
     * @param player a mezőről lelépő játékos
     */
    public void PlayerExit(Player player) {}

    /**
     * Az adott elem ki- és bemeneti állapotát állítja be, leszármazottakban megvalósítás.
     *
     * @param in  bemeneti állapot
     * @param out kimeneti állapot
     * @return az átirányítás sikeressége
     */
    public boolean PlayerRedirect(Pipe in, Pipe out) {
        return false;
    }

    /**
     * Pályaelem felvétele
     * @param pickup az adott elem, amit fel akarunk venni
     * @return sikeres / sikertelen volt az elem felvétele a pályáról
     * Absztrakt függvény, leszármazottakban van a megvalósítása.
     */
    public abstract boolean PickedUpFrom(PickupAble pickup);

    /**
     * Pipe letétele erre az objektumra
     * @param pickup a Pipe amit le akarunk rakni
     * @return lerakás sikeressége
     * Absztrakt, leszármazottakban van a megvalósítása.
     */
    public abstract boolean PlacedDownTo(Pipe pickup);

    /**
     * Pump letétele erre az objektumra
     * @param pickup a Pump amit le akarunk rakni
     * @return lerakás sikeressége
     */
    public abstract boolean PlacedDownTo(Pump pickup);

    //oopsie, itt amúgy a paraméter Pump, nem Pipe ahogy a doksiban van

    /**
     * Leszármazottban valósul meg a funkciója
     * @param pump lerakandó pumpa
     */
    public void CutInHalf(Pump pump) {}

   //Pályelem lyukasztása, alapesetben false-szal tér vissza, Pipe osztályban overrideolva van.
    public boolean Pierced() { return false; }

   //Pályaelem megjavítása, mindig false-szal tér vissza, a Cistern és Spring osztályok kivételével overrideolva van a leszármazottakban.
    public boolean Repaired() { return false; }

    //Ragacsossá tétel függvénye, ami itt mindig hamissal tér vissza, a Pipe osztályban override-olva van.
    public boolean Glued(){return false;}

    //Csúszóssá tétel függvénye, ami itt mindig hamissal tér vissza, a Pipe osztályban override-olva van.
    public boolean Lubricated(){return false;}

    /**
     * Aktív bemenet megadása
     * @return  aktív bemenet
     */
    public Pipe getActiveIn() { return null; }

    /**
     * Aktív kimenet megadása
     * @return aktív kimenet
     */
    public Pipe getActiveOut() { return null; }

    /**
     * Bemeneti cső beállítása
     * @param p bemeneti cső
     */
    public void setActiveIn(Pipe p) {}

    /**
     * Kimeneti cső beállítása
     * @param p kimeneti cső
     */
    public void setActiveOut(Pipe p) {}

    //Visszatér a játékosok listájával.
    public LinkedList<Player> getPlayers() {
        return players;
    }

    //Absztrakt függvény, leszármazottakban van megvalósítva.
    public abstract boolean canMoveFromHere();
    public boolean canMoveToHere(Player p){
        return false;
    }
    public boolean canLubricate(Player p){
        return false;
    }
    public boolean canGlue(Player p){
        return false;
    }
    public boolean canRedirect(Player p){
        return false;
    }
    public boolean canPierce(Player p){
        return false;
    }
    public boolean canRepair(Player p){
        return false;
    }

    public boolean canPickUpPipe(Mechanic m){
        return false;
    }
    public boolean canPickUpPump(Mechanic m){
        return false;
    }
    public boolean canPlaceDown(Mechanic m){
        return false;
    }
    abstract boolean  canBePlacedDown(Pipe p);
    abstract boolean  canBePlacedDown(Pump p);
}
