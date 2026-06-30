package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.game.RunOutcome;
import it.unicam.cs.mpgc.rpg125681.model.game.RunRecord;

import java.util.List;

public interface LeaderboardRepository {

    void add(RunRecord record);

    List<RunRecord> findAll();

    List<RunRecord> findByOutcome(RunOutcome outcome);
}