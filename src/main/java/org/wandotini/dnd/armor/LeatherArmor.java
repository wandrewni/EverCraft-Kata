package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public class LeatherArmor extends Armor{
    public int armorClassBonus(Character player) {
        return 2;
    }

    public boolean canBeEquippedBy(Character player) {
        return true;
    }

    public int damageReduction() {
        return 0;
    }

    @Override
    public int attackModifier(Character player) {
        return 0;
    }
}
