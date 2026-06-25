package it.unicam.cs.mpgc.rpg125681.model.world;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void sameCoordinatesAreEqual() {
        Position a = new Position(1, 2);
        Position b = new Position(1, 2);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentCoordinatesAreNotEqual() {
        Position a = new Position(1, 2);
        Position b = new Position(0, 3);
        assertNotEquals(a, b);
    }

    @Test
    void notEqualToDifferentType() {
        Position a = new Position(1, 2);
        assertNotEquals(a, "Different Type");
    }
}
