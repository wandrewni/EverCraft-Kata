package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;
import org.wandotini.dnd.CharacterClass;
import org.wandotini.dnd.CharacterRace;

public class PlateArmor extends Armor {
    public int armorClassBonus() {
        return 8;
    }

    public boolean canBeEquippedBy(Character character) {
        return character.getRace() == CharacterRace.DWARF ||
                character.getCharacterClass() == CharacterClass.FIGHTER;
    }

    public int damageReduction() {
        return 0;
    }

    @Override
    public int attackModifier() {
        return 0;
    }
}
