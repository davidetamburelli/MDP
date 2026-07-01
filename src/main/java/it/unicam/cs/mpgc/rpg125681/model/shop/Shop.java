package it.unicam.cs.mpgc.rpg125681.model.shop;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.item.Item;
import it.unicam.cs.mpgc.rpg125681.model.item.ItemType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Shop {

    private static final int BASE_PRICE_CEILING = 70;
    private static final int CEILING_PER_PROGRESS = 70;

    public List<ItemType> getCatalog(Player player, int progress) {
        Objects.requireNonNull(player, "player");
        int ceiling = BASE_PRICE_CEILING + Math.max(0, progress) * CEILING_PER_PROGRESS;
        return Arrays.stream(ItemType.values())
                .filter(type -> type.getAllowedClasses().contains(player.getPlayerClass()))
                .filter(type -> type.getBasePrice() <= ceiling)
                .sorted(Comparator.comparingInt(ItemType::getBasePrice))
                .toList();
    }

    public void buy(Player player, ItemType type, int progress) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(type, "type");
        if (!getCatalog(player, progress).contains(type)) {
            throw new IllegalArgumentException("Item not available in the shop: " + type);
        }
        player.spendGold(type.getBasePrice());
        player.getInventory().add(new Item(type));
    }
}