package org.wandotini.dnd;

public class Shield {
    protected Character wearer;

    public int armorClassBonus() {
        return 3;
    }

    public int attackModifier() {
        return wearer.getCharacterClass() == CharacterClass.FIGHTER ? -2 : -4;
    }

    public void setWearer(Character wearer) {
        this.wearer = wearer;
    }
}
