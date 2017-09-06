package org.wandotini.dnd.armor;

import org.wandotini.dnd.Character;
import org.wandotini.dnd.CharacterRace;

public class ElvenChainmail extends Armor {
    @Override
    public int armorClassBonus(Character player) {
        return isElf(player) ? 8 : 5;
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
    public int attackBonus(Character player) {
        return isElf(player) ? 1 : 0;
    }

    private boolean isElf(Character player) {
        return player.getRace() == CharacterRace.ELF;
    }
}
