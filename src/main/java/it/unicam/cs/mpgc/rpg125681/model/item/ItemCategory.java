package it.unicam.cs.mpgc.rpg125681.model.item;

public enum ItemCategory {
    WEAPON(true),
    ARMOR(true),
    POTION(false);

    private final boolean equippable;

    ItemCategory(boolean equippable) {
        this.equippable = equippable;
    }

    public boolean isEquippable() {
        return this.equippable;
    }
}