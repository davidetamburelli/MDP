package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;

public class RangedStrategy implements BehaviorStrategy {
    private final int attackRange;

    public RangedStrategy(int attackRange) {
        if (attackRange < 1) throw new IllegalArgumentException("Attack range must be greater than 0.");
        this.attackRange = attackRange;
    }

    @Override
    public void act(Enemy self, Player target) {
        if (BehaviorStrategy.distance(self.getPosition(), target.getPosition()) <= attackRange) {
            self.attack(target);
        } else {
            self.moveTo(BehaviorStrategy.stepToward(self.getPosition(), target.getPosition()));
        }
    }
}
