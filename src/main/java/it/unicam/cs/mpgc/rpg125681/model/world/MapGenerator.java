package it.unicam.cs.mpgc.rpg125681.model.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MapGenerator {

    private static final int MIN_ROOM = 3;
    private static final int MAX_ROOM = 6;

    private final Random random;

    public MapGenerator(Random random){
        this.random = Objects.requireNonNull(random, "random");
    }

    public GeneratedLevel generate(int width, int height, int roomCount){
        if (width < 5 || height < 5) throw new IllegalArgumentException("Map is too small");
        if (roomCount < 1) throw new IllegalArgumentException("Room count is too small");

        TileType[][] tiles = filledWithWalls(width, height);
        List<Position> centers = new ArrayList<>();

        for (int i = 0; i < roomCount; i++) {
            int randomW = MIN_ROOM + random.nextInt(MAX_ROOM - MIN_ROOM + 1);
            int randomH = MIN_ROOM + random.nextInt(MAX_ROOM - MIN_ROOM + 1);
            int randomX = 1 + random.nextInt(Math.max(1, width - randomW - 1));
            int randomY = 1 + random.nextInt(Math.max(1, height - randomH - 1));
            carveRoom(tiles, randomX, randomY, randomW, randomH);
            Position center = new Position(randomX + randomW / 2, randomY + randomH / 2);
            if (!centers.isEmpty()) {
                carveCorridor(tiles, centers.get(centers.size() - 1), center);
            }
            centers.add(center);
        }
        return new GeneratedLevel(new GameMap(tiles), centers);
    }

    private TileType[][] filledWithWalls(int width, int height){
        TileType[][] tiles = new TileType[height][width];
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                tiles[y][x] = TileType.WALL;
            }
        }
        return tiles;
    }

    private void carveRoom(TileType[][] tiles, int randomX, int randomY, int randomW, int randomH){
        int height = tiles.length, width = tiles[0].length;
        for (int y = randomY; y < randomY + randomH && y < height - 1; y++) {
            for (int x = randomX; x < randomX + randomW && x < width - 1; x++){
                tiles[y][x] = TileType.FLOOR;
            }
        }
    }

    private void carveCorridor(TileType[][] tiles, Position from, Position to){
        int width = tiles[0].length, height = tiles.length;
        int x = from.getX(), y = from.getY();
        while (x != to.getX()) {
            if (x > 0 && x < width - 1) tiles[y][x] = TileType.FLOOR;
            x+= Integer.signum(to.getX() - x);
        }
        while (y != to.getY()) {
            if (y > 0 && y < height - 1) tiles[y][x] = TileType.FLOOR;
            y+= Integer.signum(to.getY() - y);
        }
        if (x > 0 && x < width - 1 && y > 0 && y < height - 1) tiles[y][x] = TileType.FLOOR;
    }
}
