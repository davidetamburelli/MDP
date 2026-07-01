package it.unicam.cs.mpgc.rpg125681.persistence;

import it.unicam.cs.mpgc.rpg125681.model.entity.Player;
import it.unicam.cs.mpgc.rpg125681.model.game.GamePhase;

import java.io.Serializable;

public record SessionState(GamePhase phase, int depth, Player profile, SaveGame dungeon) implements Serializable {

    private static final long serialVersionUID = 1L;

}