public class Unarmed extends Weapon {
    public Unarmed() {
        super(1, 0, 0, 0);
    }

    @Override
    public int baseDamage(Character character) {
        if (character.getCharacterClass() == CharacterClass.MONK)
            return 3;
        else
            return baseDamage;
    }
}
