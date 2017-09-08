package org.wandotini.dnd.items;

import org.wandotini.dnd.Character;

public class BeltOfGiantStrength extends Item {
    @Override
    public int armorClassBonus() {
        return 0;
    }

    @Override
    public int strengthBonus() {
        return 4;
    }

    @Override
    public int attackModifier(Character defender) {
        return 0;
    }
}
