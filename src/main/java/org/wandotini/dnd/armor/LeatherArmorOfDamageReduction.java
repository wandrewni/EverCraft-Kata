package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public class LeatherArmorOfDamageReduction extends Armor {
    public int armorClassBonus(Character player) {
        return 2;
    }

    public boolean canBeEquippedBy(Character player) {
        return true;
    }

    public int damageReduction() {
        return 2;
    }

    @Override
    public int attackBonus(Character player) {
        return 0;
    }
}
