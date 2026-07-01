package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;

import java.io.Serializable;

public class KiterStrategy implements BehaviorStrategy, Serializable {

    private static final long serialVersionUID = 1L;

    private final int range;

    public KiterStrategy(int range) {
        if (range < 1) throw new IllegalArgumentException("Attack range must be greater than 0.");
        this.range = range;
    }

    @Override
    public void act(Enemy self, Player target, MovementService movement) {
        int distance = BehaviorStrategy.distance(self.getPosition(), target.getPosition());
        if (distance <= 1) {
            movement.move(self, BehaviorStrategy.stepAway(self.getPosition(), target.getPosition()));
        } else if (distance <= range) {
            self.attack(target);
        } else {
            movement.move(self, BehaviorStrategy.stepToward(self.getPosition(), target.getPosition()));
        }
    }
}