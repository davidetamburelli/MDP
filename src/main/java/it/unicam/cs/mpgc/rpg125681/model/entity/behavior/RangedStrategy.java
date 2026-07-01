package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

public class RangedStrategy extends ApproachAndAttackStrategy {

    private static final long serialVersionUID = 1L;

    private final int range;

    public RangedStrategy(int range) {
        if (range < 1) throw new IllegalArgumentException("Attack range must be greater than 0.");
        this.range = range;
    }

    @Override
    protected int attackRange() {
        return this.range;
    }
}