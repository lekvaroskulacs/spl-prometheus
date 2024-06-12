package View;

import Controller.Controller;
import Model.ActionType;

/**
 * CreatePopUpBar - popup bar létrehozásáért felelős interfész
 */
public interface CreatePopUpBar {

    /**
     * createPopUpBar - popup bar létrehozása
     * @param x - x koordináta
     * @param y - y koordináta
     * @param r - sugár
     * @param view - ablak
     * @param creator - létrehozó
     */
    public default void createPopUpBar(int x, int y, int r, Window view, SteppableView creator){

        GameView gameView = (GameView) view;
        PopUpBar p = new PopUpBar(x, y, r, view, creator, null);
        gameView.addPopupBar(p);

        boolean notNull = false;
        for (ActionType a : Controller.getInstance().getActivePlayer().availableActions(creator.getCorrespondingModelSteppable())) {
            if (a != null)
                notNull = true;
        }
        if (!notNull)
            gameView.removePopUpBar();

    }

    /**
     * createPopUpBarWithActions - popup bar létrehozása akciókkal
     * @param x - x koordináta
     * @param y - y koordináta
     * @param r - sugár
     * @param view  - ablak
     * @param creator - létrehozó
     * @param at - akciók
     */
    public default void createPopUpBarWithActions(int x, int y, int r, Window view, SteppableView creator, ActionType[] at) {
        GameView gameView = (GameView) view;
        PopUpBar p = new PopUpBar(x, y, r, view, creator, at);
        gameView.addCisternDisplay(p);
    }

}
