package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;
import org.wandotini.dnd.CharacterRace;

public class ElvenChainmail extends Armor {
    @Override
    public int armorClassBonus() {
        return wornByElf() ? 8 : 5;
    }

    @Override
    public boolean canBeEquippedBy(Character player) {
        return true;
    }

    @Override
    public int damageReduction() {
        return 0;
    }

    @Override
    public int attackModifier() {
        return wornByElf() ? 1 : 0;
    }

    private boolean wornByElf() {
        return wearer.getRace() == CharacterRace.ELF;
    }
}
