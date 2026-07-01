package it.unicam.cs.mpgc.rpg125681.model.entity;

import java.io.Serializable;

public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    private int amount;

    public void add(int value) {
        if (value < 0) throw new IllegalArgumentException("Gold to add must be non-negative.");
        this.amount += value;
    }

    public void spend(int value) {
        if (value < 0) throw new IllegalArgumentException("Gold to spend must be non-negative.");
        if (value > this.amount) throw new IllegalArgumentException("Not enough gold.");
        this.amount -= value;
    }

    public int getAmount() { return this.amount; }
}