package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;

import java.io.Serializable;

public class SentryStrategy implements BehaviorStrategy, Serializable {

    private static final long serialVersionUID = 1L;

    private final int range;

    public SentryStrategy(int range) {
        if (range < 1) throw new IllegalArgumentException("Attack range must be greater than 0.");
        this.range = range;
    }

    @Override
    public void act(Enemy self, Player target, MovementService movement) {
        if (BehaviorStrategy.distance(self.getPosition(), target.getPosition()) <= range) {
            self.attack(target);
        }
    }
}