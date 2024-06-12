package Model;

/**
 * Interface azoknak a pályaelemeknek, amelyeket fel lehet venni, illetve le lehet rakni
 */
public interface PickupAble {
    /**
     * Elem felvétele egy másik elemről
     * @param from melyik elemről vesszük le / csatlakoztatjuk le
     */
    void PickedUp(Steppable from);

    /**
     * Elem letétele egy másik elemre, az előző dokumentumhoz képest van visszatérési értéke
     * @param to melyik elemre tesszük le / csatlakoztatjuk rá
     * @return a letétel sikeressége
     */
    boolean PlacedDown(Steppable to);

    /**
     * Elemet fel lehet-e venni onnan amit átadunk
     * @param to - ahonnan felvesszük
     * @return fel lehet-e venni
     */
    boolean canBePlacedDownTo(Steppable to);


}
