package Model;

import Controller.Controller;

import java.io.Serializable;
import java.util.List;

/**
 * Szerelő megvalósítására használt osztály
 */
public class Mechanic extends Player implements Serializable {
    /**
     * felvett pályaelem
     */
    private PickupAble heldItems = null;

    /**
     * Pályaelem megjavítására használt függvény, ha specialAction-ben van a játékos, akkor javít.
     */
    public boolean Repair() {
        boolean repaired=false;
        if(isIgnoreStates()) {
            repaired = standingOn.Repaired();
        }
        else if(state == PlayerActionState.specialAction) {
            repaired = standingOn.Repaired();
            if (repaired) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        if(!repaired)
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(standingOn) + "is not broken", Controller.filetoWrite != null);
        return repaired;
    }

    /**
     * Pályaelem felvételére használt függvény, a felvett pályaelemet a játékosnak beadjuk, és csak akkor tudja felvenni ha mellette áll
     * @param item - felvett pályaelem
     */
    public boolean PickUp(PickupAble item) {
        boolean pickedup=false;
        if(isIgnoreStates()) {
            pickedup = standingOn.PickedUpFrom(item);
        }
        else if(state == PlayerActionState.specialAction) {
            pickedup = standingOn.PickedUpFrom(item);
            if (pickedup) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        if (pickedup) {
            item.PickedUp(standingOn);
            heldItems = item;
        }
        else
            IO_Manager.writeInfo(Controller.getInstance().getObjectName(item) + " can't be picked up", Controller.filetoWrite != null);

        return pickedup;
    }

    /**
     * Pályaelem lerakására használt függvény.
     * Változott az előző dokumentum óta, nem kell paraméter hiszen csak saját attribútumot kezel.
     */
    public boolean PlaceDown() {

        boolean successful = false;
        if(isIgnoreStates()) {
            successful = heldItems.PlacedDown(standingOn);
        }
        else if(state == PlayerActionState.specialAction) {
            successful = heldItems.PlacedDown(standingOn);
            if (successful) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        if (successful)
            heldItems = null;
        return successful;
    }

    /**
     * A szerelő elérhető akcióit adja vissza arra az elemre amit átadunk neki
     * @param step - az elem amire nézzük az akciókat
     * @return - Az akciók tömbje
     */
    public ActionType[] availableActions(Steppable step){
        ActionType[] actions = new ActionType[6];
        if(state.equals(PlayerActionState.moveAction)){
            if(step.canMoveToHere(this)) {
                actions[2] = ActionType.move;
                return actions;
            }
        }
        if(state.equals(PlayerActionState.specialAction)){
            //2
            if (step.canMoveToHere(this))
                actions[2] = ActionType.move;
            else if (!standingOn.canMoveFromHere() || standingOn == step)
                actions[2] = ActionType.pass;
            //1
            if(step.canRedirect(this))
                actions[1] = ActionType.redirect;
            //3
            if(step.canGlue(this))
                actions[3] = ActionType.glue;
            //4
            if(step.canPickUpPipe(this)){
                actions[4] = ActionType.pickupPipe;
            } else if (step.canPlaceDown(this)) {
                actions[4] = ActionType.placedown;
            }
            //5
            if(step.canPickUpPump(this)){
                actions[5] = ActionType.pickupPump;
            } else if (step.canRepair(this)) {
                actions[5] = ActionType.repair;
            } else if (step.canPierce(this)) {
                actions[5] = ActionType.pierce;
            }
            return actions;
        }
        return actions;
    }
    /**
     * Felvett pályaelem gettere
     * @return a pályaelem
     */
    public PickupAble getHeldItems() {
        return heldItems;
    }
}
