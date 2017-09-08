package org.wandotini.dnd.items;

import org.wandotini.dnd.Character;

public class RingOfProtection extends Item {
    public int armorClassBonus() {
        return 2;
    }

    @Override
    public int strengthBonus() {
        return 0;
    }

    @Override
    public int attackModifier(Character defender) {
        return 0;
    }
}
