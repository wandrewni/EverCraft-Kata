public class Character {

	private String name;
	private Alignment alignment;
	private int strength = 10, dexterity = 10, constitution = 10, wisdom = 10, intelligence = 10, charisma = 10;
	public int baseArmorClass = 10;
	public int hitPoints = 5;
	private int maxHitPoints = 5;
	private int xp = 0;
	private int level = 1;
	private CharacterClass myClass;
	public Character(){}

	public Character(int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.wisdom = wisdom;
		this.intelligence = intelligence;
		this.charisma = charisma;
        initHitPoints();
	}

    public Character(CharacterClass myClass, int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
	    this.myClass = myClass;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.wisdom = wisdom;
        this.intelligence = intelligence;
        this.charisma = charisma;
        initHitPoints();
    }

    public Character(CharacterClass characterClass) {
	    myClass = characterClass;
	    initHitPoints();
    }

    private void initHitPoints() {
        this.hitPoints = getHitpointIncrease(constitution);
        this.maxHitPoints = hitPoints;
    }

    public void attack(Character opponent, int dieRoll){
		if (dieRoll == 20) {
			gainXp();
            int critDamageModifier = CharacterClass.ROGUE == myClass ? 3 : 2;
            opponent.hitPoints -= Math.max(critDamageModifier * baseDamage(), 1);
		} else if (attackHits(opponent, dieRoll)) {
			gainXp();
			opponent.hitPoints -= Math.max(baseDamage(), 1);
		}
	}

    private boolean attackHits(Character opponent, int dieRoll) {
        int opponentArmorClass = opponent.getArmorClass();
        if (CharacterClass.ROGUE == myClass && opponent.getModifier(opponent.dexterity) > 0)
            opponentArmorClass = opponent.baseArmorClass;
        return dieRoll + attackModifier() >= opponentArmorClass;
    }

    private void gainXp() {
		xp += 10;
		if (xp == 1000 * level)
			levelUp();
	}

	private void levelUp() {
		level++;
		// TODO do hitpoints increase on level up in addition to max hp?
		hitPoints += getHitpointIncrease(constitution);
		maxHitPoints += getHitpointIncrease(constitution);
	}

    private int getHitpointIncrease(int constitution) {
        int baseHitpoints = CharacterClass.FIGHTER == myClass ? 10 : 5;
        return Math.max(baseHitpoints + getModifier(constitution),1);
    }

	private int attackModifier() {
        int levelBonus = CharacterClass.FIGHTER == myClass ? level
                : level / 2;
        int attackAbility = CharacterClass.ROGUE == myClass ? dexterity : strength;
        return getModifier(attackAbility) + levelBonus;
	}

	private int baseDamage() {
		return 1 + getModifier(strength);
	}

	private int getArmorClass() {
		return baseArmorClass + getModifier(dexterity);
	}

	private int getModifier(int ability) {
		return ability / 2 - 5;
	}


	/** plain Getters and setters **/

	public void setName(String name){
		this.name = name;
	}

	public String getName(){ //object instead of void, because it returns an object
		return name;
	}

	public void setAlignment(Alignment alignment){
	    if (myClass == CharacterClass.ROGUE && alignment == Alignment.GOOD)
	        throw new IllegalStateException("Rogues may not be good");
		this.alignment = alignment;
	}

	public Alignment getAlignment(){
		return alignment;
	}

	public int getStrength() {
		return strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getConstitution() {
		return constitution;
	}

	public int getWisdom() {
		return wisdom;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getCharisma() {
		return charisma;
	}

	public int getXP() {
		return xp;
	}

	public int getLevel() {
		return level;
	}

    public int getMaxHitPoints() {
        return maxHitPoints;
    }
}