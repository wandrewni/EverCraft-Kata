package org.wandotini.dnd.weapons;

import org.wandotini.dnd.CharacterClass;

public class Unarmed extends Weapon {
    public Unarmed() {
        super(1, 0, 0, 0);
    }

    @Override
    public int baseDamage() {
        if (wielder.getCharacterClass() == CharacterClass.MONK)
            return 3;
        else
            return baseDamage;
    }
}
