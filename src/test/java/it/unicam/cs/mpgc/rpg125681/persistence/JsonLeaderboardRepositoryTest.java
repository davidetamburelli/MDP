package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonLeaderboardRepositoryTest {

    @TempDir
    Path tempDir;

    private LeaderboardRepository repo() {
        return new JsonLeaderboardRepository(tempDir.resolve("leaderboard.json"));
    }

    private RunRecord run(RunOutcome outcome, int depth, long timestamp) {
        return new RunRecord(outcome, depth, 3,
                Map.of(EnemyType.GOBLIN, 2, EnemyType.SKELETON, 1),
                4, 12, timestamp);
    }

    @Test
    void findAllOnMissingFileReturnsEmpty() {
        assertTrue(repo().findAll().isEmpty());
    }

    @Test
    void addPersistsAndReloadsRecord() {
        LeaderboardRepository repo = repo();
        repo.add(run(RunOutcome.VICTORY, 5, 1000L));

        List<RunRecord> all = repo().findAll();
        assertEquals(1, all.size());
        RunRecord loaded = all.get(0);
        assertEquals(RunOutcome.VICTORY, loaded.outcome());
        assertEquals(5, loaded.maxDepth());
        assertEquals(3, loaded.totalKills());
        assertEquals(2, loaded.killsBySpecies().get(EnemyType.GOBLIN));
    }

    @Test
    void findAllReturnsMostRecentFirst() {
        LeaderboardRepository repo = repo();
        repo.add(run(RunOutcome.VICTORY, 5, 1000L));
        repo.add(run(RunOutcome.DEFEAT, 2, 3000L));
        repo.add(run(RunOutcome.VICTORY, 3, 2000L));

        List<Long> order = repo.findAll().stream().map(RunRecord::timestamp).toList();
        assertEquals(List.of(3000L, 2000L, 1000L), order);
    }

    @Test
    void findByOutcomeFiltersAndKeepsOrder() {
        LeaderboardRepository repo = repo();
        repo.add(run(RunOutcome.VICTORY, 5, 1000L));
        repo.add(run(RunOutcome.DEFEAT, 2, 3000L));
        repo.add(run(RunOutcome.VICTORY, 3, 2000L));

        List<RunRecord> victories = repo.findByOutcome(RunOutcome.VICTORY);
        assertEquals(2, victories.size());
        assertEquals(List.of(2000L, 1000L),
                victories.stream().map(RunRecord::timestamp).toList());
    }

    @Test
    void addAppendsToExistingFile() {
        LeaderboardRepository repo = repo();
        repo.add(run(RunOutcome.VICTORY, 5, 1000L));
        repo.add(run(RunOutcome.DEFEAT, 2, 2000L));
        assertEquals(2, repo.findAll().size());
    }
}