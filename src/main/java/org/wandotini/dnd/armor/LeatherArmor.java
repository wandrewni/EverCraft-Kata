package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public class LeatherArmor extends Armor{
    public int armorClassBonus() {
        return 2;
    }

    public boolean canBeEquippedBy(Character player) {
        return true;
    }
}
