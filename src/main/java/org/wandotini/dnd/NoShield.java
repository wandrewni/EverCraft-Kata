package org.wandotini.dnd;

public class NoShield extends Shield {
    @Override
    public int attackModifier(Character character) {
        return 0;
    }

    @Override
    public int armorClassBonus() {
        return 0;
    }
}
