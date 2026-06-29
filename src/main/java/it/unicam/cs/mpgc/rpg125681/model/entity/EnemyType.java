package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorKind;

public enum EnemyType {

    GOBLIN("Goblin", 18, 6, 10, BehaviorKind.MELEE, 0),
    SKELETON("Skeleton", 28, 8, 14, BehaviorKind.MELEE, 0),
    ARCHER("Archer", 16, 7, 16, BehaviorKind.RANGED, 3),
    MAGE("Mage", 14, 11, 22, BehaviorKind.RANGED, 4);

    private final String displayName;
    private final int baseHp;
    private final int baseAttack;
    private final int baseExp;
    private final BehaviorKind behaviorKind;
    private final int range;

    EnemyType(String displayName, int baseHp, int baseAttack, int baseExp, BehaviorKind behaviorKind, int range) {
        this.displayName = displayName;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseExp = baseExp;
        this.behaviorKind = behaviorKind;
        this.range = range;
    }

    public String getDisplayName() { return this.displayName; }
    public int getBaseHp() { return this.baseHp; }
    public int getBaseAttack() { return this.baseAttack; }
    public int getBaseExp() { return this.baseExp; }
    public BehaviorKind getBehaviorKind() { return this.behaviorKind; }
    public int getRange() { return this.range; }
}
