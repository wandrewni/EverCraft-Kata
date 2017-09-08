package org.wandotini.dnd;

public enum CharacterClass {
    FIGHTER(10, bonusEveryLevel()),
    PALADIN(8, bonusEveryLevel()),
    ROGUE(5, bonusEverySecondLevel()),
    UNCLASSED(5, bonusEverySecondLevel()),
    MONK(6, bonusEverySecondAndThirdLevel());

    private final int baseHitPoints;
    private final LevelBonusProvider levelBonusProvider;

    CharacterClass(int baseHitPoints, LevelBonusProvider levelBonusProvider) {
        this.baseHitPoints = baseHitPoints;
        this.levelBonusProvider = levelBonusProvider;
    }

    public int baseHitPoints() {
        return baseHitPoints;
    }

    public int levelAttackBonus(int level) {
        return levelBonusProvider.levelBonus(level);
    }

    interface LevelBonusProvider {
        int levelBonus(int level);
    }

    private static LevelBonusProvider bonusEverySecondAndThirdLevel() {
        return level -> {
            int bonus = 0;
            for (int i = 1; i <= level; i++)
                if (i % 2 == 0 || i % 3 == 0)
                    bonus++;
            return bonus;
        };
    }

    private static LevelBonusProvider bonusEverySecondLevel() {
        return level -> level / 2;
    }

    private static LevelBonusProvider bonusEveryLevel() {
        return level -> level - 1;
    }
}
