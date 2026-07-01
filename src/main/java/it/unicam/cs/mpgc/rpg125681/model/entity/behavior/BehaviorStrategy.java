package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.model.world.Direction;

public interface BehaviorStrategy {

    void act(Enemy self, Player target, MovementService movement);

    static int distance(Position a, Position b){
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    static Direction stepToward(Position from, Position to){
        int dxGap = to.getX() - from.getX();
        int dyGap = to.getY() - from.getY();
        if (Math.abs(dxGap) > Math.abs(dyGap)) {
            return dxGap > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        return dyGap > 0 ? Direction.DOWN : Direction.UP;
    }

    static Direction stepAway(Position from, Position to) {
        int dxGap = to.getX() - from.getX();
        int dyGap = to.getY() - from.getY();
        if (Math.abs(dxGap) > Math.abs(dyGap)) {
            return dxGap > 0 ? Direction.LEFT : Direction.RIGHT;
        }
        return dyGap > 0 ? Direction.UP : Direction.DOWN;
    }
}
