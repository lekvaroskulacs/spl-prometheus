package Model;

import java.io.Serializable;
/**
 * Enum osztály, az akciókezelés számára fontos, melyik játékosnak milyen az állapota az akciók szempontjából.
 */
public enum PlayerActionState implements Serializable {
    moveAction,
    specialAction,
    turnOver,
}
