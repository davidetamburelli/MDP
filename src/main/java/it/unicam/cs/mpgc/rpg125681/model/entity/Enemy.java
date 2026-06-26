package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorStrategy;
import it.unicam.cs.mpgc.rpg125681.model.world.Position;
import it.unicam.cs.mpgc.rpg125681.model.movement.MovementService;

import java.util.Objects;

public class Enemy extends LivingEntity{

    private final int attackPower;
    private final int expReward;
    private final BehaviorStrategy behavior;

    public Enemy(Position position, int id, int maxHp, int attackPower, int expReward, BehaviorStrategy behavior) {
        super(position, id, maxHp);
        if (attackPower < 0) throw new IllegalArgumentException("Attack must be greater than 0.");
        if (expReward < 0) throw new IllegalArgumentException("Exp must be greater than 0.");
        this.attackPower = attackPower;
        this.expReward = expReward;
        this.behavior = Objects.requireNonNull(behavior, "behavior");
    }

    public void update(Player target, MovementService movement) {
        this.behavior.act(this, target, movement);
    }

    public void attack(LivingEntity target){
        target.takeDamage(this.attackPower);
    }

    public int getAttackPower() { return this.attackPower; }
    public int getExpReward() { return this.expReward; }
}
