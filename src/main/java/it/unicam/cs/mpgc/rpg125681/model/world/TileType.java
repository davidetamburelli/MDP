package it.unicam.cs.mpgc.rpg125681.model.world;

public enum TileType {

    FLOOR(true, '.'),
    WALL(false, '#');

    private final boolean walkable;
    private final char symbol;

    TileType(boolean walkable, char symbol){
        this.walkable = walkable;
        this.symbol = symbol;
    }

    public boolean isWalkable() { return this.walkable; }
    public char getSymbol() { return this.symbol; }

    public static TileType fromSymbol(char symbol) {
        for (TileType type : TileType.values()) {
            if (type.symbol == symbol) return type;
        }
        throw new IllegalArgumentException("Unknown tile symbol: " + symbol);
    }
}
