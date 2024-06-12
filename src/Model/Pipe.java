package Model;

import Controller.Controller;
import View.PipeView;
import View.PumpView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 * Csövet reprezentáló osztály
 */
public class Pipe extends Steppable implements PickupAble, Serializable {

    /**
     * A cső kapacitása, kritérium szerint mindig egy
     */
    private int waterCapacity = 1;
    /**
     * Tárolt víz mennyisége
     */
    private int heldWater = 0;
    /**
     * Törött-e a cső
     */
    private boolean broken = false;
    /**
     * Lehet-e már lyukasztani a csövet
     */
    private boolean readyToPierce = true;
    /**
     * csúszós-e a cső
     */
    private boolean lubricated = false;
    /**
     * ragadós-e a cső
     */
    private boolean glued = false;
    /**
     * fel van-e emelve a cső
     */
    private boolean beingHeld = false;
    /**
     * Mennyi idő múlva lehet lyukasztani
     */
    private int readyToPierceTimer=0;

    /**
     * Csatlakozási pontok
     */
    private LinkedList<WaterNode> nodes = new LinkedList<>();

    /**
     * Pontszámításhoz használt singleton
     */
    private final PointCounter counter;

    /**
     * Pipe konstruktora
     */
    public Pipe() {
        counter = PointCounter.getInstance();
    }

    /**
     * A cső felvételét megvalósító metódus. Mechanic tudja meghívni, ezáltal felemeli a kezébe
     * a cső egyik végét, amennyiben ez lehetséges, és a rajta lévő játékos, ha van, leesik.
     * @param from melyik elemről vesszük le / csatlakoztatjuk le
     */
    @Override
    public void PickedUp(Steppable from) {
        int pickupIdx = nodes.indexOf(from);
        if (pickupIdx != -1) {
            if (nodes.size() > 1)
                nodes.remove(pickupIdx);
            beingHeld=true;
            if (players.size() != 0) {
                IO_Manager.writeInfo(Controller.getInstance().getObjectName(players.getFirst()) + " is fallen off", Controller.filetoWrite != null);
                players.getFirst().setFellDown(true);
            }
            counter.AddSaboteurPoints(heldWater);
            heldWater=0;
        }

    }

    /**
     * Mechanic tudja meghívni, ezáltal lehelyezi a
     * kezéből a cső felemelt végét, amennyiben ez lehetséges.
     * @param to melyik elemre tesszük le / csatlakoztatjuk rá
     * @return igaz, ha sikeres a letétel
     */
    @Override
    public boolean PlacedDown(Steppable to) {
        if (to.PlacedDownTo(this)) {
            beingHeld = false;
            return true;
        }
        return false;
    }

    /**
     * Amennyiben a játékos ráléphet a csőre, akkor
     * rákerül, és az eltárolja a players attribútumában. Egyszerre csak egy játékos lehet egy
     * csövön.
     * @param player a mezőre belépni akaró játékos
     * @return a lépés sikeressége
     */
    @Override
    public boolean PlayerEnter(Player player) {
        if (players.size() != 0) {
            IO_Manager.writeInfo("Can't move to " + Controller.getInstance().getObjectName(this) + ", "
                    + Controller.getInstance().getObjectName(players.getFirst()) + " is standing on it", Controller.filetoWrite != null);
            return false;
        }
        boolean successful = nodes.getFirst()!=null && nodes.getLast()!=null && (nodes.contains(player.getStandingOn()) || player.getStandingOn() == null);

        if (successful){
            players.add(player);

            if (lubricated){
                Random random = new Random();
                int end = random.nextInt(1,2);
                boolean ignoreState = Player.isIgnoreStates();
                Player.setIgnoreStates(true);
                if(end == 1){
                    player.getStandingOn().PlayerExit(player);
                    player.setStandingOn(null);
                    player.Move(nodes.getFirst());
                }
                else if(end == 2){
                    player.getStandingOn().PlayerExit(player);
                    player.setStandingOn(null);
                    player.Move(nodes.getLast());
                }
                Player.setIgnoreStates(ignoreState);
                players.remove(player);
                if (player.getState() == PlayerActionState.moveAction)
                    player.setState(PlayerActionState.specialAction);
                else if (player.getState() == PlayerActionState.specialAction) {
                    player.setState(PlayerActionState.turnOver);
                    Controller.getInstance().turnOver();
                }
                lubricated = false;
                successful = false;
            }

            if (glued) {
                player.setStuck(true);
                player.setGlueLength(1);
                glued = false;
                player.setState(PlayerActionState.turnOver);
                Controller.getInstance().turnOver();
            }

        }
        return successful;
    }

    /**
     * Amennyiben a játékos leléphet a csőről, akkor
     * lekerül róla, és az eltávolítja a players attribútumából.
     * @param player a mezőről lelépő játékos
     */
    @Override
    public void PlayerExit(Player player) {
        players.remove(player);
    }

    /**
     * Ez a függvény akkor hívódik, amikor fel szeretnénk venni valamit egy csőről.
     * @param pickup az adott elem, amit fel akarunk venni
     * @return mindig hamis, mivel csőről nem vehetünk fel semmit
     */
    @Override
    public boolean PickedUpFrom(PickupAble pickup) {
        return false;
    }

    /**
     * Ez a függvény akkor lép életbe, amikor a
     * játékos lehelyez a pickup pumpát a csőre, ezzel kettévágva azt, tehát meghívja a
     * CutInHalf függvényt. A cső két része az új pumpához csatlakozik.
     * @param pickup a Pump amit le akarunk rakni
     * @return lerakás sikeressége
     */
    @Override
    public boolean PlacedDownTo(Pump pickup) {
        CutInHalf(pickup);
        return true;

    }

    /**
     * Ez a függvény akkor lép életbe, amikor a játékos lehelyezne egy csövet erre a csőre. Ez nem lehetséges
     * @param pickup a Pipe amit le akarunk rakni
     * @return hamis, mivel nem lehet csövet csőre rakni
     */
    @Override
    public boolean PlacedDownTo(Pipe pickup) {
        return false;
    }

    /**
     * Akkor hívódik meg, amikor egy pumpát a szerelő lehelyez
     * a csőre, a kettévágást kezeli.
     * @param pump lerakandó pumpa
     */
    @Override
    public void CutInHalf(Pump pump) {

        String newP1="GenPipe"+ Controller.getInstance().createdPipeNumber++;
        String newP2="GenPipe"+Controller.getInstance().createdPipeNumber++;
        Controller.getInstance().create(newP1,"pipe",0,0);
        Controller.getInstance().create(newP2,"pipe",0,0);

        Pipe newPipe1 =((Pipe)Controller.getInstance().getObjectCatalog().get(newP1));
        Pipe newPipe2 =((Pipe)Controller.getInstance().getObjectCatalog().get(newP2));
        newPipe1.AddWaterNode(pump);
        newPipe2.AddWaterNode(pump);

        pump.AddPipe(newPipe1);
        pump.AddPipe(newPipe2);

        Player player=players.getFirst();
        int x=Controller.getInstance().getGameView().getDrawableByCorrespondingModel(player).getX();
        int y=Controller.getInstance().getGameView().getDrawableByCorrespondingModel(player).getY()+25;
        //PLAYER LESZEDÉSE A CSŐRŐL
        player.setStandingOn(null);
        players.remove(player);


        WaterNode node1 = nodes.getFirst();
        WaterNode node2 = nodes.getLast();

        node1.RemovePipe(this);
        node1.AddPipe(newPipe1);

        node2.AddPipe(newPipe2);
        node2.RemovePipe(this);

        newPipe1.AddWaterNode(node1);
        newPipe2.AddWaterNode(node2);

        Pipe node1activeIn = node1.getActiveIn();
        Pipe node1activeOut = node1.getActiveOut();
        if (node1activeIn == this)
            node1.setActiveIn(newPipe1);
        if (node1activeOut == this)
            node1.setActiveOut(newPipe1);

        Pipe node2activeIn = node2.getActiveIn();
        Pipe node2activeOut = node2.getActiveOut();
        if (node2activeIn == this)
            node2.setActiveIn(newPipe2);
        if (node2activeOut == this)
            node2.setActiveOut(newPipe2);

        newPipe1.waterCapacity=this.waterCapacity;
        newPipe1.heldWater=this.heldWater;
        newPipe1.broken= this.broken;
        newPipe1.readyToPierce=this.readyToPierce;
        newPipe1.lubricated=this.lubricated;
        newPipe1.glued=this.glued;
        newPipe1.beingHeld= this.beingHeld;
        newPipe1.readyToPierceTimer=this.readyToPierceTimer;

        newPipe2.waterCapacity=this.waterCapacity;
        newPipe2.heldWater=this.heldWater;
        newPipe2.broken= this.broken;
        newPipe2.readyToPierce=this.readyToPierce;
        newPipe2.lubricated=this.lubricated;
        newPipe2.glued=this.glued;
        newPipe2.beingHeld= this.beingHeld;
        newPipe2.readyToPierceTimer=this.readyToPierceTimer;

        boolean ignoreStates = Player.isIgnoreStates();
        Player.setIgnoreStates(true);
        player.Move(newPipe1);
        Player.setIgnoreStates(ignoreStates);

        Controller.getInstance().removeObject(this);
        Controller.getInstance().getGameView().remove(Controller.getInstance().getGameView().getPipeViewByCorrespondingModel(this));

        Controller.getInstance().getGameView().remove(Controller.getInstance().getGameView().getDrawableByCorrespondingModel(pump));
        PumpView pumpView = new PumpView(x,y,25,pump,null,Controller.getInstance().getGameView());
        Controller.getInstance().getGameView().addPumpView(pumpView);

        PipeView pipeView1 = new PipeView(
                Controller.getInstance().getGameView().getDrawableByCorrespondingModel(newPipe1.getNodes().getFirst()),
                Controller.getInstance().getGameView().getDrawableByCorrespondingModel(newPipe1.getNodes().getLast()),
                5,
                newPipe1,
                Controller.getInstance().getGameView()
        );
        Controller.getInstance().getGameView().addPipeView(pipeView1);
        PipeView pipeView2 = new PipeView(
                Controller.getInstance().getGameView().getDrawableByCorrespondingModel(newPipe2.getNodes().getFirst()),
                Controller.getInstance().getGameView().getDrawableByCorrespondingModel(newPipe2.getNodes().getLast()),
                5,
                newPipe2,
                Controller.getInstance().getGameView()
        );
        Controller.getInstance().getGameView().addPipeView(pipeView2);


    }

    /**
     * Saboteur tudja meghívni, ezáltal kilyukasztva a csövet
     * @return igaz, ha nem volt eleve törött a cső
     */
    @Override
    public boolean Pierced() {
        if (broken) {
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is already broken", Controller.filetoWrite != null);
            return false;
        }
        if(readyToPierce) {
            broken = true;
        }
        else
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is not ready to be pierced again", Controller.filetoWrite != null);
        return broken;
    }

    /**
     * A WaterNode-ok tudják meghívni, ezzel megnövelve a
     * csőben lévő víz mennyiségét amennyiben lehetséges. Azzal a mennyiséggel tér vissza,
     * amit tudott növelni, ha nem tudott növelni akkor ez nulla. Ha a cső lyukas vagy fel van
     * emelve, akkor a víz kifolyik és növeli a Saboteur csapat pontjait.
     * @param amount beérkező víz mennyiség
     * @return amennyivel ténylegesen nőtt a cső tartalma
     */
    public int GainWater(int amount) {

        if (broken || beingHeld) {
            counter.AddSaboteurPoints(amount);
            return 0;
        }
        else {
            int gained = heldWater + amount <= 1 ? amount : 1 - heldWater;
            heldWater += gained;
            return gained;
        }

    }

    /**
     * A WaterNode-ok tudják meghívni, ezzel lecsökkentve a
     * csőben lévő víz mennyiségét amennyiben lehetséges. Azzal a mennyiséggel tér vissza,
     * amit tudott csökkenteni, ha már nem volt benne víz akkor ez nulla.
     * @param amount csökkenteni kívánt mennyiség
     * @return ténylegesen vesztett vízmennyiség
     */
    public int LoseWater(int amount) {
        int lost = Math.min(heldWater, amount);
        heldWater -= lost;
        return lost;

    }

    /**
     * Végpont hozzáadása a csőhöz
     * @param w hozzáadandó elem
     * @return igaz, ha sikeres a hozzáadás
     */
    public boolean AddWaterNode(WaterNode w) {
        if (nodes.size() > 1) {
            return false;
        }
        nodes.add(w);
        return true;

    }

    /**
     * Végpont lecsatolása a csőről
     * @param w lecsatolandó elem
     */
    public void RemoveWaterNode(WaterNode w) {
        nodes.remove(w);
    }

    /**
     * Ragasztáskor lefutó függvény, ami megmondja hogy lehetett-e ragasztani
     * @return sikeresség
     */
    public boolean Glued(){
        if(glued){
            return false;
        }
        else{
            glued = true;
            return true;
        }
    }
    /**
     * Csúszósításkor lefutó függvény, ami megmondja hogy lehetett-e csúszósítani
     * @return sikeresség
     */
    public boolean Lubricated(){
        if(lubricated){
            return false;
        }
        else{
            lubricated = true;
            return true;
        }
    }

    /**
     * Tud-e innen mozogni
     * @return tud()mindig
     */
    @Override
    public boolean canMoveFromHere() {
        return true;
    }

    /**
     * Javításkor lefutó függvény
     * @return sikeres-e a javítás
     */
    @Override
    public boolean Repaired(){
        if(broken){
            broken = false;
            readyToPierce = false;
            Random random = new Random();
            readyToPierceTimer = random.nextInt(4, 9);
            return true;
        } else {
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(this) + " is not broken", Controller.filetoWrite != null);
            return false;
        }
    }

    /**
     * Tud-e mozogni innen a játékos
     * @param p - a játékos
     * @return tud-e
     */
    @Override
    public boolean canMoveToHere(Player p){
        return players.isEmpty() && nodes.contains(p.standingOn);
    }

    /**
     * Lehet-e csúszóssá tenni a csövet
     * @param p - a játékos aki csúszóssá tenné
     * @return lehet-e
     */
    @Override
    public boolean canLubricate(Player p){
        return p.standingOn==this && !lubricated;
    }
    /**
     * Ki lehet-e lyukasztani a csövet
     * @param p - a játékos aki kilyukasztaná
     * @return lehet-e
     */
    @Override
    public boolean canPierce(Player p){
        return p.standingOn==this && !broken && readyToPierce;
    }
    /**
     * Meg lehet-e javítani a csövet
     * @param p - a játékos aki megjavítaná
     * @return lehet-e
     */
    @Override
    public boolean canRepair(Player p){
        return p.standingOn==this && broken;
    }
    /**
     * Lehet-e csúszóssá tenni a csövet
     * @param p - a játékos aki csúszóssá tenné
     * @return lehet-e
     */
    @Override
    public boolean canGlue(Player p){
        return p.standingOn==this && !glued;
    }
    /**
     * Lehet-e felemelni a csövet
     * @param m - a szerelő aki felemelné
     * @return lehet-e
     */
    @Override
    public boolean canPickUpPipe(Mechanic m){
        return nodes.contains(m.standingOn) && m.getHeldItems()==null;
    }

    /**
     * Lehet-e lerakni erre azt amit tart a szerelő
     * @param m - a szerelő aki lerakná
     * @return lehet-e
     */
    @Override
    public boolean canPlaceDown(Mechanic m){
        if (m.getHeldItems() == null)
            return false;
        return m.standingOn==this && m.getHeldItems().canBePlacedDownTo(this);
    }

    /**
     * Elemet fel lehet-e venni onnan amit átadunk
     * @param to - ahonnan felvesszük
     * @return fel lehet-e venni
     */
    @Override
    public boolean canBePlacedDownTo(Steppable to){
        return to.canBePlacedDown(this);
    }

    /**
     * Csövet csőre nem lehet rakni
     * @param p - a cső amit leraknánk
     * @return nem lehet
     */
    @Override
    public boolean canBePlacedDown(Pipe p){
        return false;
    }

    /**
     * Pumpát csőre le lehet rakni
     * @param p - a pumpa amit leraknánk
     * @return lehet
     */
    @Override
    public boolean canBePlacedDown(Pump p){
        return true;
    }


    //GETTEREK-SETTEREK

    public int getWaterCapacity(){
        return waterCapacity;
    }
    public void setWaterCapacity(int w){
         waterCapacity=w;
    }

    public int getReadyToPierceTimer() {
        return readyToPierceTimer;
    }

    public void setReadyToPierceTimer(int readyToPierceTimer) {
        this.readyToPierceTimer = readyToPierceTimer;
    }

    public int getHeldWater() {
        return heldWater;
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean isReadyToPierce() {
        return readyToPierce;
    }

    public boolean isLubricated() {
        return lubricated;
    }

    public boolean isGlued() {
        return glued;
    }

    public boolean isBeingHeld() {
        return beingHeld;
    }

    public LinkedList<WaterNode> getNodes() {
        return nodes;
    }

    public void setHeldWater(int heldWater) {
        this.heldWater = heldWater;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public void setReadyToPierce(boolean readyToPierce) {
        this.readyToPierce = readyToPierce;
    }

    public void setLubricated(boolean lubricated) {
        this.lubricated = lubricated;
    }

    public void setGlued(boolean glued) {
        this.glued = glued;
    }

    public void setBeingHeld(boolean beingHeld) {
        this.beingHeld = beingHeld;
    }
}
