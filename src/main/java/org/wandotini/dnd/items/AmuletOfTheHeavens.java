package org.wandotini.dnd.items;

import org.wandotini.dnd.Alignment;
import org.wandotini.dnd.Character;
import org.wandotini.dnd.CharacterClass;

public class AmuletOfTheHeavens extends Item {
    @Override
    public int armorClassBonus() {
        return 0;
    }

    @Override
    public int strengthBonus() {
        return 0;
    }

    @Override
    public int attackModifier(Character defender) {
        int attackBonus = 0;
        if (Alignment.NEUTRAL == defender.getAlignment())
            attackBonus += 1;
        else if (Alignment.EVIL == defender.getAlignment())
            attackBonus += 2;
        if (wearer.getCharacterClass() == CharacterClass.PALADIN)
            attackBonus *= 2;
        return attackBonus;
    }
}
