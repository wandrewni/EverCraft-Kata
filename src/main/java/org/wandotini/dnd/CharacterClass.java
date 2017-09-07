package org.wandotini.dnd;

public enum CharacterClass {
    FIGHTER(10), ROGUE(5), MONK(6), PALADIN(8), UNCLASSED(5);

    private final int baseHitPoints;

    CharacterClass(int baseHitPoints) {
        this.baseHitPoints = baseHitPoints;
    }

    public int baseHitPoints() {
        return baseHitPoints;
    }
}
