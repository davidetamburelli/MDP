package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

import it.unicam.cs.mpgc.rpg125681.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;

public interface BehaviorStrategy {

    void act(Enemy self, Player target);

    static int distance(Position a, Position b){
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    static Position stepToward(Position from, Position to){
        int dxGap = to.getX() - from.getX();
        int dyGap = to.getY() - from.getY();
        if (Math.abs(dxGap) > Math.abs(dyGap)) {
            return new Position(from.getX() + Integer.signum(dxGap), from.getY());
        }
        return new Position(from.getX(), from.getY() + Integer.signum(dyGap));
    }
}
