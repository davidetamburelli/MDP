package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;

public class MeleeChaseStrategy implements BehaviorStrategy{

    @Override
    public void act(Enemy self, Player target, MovementService movement) {
        if (BehaviorStrategy.distance(self.getPosition(), target.getPosition()) <= 1) {
            self.attack(target);
        } else {
            movement.move(self, BehaviorStrategy.stepToward(self.getPosition(), target.getPosition()));
        }
    }
}
