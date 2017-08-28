package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public abstract class Armor {
    public abstract int armorClassBonus();
    public abstract boolean canBeEquippedBy(Character player);
}
