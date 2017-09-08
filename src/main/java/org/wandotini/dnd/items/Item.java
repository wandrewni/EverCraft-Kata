package org.wandotini.dnd.items;

import org.wandotini.dnd.Character;

public abstract class Item {
    protected Character wearer;

    public abstract int armorClassBonus();
    public abstract int strengthBonus();
    public abstract int attackModifier(Character defender);

    public void setWearer(Character wearer) {
        this.wearer = wearer;
    }
}
