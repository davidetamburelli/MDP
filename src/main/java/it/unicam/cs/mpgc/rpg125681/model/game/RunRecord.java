package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public record RunRecord(
        RunOutcome outcome,
        int maxDepth,
        int totalKills,
        Map<EnemyType, Integer> killsBySpecies,
        int level,
        int exp,
        long timestamp) {

    public static RunRecord of(RunOutcome outcome, int maxDepth, GameWorld world, long timestamp) {
        Map<EnemyType, Integer> kills = new EnumMap<>(EnemyType.class);
        kills.putAll(world.getKillLog().asMap());
        return new RunRecord(
                outcome,
                maxDepth,
                world.getKillLog().total(),
                Collections.unmodifiableMap(kills),
                world.getPlayer().getLevel(),
                world.getPlayer().getExp(),
                timestamp);
    }
}