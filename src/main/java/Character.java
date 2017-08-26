public class Character {

	private String name;
	private Alignment alignment;
	private int strength = 10, dexterity = 10, constitution = 10, wisdom = 10, intelligence = 10, charisma = 10;
	public int baseArmorClass = 10;
	public int hitPoints = 5;
	private int maxHitPoints = 5;
	private int xp = 0;
	private int level = 1;
	private CharacterClass myClass = CharacterClass.UNCLASSED; // TODO get rid of?

    // TODO consolidate constructors?
	public Character(){
		initHitPoints();
		initAlignment();
	}

	public Character(int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.wisdom = wisdom;
		this.intelligence = intelligence;
		this.charisma = charisma;
        initHitPoints();
		initAlignment();
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
		initAlignment();
    }

    public Character(CharacterClass characterClass) {
	    myClass = characterClass;
	    initHitPoints();
		initAlignment();
	}

	private void initAlignment() {
		if (CharacterClass.PALADIN == myClass)
            alignment = Alignment.GOOD;
	}

	private void initHitPoints() {
        this.hitPoints = getHitpointIncrease(constitution);
        this.maxHitPoints = hitPoints;
    }

    public void attack(Character opponent, int dieRoll){
		if (dieRoll == 20) {
			gainXp();
			opponent.hitPoints -= Math.max(getCritDamageModifierVersus(opponent) * baseDamageVersus(opponent), 1);
		} else if (attackHits(opponent, dieRoll)) {
			gainXp();
			opponent.hitPoints -= Math.max(baseDamageVersus(opponent), 1);
		}
	}

	private int getCritDamageModifierVersus(Character opponent) {
		boolean paladinFightingEvil = CharacterClass.PALADIN == myClass && Alignment.EVIL == opponent.getAlignment();
		boolean rogue = CharacterClass.ROGUE == myClass;
		return paladinFightingEvil || rogue ? 3 : 2;
	}

	private boolean attackHits(Character opponent, int dieRoll) {
        return dieRoll + attackModifierVersus(opponent) >= opponent.getArmorClassVersusAttacker(this);
    }

    private int getArmorClassVersusAttacker(Character attacker){
        int armorClass = baseArmorClass;
        int wisdomMod = getModifier(wisdom);
        int dexMod = getModifier(dexterity);

        if (CharacterClass.MONK == myClass && wisdomMod > 0)
            armorClass += wisdomMod;
        if (CharacterClass.ROGUE == attacker.myClass)
            return armorClass + Math.min(dexMod, 0);
        else
            return armorClass + dexMod;
    }

    private void gainXp() {
		xp += 10;
		if (xp % 1000 == 0)
			levelUp();
	}

	private void levelUp() {
		level++;
		// TODO do hitpoints increase on level up in addition to max hp?
		hitPoints += getHitpointIncrease(constitution);
		maxHitPoints += getHitpointIncrease(constitution);
	}

    private int getHitpointIncrease(int constitution) {
        int baseHitpoints;
        switch (myClass) {
            case FIGHTER:
                baseHitpoints = 10;
                break;
            case MONK:
                baseHitpoints = 6;
                break;
			case PALADIN:
				baseHitpoints = 8;
				break;
            default:
                baseHitpoints = 5;
        }
        return Math.max(baseHitpoints + getModifier(constitution),1);
    }

	private int attackModifierVersus(Character opponent) {
		int abilityScore = CharacterClass.ROGUE == myClass ? dexterity : strength;
		return getModifier(abilityScore) + getAttackBonusForLevel() + getAttackRollBonusAgainst(opponent);
	}

	private int getAttackRollBonusAgainst(Character opponent) {
		return CharacterClass.PALADIN == myClass && Alignment.EVIL == opponent.getAlignment() ? 2 : 0;
	}

	private int getAttackBonusForLevel() {
		switch (myClass) {
			case MONK:
				int bonus = 0;
				for (int i = 1; i <= level; i++)
					if (i % 2 == 0 || i % 3 == 0)
						bonus++;
				return bonus;
			case FIGHTER:
			case PALADIN:
				return level - 1;
			default:
				return level / 2;
		}
	}

	private int baseDamageVersus(Character opponent) {
        int base;
		boolean monk = CharacterClass.MONK == myClass;
		boolean paladinFightingEvil = CharacterClass.PALADIN == myClass && Alignment.EVIL == opponent.getAlignment();
		if (monk || paladinFightingEvil)
        	base = 3;
        else
        	base = 1;
        return base + getModifier(strength);
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
	    if (myClass == CharacterClass.PALADIN)
	    	throw new IllegalStateException("Paladin alignment cannot be changed");
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