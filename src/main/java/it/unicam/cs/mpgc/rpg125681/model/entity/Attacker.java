package it.unicam.cs.mpgc.rpg125681.model.entity;

public interface Attacker {
    int getAttackPower();
    default void attack(LivingEntity target) {
        target.takeDamage(getAttackPower());
    }
}
