package it.unicam.cs.mpgc.rpg125681.model.entity.behavior;

public class MeleeChaseStrategy extends ApproachAndAttackStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    protected int attackRange() {
        return 1;
    }
}