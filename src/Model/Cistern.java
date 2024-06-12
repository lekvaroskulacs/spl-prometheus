package Model;

import Controller.Controller;
import View.GameView;
import View.PipeView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 * Ciszterna megvalósítására szolgáló osztály
 */
public class Cistern extends WaterNode implements Serializable {
    /**
     * Ciszternáról felvehető elemek, amik generálódtak rajta
     */
    private LinkedList<PickupAble> createdPickupables = new LinkedList<>();
    /**
     * Ciszternán keletkezett csövek listája
     */
    private LinkedList<Pipe> generatedPipes = new LinkedList<>();
    /**
     * Ciszternán keletkezett pumpák listája
     */
    private LinkedList<Pump> generatedPumps = new LinkedList<>();

    /**
     * Pontszámításhoz használt singleton
     */
    private final PointCounter counter;

    /**
     * Ciszterna konstruktora
     */
    public Cistern() {
        counter = PointCounter.getInstance();
    }

    /**
     * PickupAble felvétele esetén hívódik, a
     * ciszternában lerendezi a Pickupable felvételének következményeit.
     * @param pickup az adott elem, amit fel akarunk venni
     * @return a felvétel eredménye
     */
    @Override
    public boolean PickedUpFrom(PickupAble pickup) {
        generatedPumps.remove(pickup);
        generatedPipes.remove(pickup);
        boolean result = createdPickupables.remove(pickup);
        if (!result)
            result = pipes.remove(pickup);
        return result;
    }

    /**
     * PickupAble lerakása esetén hívja egy
     * mechanic ha a ciszternán áll, ekkor beköti a ciszternába a kezében lévő csövet
     * @param pickup a Pipe amit le akarunk rakni
     * @return a felvétel eredménye(mivel ciszternára lehet csövet kötni ezért ez mindig kivitelezhető)
     */
    @Override
    public boolean PlacedDownTo(Pipe pickup) {

        AddPipe(pickup);
        pickup.AddWaterNode(this);
        return true;

    }

    /**
     * Ez a ciszternára pumpa lerakásának függvénye, visszatérve jelzi, hogy ez nem lehetséges
     * @param pickup a Pump amit le akarunk rakni
     * @return a felvétel eredménye(mivel ciszternára nem lehet pumpát kötni ezért ez sose kivitelezhető)
     */
    @Override
    public boolean PlacedDownTo(Pump pickup) {
        IO_Manager.writeInfo("Can't place it here", Controller.filetoWrite != null);
        return false;
    }



    /**
     * Minden játékos köre végén hívódik, a vízfolyást kezeli, itt a
     * belekötött csövekből veszi át a vizet, valamint jelez a pontszámlálónak, hogy nőtt a
     * szerelők pontszáma.
     */
    @Override
    public void WaterFlow() {
        for (Pipe p : pipes) {
            int lost = p.LoseWater(1);
            counter.AddMechanicPoints(lost);
            IO_Manager.write(Controller.getInstance().getObjectName(p) + " lost " + lost, Controller.filetoWrite != null);
        }
    }

    /**
     * A generálás függvénye, ami random generál egy pumpát vagy csövet. Ha már van 3 generált akkor nem készít többet
     */
    public void GeneratePickupables() {
        if (createdPickupables.size() > 3)
            return;
        Random rand = new Random();
        if (rand.nextInt(0, 10) == 9) {
            String pumpName = "genPump" + Controller.getInstance().createdPumpNumber++;
            Controller.getInstance().create(pumpName, "pump", -50, -50);
            Pump p = (Pump) Controller.getInstance().getObjectCatalog().get(pumpName);
            createdPickupables.add(p);
            generatedPumps.add(p);
        }
        if (rand.nextInt(0, 10) == 9) {
            String pipeName = "genPipe" + Controller.getInstance().createdPipeNumber++;
            Controller.getInstance().create(pipeName, "pipe", 0, 0);
            Pipe pi = (Pipe) Controller.getInstance().getObjectCatalog().get(pipeName);
            createdPickupables.add(pi);
            generatedPipes.add(pi);
            pi.AddWaterNode(this);
            AddPipe(pi);

            GameView gameView = Controller.getInstance().getGameView();
            PipeView pipeView = new PipeView(
                    gameView.getDrawableByCorrespondingModel(this),
                    gameView.getDrawableByCorrespondingModel(this),
                    5,
                    pi,
                    gameView);
            gameView.addPipeView(pipeView);
        }
    }

    /**
     * A createdPickupables gettere
     * @return felvehető elemek a ciszternáról
     */
    public LinkedList<PickupAble> getCreatedPickupables() {
        return createdPickupables;
    }

    /**
     * Fel tud-e venni innen pumpát a szerelő
     * @param m - a szerelő
     * @return - fel tud-e venni
     */
    @Override
    public boolean canPickUpPump(Mechanic m){
        return m.standingOn==this && !generatedPumps.isEmpty() && m.getHeldItems()==null;
    }
    /**
     * Fel tud-e venni innen csövet a szerelő
     * @param m - a szerelő
     * @return - fel tud-e venni
     */
    @Override
    public boolean canPickUpPipe(Mechanic m){
        return m.standingOn==this && !generatedPipes.isEmpty() && m.getHeldItems()==null;
    }

    /**
     * Le tudja-e rakni a kezében lévő dolgot a szerelő ide
     * @param m - a szerelő
     * @return - le tudja-e rakni
     */
    @Override
    public boolean canPlaceDown(Mechanic m){
        if (m.getHeldItems() == null)
            return false;
        return m.standingOn==this && m.getHeldItems().canBePlacedDownTo(this);
    }

    /**
     * El tud-e innen mozogni a játékos bárhova
     * @return el tud-e
     */
    @Override
    public boolean canMoveFromHere() {
        boolean canMove=false;
        for(Pipe p:pipes){
            if(p.players.isEmpty() && !generatedPipes.contains(p))
                canMove=true;
        }
        return canMove;
    }

    /**
     * Le lehet rakni ide csövet
     * @param p - cső
     * @return Le lehet
     */
    @Override
    boolean canBePlacedDown(Pipe p) {
        return true;
    }

    /**
     * Le lehet rakni ide pumpát
     * @param p - pumpa
     * @return Nem lehet
     */
    @Override
    boolean canBePlacedDown(Pump p) {
        return false;
    }

    /**
     * A generált csövek listájának gettere
     * @return a lista
     */
    public LinkedList<Pipe> getGeneratedPipes() {
        return generatedPipes;
    }
    /**
     * A generált pumpák listájának gettere
     * @return a lista
     */
    public LinkedList<Pump> getGeneratedPumps() {
        return generatedPumps;
    }


}
