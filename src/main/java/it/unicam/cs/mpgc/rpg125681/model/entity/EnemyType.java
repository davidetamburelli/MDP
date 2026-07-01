package it.unicam.cs.mpgc.rpg125681.model.entity;

import it.unicam.cs.mpgc.rpg125681.model.entity.behavior.BehaviorKind;

public enum EnemyType {

    GOBLIN("Goblin", 18, 6, 10, BehaviorKind.MELEE, 0, AbsorbStat.ATTACK, 1, 0.30, 5),
    SKELETON("Skeleton", 28, 8, 14, BehaviorKind.MELEE, 0, AbsorbStat.MAX_HP, 6, 0.15, 8),
    ARCHER("Archer", 16, 7, 16, BehaviorKind.RANGED, 3, AbsorbStat.ATTACK, 2, 0.20, 10),
    MAGE("Mage", 14, 11, 22, BehaviorKind.RANGED, 4, AbsorbStat.ATTACK, 3, 0.25, 15);

    private final String displayName;
    private final int baseHp;
    private final int baseAttack;
    private final int baseExp;
    private final BehaviorKind behaviorKind;
    private final int range;
    private final AbsorbStat absorbStat;
    private final int absorbAmount;
    private final double dropChance;
    private final int baseGold;

    EnemyType(String displayName, int baseHp, int baseAttack, int baseExp, BehaviorKind behaviorKind,
              int range, AbsorbStat absorbStat, int absorbAmount, double dropChance, int baseGold) {
        this.displayName = displayName;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseExp = baseExp;
        this.behaviorKind = behaviorKind;
        this.range = range;
        this.absorbStat = absorbStat;
        this.absorbAmount = absorbAmount;
        this.dropChance = dropChance;
        this.baseGold = baseGold;
    }

    public String getDisplayName() { return this.displayName; }
    public int getBaseHp() { return this.baseHp; }
    public int getBaseAttack() { return this.baseAttack; }
    public int getBaseExp() { return this.baseExp; }
    public BehaviorKind getBehaviorKind() { return this.behaviorKind; }
    public int getRange() { return this.range; }
    public AbsorbStat getAbsorbStat() { return this.absorbStat; }
    public int getAbsorbAmount() { return this.absorbAmount; }
    public double getDropChance() { return this.dropChance; }
    public int getBaseGold() { return this.baseGold; }
}