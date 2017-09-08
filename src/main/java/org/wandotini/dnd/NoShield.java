package org.wandotini.dnd;

public class NoShield extends Shield {
    @Override
    public int attackModifier() {
        return 0;
    }

    @Override
    public int armorClassBonus() {
        return 0;
    }
}
