package org.wandotini.dnd.weapons;

import org.wandotini.dnd.Character;
import org.wandotini.dnd.CharacterRace;

public class ElvenLongsword extends Weapon {
    public ElvenLongsword() {
        super(5, 1, 1, 0);
    }

    @Override
    public int bonusDamage(Character wielder, Character defender) {
        return calcRacialBonus(wielder, defender);
    }

    @Override
    public int attackModifier(Character wielder, Character defender) {
        return calcRacialBonus(wielder, defender);
    }

    private int calcRacialBonus(Character wielder, Character defender) {
        boolean wieldedByElf = wielder.getRace() == CharacterRace.ELF;
        boolean againstOrc = defender.getRace() == CharacterRace.ORC;
        if (wieldedByElf && againstOrc)
            return 5;
        else if (wieldedByElf || againstOrc)
            return 2;
        else
            return 1;
    }
}
