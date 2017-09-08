package org.wandotini.dnd.weapons;

import org.wandotini.dnd.Character;

public abstract class Weapon {
    protected int baseDamage;
    protected int bonusDamage;
    protected int attackModifier;
    protected int critModifier;

    Weapon(int baseDamage, int bonusDamage, int attackModifier, int critModifier){
        this.baseDamage = baseDamage;
        this.bonusDamage = bonusDamage;
        this.attackModifier = attackModifier;
        this.critModifier = critModifier;
    }

    public int baseDamage(Character character){
        return baseDamage;
    }

    public int bonusDamage(Character wielder, Character defender){
        return bonusDamage;
    }

    public int attackModifier(Character wielder, Character defender){
        return attackModifier;
    }

    public int critModifier() {
        return critModifier;
    }
}
