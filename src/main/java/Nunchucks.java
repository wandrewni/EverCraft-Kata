public class Nunchucks extends Weapon {
    public Nunchucks(){
        super(6, 0, -4, 0);
    }

    @Override
    public int attackModifier(Character wielder, Character opponent) {
        if (wielder.getCharacterClass() == CharacterClass.MONK)
            return 0;
        else return super.attackModifier(wielder, opponent);
    }
}
