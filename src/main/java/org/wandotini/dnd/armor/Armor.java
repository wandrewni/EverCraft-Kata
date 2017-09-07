package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public abstract class Armor {
    public abstract int armorClassBonus(Character player);
    public abstract boolean canBeEquippedBy(Character player);
    public abstract int damageReduction();
    public abstract int attackModifier(Character player);
}
