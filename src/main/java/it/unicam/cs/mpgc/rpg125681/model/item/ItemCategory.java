package it.unicam.cs.mpgc.rpg125681.model.item;

public enum ItemCategory {
    WEAPON(true, false),
    ARMOR(true, false),
    POTION(false, true);

    private final boolean equippable;
    private final boolean consumable;

    ItemCategory(boolean equippable, boolean consumable) {
        this.equippable = equippable;
        this.consumable = consumable;
    }

    public boolean isEquippable() {
        return this.equippable;
    }

    public boolean isConsumable() {
        return this.consumable;
    }

}