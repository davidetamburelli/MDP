package it.unicam.cs.mpgc.rpg125681.model.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Item> items = new ArrayList<>();
    private final Map<ItemCategory, Item> equipped = new EnumMap<>(ItemCategory.class);

    public void add(Item item) {
        items.add(Objects.requireNonNull(item, "item"));
    }

    public void remove(Item item) {
        items.remove(item);
        equipped.values().removeIf(equippedItem -> equippedItem.equals(item));
    }

    public void equip(Item item) {
        Objects.requireNonNull(item, "item");
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not in inventory.");
        }
        ItemCategory category = item.getType().getCategory();
        if (!category.isEquippable()) {
            throw new IllegalArgumentException("Category not equippable: " + category);
        }
        equipped.put(category, item);
    }

    public Item getEquipped(ItemCategory category) {
        return equipped.get(category);
    }

    public Item getEquippedWeapon() { return equipped.get(ItemCategory.WEAPON); }
    public Item getEquippedArmor() { return equipped.get(ItemCategory.ARMOR); }

    public List<Item> getItems() { return Collections.unmodifiableList(this.items); }
}