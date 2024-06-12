package Model;

import Controller.Controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Játékosok funkcióinak megvalósítása
 */
public abstract class Player implements Serializable {
    /**
     * Le van-e esve a játékos a pályáról
     */
    protected boolean fellDown = false;
    /**
     * ragacsos-e a játékos
     */
    protected boolean stuck = false;
    /**
     * mennyi ideig ragad a játékos
     */
    protected int glueLength = 0;
    /**
     * A leesett játékosok listája
     */
    public static List<Player> fallen = new LinkedList<>();
    /**
     * Le lett-e szedve a játékos a pályáról(CutInHalf-hoz)
     */
    protected boolean removed=true;


    /**
     * Az aktuális elem, amin áll a játékos
     */
    protected Steppable standingOn;
    /**
     * A játékos státusza(nem az ő köre/mozog/akciók)
     */
    PlayerActionState state =PlayerActionState.turnOver;
    /**
     * flag, hogy az actionstate-ket figyelembe kell-e venni.
     */
    private static boolean ignoreStates = true;

    /**
     * Mozgás egyik elemről a másik, szomszédosra, figyelembe véve az ignoreStates flaget, és azt, hogy a játékos action-t végezhet
     * @param to amire szeretni lépni
     */
    public boolean Move(Steppable to) {
        removed=false;

        if(!ignoreStates && state==PlayerActionState.turnOver)
            return false;

        if (fellDown) {
            return false;
        }

        if (stuck) {
            IO_Manager.writeInfo("Can't move to " + Controller.getInstance().getObjectName(to) +
                    " because " + Controller.getInstance().getObjectName(this) + " is stuck", Controller.filetoWrite != null);
            return false;
        }

        if (to.PlayerEnter(this)) {

            if (standingOn != null) {
                if (!ignoreStates && state == PlayerActionState.moveAction)
                    state = PlayerActionState.specialAction;
                else if (!ignoreStates && state == PlayerActionState.specialAction) {
                    state = PlayerActionState.turnOver;
                    Controller.getInstance().turnOver();
                }
                standingOn.PlayerExit(this);
            }

            standingOn = to;

            return true;
        }

        return false;

    }

    /**
     * Játékos eltávolítása a pályáról(CutInHalf-hez is)
     */
    public void RemovePlayer(){
        if(standingOn != null){
            standingOn.PlayerExit(this);
        }
        standingOn = null;
        removed=true;
    }

    /**
     * Pumpa beállítása, meghívja a Pump PlayerRedirect függvényét.
     * @param in bemeneti cső
     * @param out kimeneti  cső
     */
    public boolean Redirect(Pipe in, Pipe out) {
        if (state != PlayerActionState.specialAction)
            return false;

        if (standingOn.PlayerRedirect(in, out)) {
            state = PlayerActionState.turnOver;
            Controller.getInstance().turnOver();
            return true;
        }
        return false;
    }


    /**
     * Kilyukasztja (amennyiben nem lyukas, és lyukasztható) azt az elemet, amin jelenleg a szabotőr áll
     */
    public boolean Pierce() {
        boolean pierced=false;
        if(ignoreStates) {
            pierced = standingOn.Pierced();
        }
        else if(state == PlayerActionState.specialAction) {
            pierced = standingOn.Pierced();
            if (pierced) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        return pierced;
    }

    /**
     * Ragacsossá teszi (amennyiben nem ragacsos) azt az elemet, amin jelenleg a szabotőr áll
     */
    public boolean Glue(){
        boolean glued = false;
        if(ignoreStates) {
            glued = standingOn.Glued();
        }
        else if(state == PlayerActionState.specialAction) {
            glued = standingOn.Glued();
            if (glued) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        return glued;
    }

    /**
     * Abstrakt függvény, ami a lehetséges akciókat adja vissza amit a játékos csinálhat az adott játékelemmel, leszármazottakban implementált
     * @param step - a játékelem
     * @return Az akciók tömbje
     */
    public abstract ActionType[] availableActions(Steppable step);


    //GETTEREK-SETTEREK

    public void setStuck(boolean b){
        stuck = b;
    }

    //A fellDown értékével tér vissza.
    public boolean isFellDown() {
        return fellDown;
    }

    //A stuck értékével tér vissza.
    public boolean isStuck() {
        return stuck;
    }

    //A játékos akciójának állapotát kérdezi le.
    public PlayerActionState getState() {
        return state;
    }

    //Az ignoreStates értékével tér vissza.
    public static boolean isIgnoreStates() {
        return ignoreStates;
    }

    public static void setIgnoreStates(boolean ignoreStates) {
        Player.ignoreStates = ignoreStates;
    }

    //A stangindgOn értékét állítja be.
    public void setStandingOn(Steppable standingOn) {
        this.standingOn = standingOn;
    }

    //A játékos akcióját állítja be.
    public void setState(PlayerActionState state) {
        this.state = state;
    }

    public int getGlueLength() {
        return glueLength;
    }

    public void setGlueLength(int glueLength) {
        this.glueLength = glueLength;
    }
    public boolean isRemoved() {
        return removed;
    }

    public Steppable getStandingOn() {
        return standingOn;
    }
    public void setFellDown(boolean value) {
        if (value != fellDown)
            if (value) {
                fallen.add(this);
            }
            else
                fallen.remove(this);

        fellDown=value;
    }
}
