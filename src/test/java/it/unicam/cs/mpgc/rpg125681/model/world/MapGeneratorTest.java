package it.unicam.cs.mpgc.rpg125681.model.world;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MapGeneratorTest {

    private GeneratedLevel generate(long seed) {
        return new MapGenerator(new Random(seed)).generate(15, 11, 5);
    }

    private Set<Position> reachableFrom(GameMap map, Position start) {
        Set<Position> seen = new HashSet<>();
        Deque<Position> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);
        int[][] deltas = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            Position p = queue.poll();
            for (int[] d : deltas) {
                Position next = new Position(p.getX() + d[0], p.getY() + d[1]);
                if (map.isWalkable(next) && !seen.contains(next)) {
                    seen.add(next);
                    queue.add(next);
                }
            }
        }
        return seen;
    }

    private boolean sameLayout(GameMap a, GameMap b) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) {
            return false;
        }
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                Position p = new Position(x, y);
                if (a.isWalkable(p) != b.isWalkable(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    void allRoomsAreReachable() {
        for (long seed = 1; seed <= 10; seed++) {
            GeneratedLevel level = generate(seed);
            Set<Position> reachable = reachableFrom(level.map(), level.rooms().get(0));
            for (Position room : level.rooms()) {
                assertTrue(reachable.contains(room),
                        "Unreachable room with seed " + seed + ": " + room.getX() + "," + room.getY());
            }
        }
    }

    @Test
    void sameSeedProducesIdenticalMap() {
        GeneratedLevel a = generate(99);
        GeneratedLevel b = generate(99);
        assertTrue(sameLayout(a.map(), b.map()));
        assertEquals(a.rooms(), b.rooms());
    }

    @Test
    void returnsRequestedNumberOfRooms() {
        GeneratedLevel level = generate(1);
        assertEquals(5, level.rooms().size());
    }

    @Test
    void roomCentersAreOnFloor() {
        GeneratedLevel level = generate(3);
        for (Position room : level.rooms()) {
            assertTrue(level.map().isWalkable(room));
        }
    }

    @Test
    void borderIsAllWalls() {
        GameMap map = generate(3).map();
        for (int x = 0; x < map.getWidth(); x++) {
            assertFalse(map.isWalkable(new Position(x, 0)));
            assertFalse(map.isWalkable(new Position(x, map.getHeight() - 1)));
        }
        for (int y = 0; y < map.getHeight(); y++) {
            assertFalse(map.isWalkable(new Position(0, y)));
            assertFalse(map.isWalkable(new Position(map.getWidth() - 1, y)));
        }
    }

    @Test
    void mapHasRequestedDimensions() {
        GameMap map = generate(1).map();
        assertEquals(15, map.getWidth());
        assertEquals(11, map.getHeight());
    }

    @Test
    void tooSmallDimensionsThrow() {
        MapGenerator generator = new MapGenerator(new Random());
        assertThrows(IllegalArgumentException.class, () -> generator.generate(3, 3, 5));
    }

    @Test
    void nonPositiveRoomCountThrows() {
        MapGenerator generator = new MapGenerator(new Random());
        assertThrows(IllegalArgumentException.class, () -> generator.generate(15, 11, 0));
    }

    @Test
    void nullRandomThrows() {
        assertThrows(NullPointerException.class, () -> new MapGenerator(null));
    }
}