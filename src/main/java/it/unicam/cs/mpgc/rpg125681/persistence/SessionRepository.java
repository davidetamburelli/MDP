package it.unicam.cs.mpgc.rpg125681.persistence;

import java.util.List;

public interface SessionRepository {

    void save(String slot, SessionState state);
    SessionState load(String slot);
    List<String> listSlots();
    boolean exists(String slot);
    void delete(String slot);
}