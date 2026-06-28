package it.unicam.cs.mpgc.rpg125681.persistence;

import java.util.List;

public interface GameRepository {

    void save(String slot, SaveGame data);
    SaveGame load(String slot);
    List<String> listSlots();
    boolean exists(String slot);
    void delete(String slot);

}
