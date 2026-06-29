package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KillLogTest {

    @Test
    void recordIncrementsCountForType() {
        KillLog log = new KillLog();
        log.record(EnemyType.GOBLIN);
        log.record(EnemyType.GOBLIN);
        assertEquals(2, log.count(EnemyType.GOBLIN));
    }

    @Test
    void countOfUnrecordedTypeIsZero() {
        KillLog log = new KillLog();
        assertEquals(0, log.count(EnemyType.MAGE));
    }

    @Test
    void totalSumsAcrossTypes() {
        KillLog log = new KillLog();
        log.record(EnemyType.GOBLIN);
        log.record(EnemyType.GOBLIN);
        log.record(EnemyType.SKELETON);
        assertEquals(3, log.total());
    }

    @Test
    void asMapIsUnmodifiable() {
        KillLog log = new KillLog();
        log.record(EnemyType.GOBLIN);
        assertThrows(UnsupportedOperationException.class, () -> log.asMap().put(EnemyType.MAGE, 9));
    }

    @Test
    void recordNullThrows() {
        KillLog log = new KillLog();
        assertThrows(NullPointerException.class, () -> log.record(null));
    }
}