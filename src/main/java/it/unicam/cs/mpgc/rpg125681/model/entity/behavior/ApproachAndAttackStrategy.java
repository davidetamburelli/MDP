package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;

import java.io.Serializable;

public abstract class ApproachAndAttackStrategy implements BehaviorStrategy, Serializable {

    private static final long serialVersionUID = 1L;

    protected abstract int attackRange();

    @Override
    public void act(Enemy self, Player target, MovementService movement) {
        if (BehaviorStrategy.distance(self.getPosition(), target.getPosition()) <= attackRange()) {
            self.attack(target);
        } else {
            movement.move(self, BehaviorStrategy.stepToward(self.getPosition(), target.getPosition()));
        }
    }
}