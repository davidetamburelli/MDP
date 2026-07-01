package it.unicam.cs.mpgc.rpg125681.model.game;

import it.unicam.cs.mpgc.rpg125681.model.entity.EnemyType;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class KillLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<EnemyType, Integer> counts = new EnumMap<>(EnemyType.class);

    public void record(EnemyType type) {
        Objects.requireNonNull(type, "type");
        counts.merge(type, 1, Integer::sum);
    }

    public int count(EnemyType type) {
        return counts.getOrDefault(type, 0);
    }

    public int total() {
        return counts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<EnemyType, Integer> asMap() {
        return Collections.unmodifiableMap(counts);
    }
}
