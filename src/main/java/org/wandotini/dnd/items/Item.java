package org.wandotini.dnd.items;

import org.wandotini.dnd.Character;

public abstract class Item {
    public abstract int armorClassBonus();
    public abstract int strengthBonus();
    public abstract int attackModifier(Character wearer, Character defender);
}
