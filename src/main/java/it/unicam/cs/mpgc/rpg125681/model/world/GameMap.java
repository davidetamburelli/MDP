package it.unicam.cs.mpgc.rpg125681.model.world;

import java.util.List;
import java.util.Objects;

public final class GameMap {

    private final TileType[][] tiles;
    private final int width;
    private final int height;

    public GameMap(TileType[][] tiles) {
        Objects.requireNonNull(tiles, "tiles");
        if (tiles.length == 0 || tiles[0].length == 0) {
            throw new  IllegalArgumentException("Map can't be empty");
        }
        this.height = tiles.length;
        this.width = tiles[0].length;
        this.tiles = new TileType[height][width];
        for (int y = 0; y < height; y++) {
            if (tiles[y].length != width) {
                throw new IllegalArgumentException("Lines of uneven length");
            }
            System.arraycopy(tiles[y], 0, this.tiles[y], 0, width);
        }
    }

    public static GameMap fromRows(List<String> rows) {
        Objects.requireNonNull(rows, "rows");
        if (rows.isEmpty()) {
            throw new  IllegalArgumentException("Map can't be empty");
        }
        int height = rows.size();
        int width  = rows.get(0).length();
        TileType[][] tiles = new TileType[height][width];
        for (int y = 0; y < height; y++) {
            String row = rows.get(y);
            if (row.length() != width) {
                throw new IllegalArgumentException("Lines of uneven length");
            }
            for (int x = 0; x < width; x++) {
                tiles[y][x] = TileType.fromSymbol(row.charAt(x));
            }
        }
        return new GameMap(tiles);
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    public boolean contains(Position position) {
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }

    public TileType tileAt(Position position) {
        if (!(contains(position))) {
            throw new IndexOutOfBoundsException("Position " + position + " is not in the map");
        }
        return this.tiles[position.getY()][position.getX()];
    }

    public boolean isWalkable(Position position) {
        return contains(position) && tileAt(position).isWalkable();
    }
}
