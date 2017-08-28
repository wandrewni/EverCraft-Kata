public class Weapon {
    protected int baseDamage;
    protected int bonusDamage;
    protected int attackModifier;
    protected int critModifier;

    public Weapon(int baseDamage, int bonusDamage, int attackModifier, int critModifier){
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

    public int attackModifier(Character wielder, Character opponent){
        return attackModifier;
    }

    public int critModifier() {
        return critModifier;
    }
}
