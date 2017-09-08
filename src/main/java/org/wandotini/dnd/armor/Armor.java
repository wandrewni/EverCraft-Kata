package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;

public abstract class Armor {
    protected Character wearer;

    public abstract int armorClassBonus();
    public abstract boolean canBeEquippedBy(Character player);
    public abstract int damageReduction();
    public abstract int attackModifier();

    public void setWearer(Character wearer) {
        this.wearer = wearer;
    }
}
