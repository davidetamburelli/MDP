package it.unicam.cs.mpgc.rpg125681.model.item;

import it.unicam.cs.mpgc.rpg125681.model.entity.PlayerClass;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public enum ItemType {

    POTION_MINOR("Minor Potion", ItemCategory.POTION, 20, false, 10, true, EnumSet.allOf(PlayerClass.class)),
    POTION_NORMAL("Normal Potion", ItemCategory.POTION, 50, false, 25, true, EnumSet.allOf(PlayerClass.class)),
    POTION_MAJOR("Major Potion", ItemCategory.POTION, 100, false, 50, true, EnumSet.allOf(PlayerClass.class)),

    POTION_PERCENT_LESSER("Lesser Percent Potion", ItemCategory.POTION, 15, true, 30, true, EnumSet.allOf(PlayerClass.class)),
    POTION_PERCENT_HALF("Half Percent Potion", ItemCategory.POTION, 50, true, 80, true, EnumSet.allOf(PlayerClass.class)),
    POTION_PERCENT_MAX("Max Percent Potion", ItemCategory.POTION, 100, true, 200, true, EnumSet.allOf(PlayerClass.class)),

    SWORD_IRON("Iron Sword", ItemCategory.WEAPON, 5, false, 50, true, EnumSet.of(PlayerClass.WARRIOR)),
    SWORD_SILVER("Silver Sword", ItemCategory.WEAPON, 10, false, 120, false, EnumSet.of(PlayerClass.WARRIOR)),
    SWORD_GOLD("Gold Sword", ItemCategory.WEAPON, 18, false, 300, false, EnumSet.of(PlayerClass.WARRIOR)),

    DAGGER_IRON("Iron Dagger", ItemCategory.WEAPON, 4, false, 45, true, EnumSet.of(PlayerClass.ROGUE)),
    DAGGER_SILVER("Silver Dagger", ItemCategory.WEAPON, 9, false, 110, false, EnumSet.of(PlayerClass.ROGUE)),
    DAGGER_GOLD("Gold Dagger", ItemCategory.WEAPON, 16, false, 280, false, EnumSet.of(PlayerClass.ROGUE)),

    ORB_GLASS("Glass Orb", ItemCategory.WEAPON, 6, false, 60, true, EnumSet.of(PlayerClass.SORCERER)),
    ORB_CRYSTAL("Crystal Orb", ItemCategory.WEAPON, 12, false, 140, false, EnumSet.of(PlayerClass.SORCERER)),
    ORB_ETHEREAL("Ethereal Orb", ItemCategory.WEAPON, 20, false, 350, false, EnumSet.of(PlayerClass.SORCERER)),

    ARMOR_LEATHER("Leather Armor", ItemCategory.ARMOR, 5, false, 60, true, EnumSet.allOf(PlayerClass.class)),
    ARMOR_PLATE("Plate Armor", ItemCategory.ARMOR, 12, false, 150, true, EnumSet.allOf(PlayerClass.class)),
    ARMOR_HARDENED_PLATE("Hardened Plate Armor", ItemCategory.ARMOR, 20, false, 300, false, EnumSet.of(PlayerClass.WARRIOR)),
    ARMOR_DUST("Dust Armor", ItemCategory.ARMOR, 10, false, 200, false, EnumSet.of(PlayerClass.ROGUE)),
    ARMOR_PERCEPTION("Perception Armor", ItemCategory.ARMOR, 8, false, 220, false, EnumSet.of(PlayerClass.SORCERER));

    private final String displayName;
    private final ItemCategory category;
    private final int effectValue;
    private final boolean percentage;
    private final int basePrice;
    private final boolean droppable;
    private final Set<PlayerClass> allowedClasses;

    ItemType(String displayName, ItemCategory category, int effectValue, boolean percentage, int basePrice, boolean droppable, Set<PlayerClass> allowedClasses) {
        this.displayName = displayName;
        this.category = category;
        this.effectValue = effectValue;
        this.percentage = percentage;
        this.basePrice = basePrice;
        this.droppable = droppable;
        this.allowedClasses = allowedClasses;
    }

    public String getDisplayName() { return displayName; }
    public ItemCategory getCategory() { return category; }
    public int getEffectValue() { return effectValue; }
    public boolean isPercentage() { return percentage; }
    public int getBasePrice() { return basePrice; }
    public boolean isDroppable() { return droppable; }
    public Set<PlayerClass> getAllowedClasses() { return allowedClasses; }

    public static ItemType getRandomDrop(Random random) {
        List<ItemType> droppableItems = Arrays.stream(values())
                .filter(ItemType::isDroppable)
                .collect(Collectors.toList());
        if (droppableItems.isEmpty()) {
            return null;
        }
        return droppableItems.get(random.nextInt(droppableItems.size()));
    }
}