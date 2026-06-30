package it.unicam.cs.mpgc.rpg125681.model.item;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final ItemType type;

    public Item(ItemType type) {
        this.id = UUID.randomUUID().toString();
        this.type = Objects.requireNonNull(type, "type");
    }

    public ItemType getType() { return this.type; }
    public String getId() { return this.id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}