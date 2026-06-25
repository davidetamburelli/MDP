package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    void sameIdMeansEqual() {
        Player a = new Warrior(new Position(0, 0), 1);
        Player b = new Warrior(new Position(1, 1), 1);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentIdMeansDifferent() {
        Player a = new Warrior(new Position(0, 0), 1);
        Player b = new Warrior(new Position(1, 1), 2);
        assertNotEquals(a, b);
    }

    @Test
    void equalityIsByIdAcrossTypes() {
        Player a = new Warrior(new Position(0, 0), 1);
        Player b = new Rogue(new Position(1, 1), 1);
        assertEquals(a, b);
    }
}
