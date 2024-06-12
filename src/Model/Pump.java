package Model;

import Controller.Controller;

import java.io.Serializable;

/**
 * Pumpa funkcióinak megvalósítása
 */
public class Pump extends WaterNode implements PickupAble, Serializable {
    /**
     * törött-e a pumpa
     */
    private boolean broken = false;
    /**
     * a pumpa víztartályának kapacitása
     */
    private int waterCapacity = 20;
    /**
     * a tárolt víz mennyisége
     */
    private int heldWater = 0;
    /**
     * a maximum beköthető csövek
     */
    private int maximumPipes = 10;
    /**
     * Az a cső amin a víz bejöhet
     */
    private Pipe activeIn = null;
    /**
     * Az a cső amin a víz kimehet
     */
    private Pipe activeOut = null;

    /**
     * Akkor hívódik, ha egy játékos felveszi ezt a pumpát(amit nem tud)
     * @param from melyik elemről vesszük le / csatlakoztatjuk le a pumpát
     */
    @Override
    public void PickedUp(Steppable from) {}

    /**
     * Pumpa letétele
     * @param to melyik elemre tesszük le / csatlakoztatjuk rá a pumpát
     * @return ha sikerül letenni, akkor true-val tér vissza, különben false-szal
     */
    @Override
    public boolean PlacedDown(Steppable to) {
        return to.PlacedDownTo(this);
    }
    /**
     * A pickup felvétele a pumpárópl
     * @param pickup az adott elem, amit fel akarunk venni
     * @return amennyiben az elemet sikeresen felvették true-val tér vissza, különben false-szal
     */
    @Override
    public boolean PickedUpFrom(PickupAble pickup) {

        int pickupIdx = pipes.indexOf(pickup);
        if (pickupIdx != -1) {
            pipes.remove(pickupIdx);
            if(pickup == activeIn){
                activeIn = null;
            }
            if(pickup == activeOut){
                activeOut = null;
            }
            return true;
        }
        return false;

    }

    /**
     * Egy cső lerakása a pumpára
     * @param pickup a Pipe amit le akarunk rakni
     * @return amennyiben sikeresen le lehet helyezni a csövet a pumpára true-val tér vissza, különben false-szal
     */
    @Override
    public boolean PlacedDownTo(Pipe pickup) {
        boolean successful = AddPipe(pickup);
        if (successful)
            pickup.AddWaterNode(this);
        return successful;
    }

    /**
     * Egy pumpa lerakása a pumpára
     * @param pickup a Pump amit le akarunk rakni
     * @return mindig false-szal tér vissza, hiszen pump-ra nem lehet másik pump-ot helyezni
     */
    @Override
    public boolean PlacedDownTo(Pump pickup) {
        IO_Manager.writeInfo("Can't place it here", Controller.filetoWrite != null);
        return false;
    }

    /**
     * a vízfolyás függvénye, először átadja a vizet a kimenő csőnek, ha a pumpa nem romlott, vagy a tárolója nem üres, majd elveszi a vizet a bemeneti csőtől
     */
    @Override
    public void WaterFlow() {

        if (broken) {
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is broken", Controller.filetoWrite != null);
        }
        int gained=0;
        if(!broken && heldWater != 0 && activeOut != null)
        {
            gained= activeOut.GainWater(1);
            if(gained==1){
                heldWater--;
            }
            IO_Manager.write(Controller.getInstance().getObjectName(activeOut) + " gained " + gained, Controller.filetoWrite != null);
        } else if (heldWater == 0) {
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is empty", Controller.filetoWrite != null);
        }

        if (activeIn != null) {

            int lost = activeIn.LoseWater(1);
            int excess = (heldWater + lost > waterCapacity) ? (heldWater + lost - waterCapacity) : 0;
            activeIn.GainWater(excess);
            heldWater +=lost-excess;
            IO_Manager.write(Controller.getInstance().getObjectName(activeIn) + " lost " + (lost - excess), Controller.filetoWrite != null);
        }
    }

    /**
     * @param in  bemeneti állapot
     * @param out kimeneti állapot
     *            beállítja a bemeneti és kimeneti csöveket a paraméterek alapján
     * @return ha az átállítani kívánt elemek valóban rá vannak kötve a pumpára, akkor true-val tér vissza,
     * különben false-szal
     */
    @Override
    public boolean PlayerRedirect(Pipe in, Pipe out) {
        if(pipes.contains(in) && pipes.contains(out)) {
            activeIn = in;
            activeOut = out;
            return true;
        }
        else return false;
    }

    /**
     * @return visszatér a jelenlegi bemeneti csővel
     */
    public Pipe getActiveIn() {
        return activeIn;
    }

    /**
     * @return visszatér a jelenlegi kimeneti csővel
     */
    public Pipe getActiveOut() {
        return activeOut;
    }

    /**
     * @param p bemeneti cső
     * beállítja a p csövet a bemeneti csőre
     */
    public void setActiveIn(Pipe p) {
        activeIn = p;
    }

    /**
     * @param p kimeneti cső
     * beállítja a p csövet az aktív kimeneti csőre
     */
    public void setActiveOut(Pipe p) {
        activeOut = p;
    }

    /**
     * Elemet fel lehet-e venni onnan amit átadunk
     * @param to - ahonnan felvesszük
     * @return fel lehet-e venni
     */
    @Override
    public boolean canBePlacedDownTo(Steppable to) {
        return to.canBePlacedDown(this);
    }

    /**
     * Le lehet-e rakni erre csövet
     * @param p - a cső amit lerakunk
     * @return le lehet-e rakni
     */
    @Override
    boolean canBePlacedDown(Pipe p) {
        return maximumPipes>pipes.size();
    }

    /**
     * Erre nem lehet rakni pumpát
     * @param p - pumpa
     * @return nem lehet
     */
    @Override
    boolean canBePlacedDown(Pump p) {
        return false;
    }

    /**
     * @param p hozzáadandó cső
     * ha a pumpára kötött csövek száma kisebb a maximális értéknél, akkor a csövet a pumpára köti
     * @return ha sikerült a csövet a pumpára kötni, akkor true-val tér vissza, különben false-szal
     */
    @Override
    public boolean AddPipe(Pipe p) {
        boolean valid = pipes.size() < maximumPipes;
        if(valid)
            pipes.add(p);
        else
            IO_Manager.writeInfo("Can't place " + Controller.getInstance().getObjectName(p) + " here, because " +
                    Controller.getInstance().getObjectName(this) + " reached pipe capacity", Controller.filetoWrite != null);
        return valid;

    }

    /**
     * Megjavítja a pumpát, ha törött
     * @return sikeresség
     */
    @Override
    public boolean Repaired(){
        if(broken){
            broken = false;
            return true;
        } else {
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is not broken", Controller.filetoWrite != null);
            return false;
        }
    }

    /**
     * Lehet-e átirányítani a pumpát
     * @param p - a játékos aki átirányítja
     * @return lehet-e
     */
    @Override
    public boolean canRedirect(Player p){
        return p.standingOn==this && pipes.size()>1;
    }
    /**
     * Lehet-e megjavítani a pumpát
     * @param p - a játékos aki megjavítja
     * @return lehet-e
     */
    @Override
    public boolean canRepair(Player p){
        return p.standingOn==this && broken;
    }

    /**
     * Le lehet-e rakni erre azt amit a szerelő fog
     * @param m - a szerelő aki lerakná
     * @return le lehet-e
     */
    @Override
    public boolean canPlaceDown(Mechanic m){
        if (m.getHeldItems() == null)
            return false;
        return m.standingOn==this && m.getHeldItems().canBePlacedDownTo(this);
    }

    //Visszatér a broken értékével.
    public boolean isBroken() {
        return broken;
    }

    //Visszatér a WaterCapacity értékével.
    public int getWaterCapacity() {return waterCapacity;}

    //Visszatér a heldWater értékével.
    public int getHeldWater() {
        return heldWater;
    }

    //Visszatér a maximumPipes értékével.
    public int getMaximumPipes() {
        return maximumPipes;
    }

    //Beállítja a broken értékét a paraméterben megadottra.
    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    //A waterCapacity értékét állítja be.
    public void setWaterCapacity(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    //A heldWater értékét állítja be.
    public void setHeldWater(int heldWater) {
        this.heldWater = heldWater;
    }

    //A MaximumPipes értékét állítja be.
    public void setMaximumPipes(int maximumPipes) {
        this.maximumPipes = maximumPipes;
    }
}
