package Model;

import Controller.Controller;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * A WaterNode osztály egy absztrakt osztály, leszármazottjai a Cistern, Spring és a Pump osztályok
 */
public abstract class WaterNode extends Steppable implements Serializable {
    /**
     * a WaterNode-ba csatlakozó csövek listája
     */
    protected LinkedList<Pipe> pipes = new LinkedList<>();

    /**
     * A függvényt hívó játékos rákerül a WaterNode-ra,
     * és az eltárolja a players attribútumában.
     * @param player a mezőre belépni akaró játékos
     * @return a lépés sikeressége
     */
    @Override
    public boolean PlayerEnter(Player player) {

        boolean successful = pipes.contains(player.getStandingOn()) || player.getStandingOn() == null;
        if (successful)
            players.add(player);
        else
            IO_Manager.writeInfo("Can't move to " + Controller.getInstance().getObjectName(this) + ", it isn't connected here", Controller.filetoWrite != null);
        return successful;

    }

    /**
     * Amennyiben a játékos leléphet a WaterNode
     * objektumról, akkor lekerül róla, és az eltávolítja a players attribútumából.
     * @param player a mezőről lelépő játékos
     */
    @Override
    public void PlayerExit(Player player) {
        players.remove(player);
    }


    /**
     * A pickup objektum felvétele erről az objektumról.
     * @param pickup az adott elem, amit fel akarunk venni
     * @return ha sikerült felvenni, akkor true-val tér vissza, különben false-sal
     */
    public abstract boolean PickedUpFrom(PickupAble pickup);

    /**
     * Egy cső letétele erre az objektumra. Létezik pumpa letételére is ez a függvény.
     * @param pickup a Pipe amit le akarunk rakni
     * @return ha sikerült lerakni, akkor true-val tér vissza, különben false-sal
     */
    public abstract boolean PlacedDownTo(Pipe pickup);

    /**
     * Egy pumpa letétele erre az objektumra. Létezik cső letételére is ez a függvény.
     * @param pickup a Pump amit le akarunk rakni
     * @return ha sikerült lerakni, akkor true-val tér vissza, különben false-sal
     */
    public abstract boolean PlacedDownTo(Pump pickup);

    /**
     * Víz folyatása ebben a végpontban, absztrakt metódus.
     */
    public abstract void WaterFlow();

    /**
     * Cső hozzáadása a WaterNode-hoz
     * @param p hozzáadandó cső
     * @return ha sikerült hozzáadni a csövet a WaterNode-hoz, akkor true-val tér vissza, különben false-sal
     */
    public boolean AddPipe(Pipe p) {
        pipes.add(p);
        return true;

    }

    /**
     * Cső leválasztása a WaterNode-ról
     * @param p leválasztandó cső
     */
    public void RemovePipe(Pipe p) {
        pipes.remove(p);
    }

    //Visszatér a csövek listájával.
    public LinkedList<Pipe> getPipes() {
        return pipes;
    }

    /**
     * Megvizsgálja, hogy szabad-e valamelyik szomszédos csöve
     * @return true, ha leléphet egy rá kötött csőre, különben false
     */
    @Override
    public boolean canMoveFromHere() {
        boolean canMove=false;
        for(Pipe p:pipes){
            if(p.players.isEmpty())
                canMove=true;
        }
        return canMove;
    }

    /**
     * Mozoghat-e ide a játékos
     * @param p - a játékos
     * @return mozoghat-e
     */
    @Override
    public boolean canMoveToHere(Player p){
        return pipes.contains(p.getStandingOn());
    }
}
