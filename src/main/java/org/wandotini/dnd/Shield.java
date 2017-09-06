package org.wandotini.dnd;

public class Shield {
    public int armorClassBonus() {
        return 3;
    }

    public int attackModifier(Character character) {
        return character.getCharacterClass() == CharacterClass.FIGHTER ? -2 : -4;
    }
}
