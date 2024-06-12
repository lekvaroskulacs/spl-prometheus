package Model;

import Controller.Controller;

import java.io.Serializable;
import java.util.List;

/**
 * A Saboteur osztály valósítja meg a szabotőrrel kapcsolatos metódusokat
 */
public class Saboteur extends Player implements Serializable {

    /**
     * Csúszóssá teszi azt amin áll, ha tudja, meghívja azon pályaelem Lubricated függvényét.
     * @return sikeresség
     */
    public boolean Lubricate(){
        boolean lubricated=false;
        if(isIgnoreStates()) {
            lubricated = standingOn.Lubricated();
        }
        else if(state == PlayerActionState.specialAction) {
            lubricated = standingOn.Lubricated();
            if (lubricated) {
                state = PlayerActionState.turnOver;
                Controller.getInstance().turnOver();
            }
        }
        return lubricated;

    }

    /**
     * A szabotőr elérhető akcióit adja vissza arra az elemre amit átadunk neki
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
            //0
            if(step.canLubricate(this))
                actions[0] = ActionType.lubricate;
            //1
            if(step.canRedirect(this))
                actions[1] = ActionType.redirect;
            //3
            if(step.canGlue(this))
                actions[3] = ActionType.glue;
            //5
            if (step.canPierce(this))
                actions[5] = ActionType.pierce;
            return actions;
        }
        return actions;
    }


}
