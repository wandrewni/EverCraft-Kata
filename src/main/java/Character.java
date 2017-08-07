public class Character {

	private String name;
	private Alignment alignment; //enum you made in Alignment.java
	private int strength = 10, dexterity = 10, constitution = 10, wisdom = 10, intelligence = 10, charisma = 10;
	public int baseArmorClass = 10;
	public int hitPoints = 5;
	private int maxHitPoints = 5;
	private int xp = 0;
	private int level = 1;

	public Character(){}

	public Character(int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.wisdom = wisdom;
		this.intelligence = intelligence;
		this.charisma = charisma;
		this.hitPoints = getHitpointIncrease(constitution);
		this.maxHitPoints = hitPoints;
	}

    public void attack(Character opponent, int dieRoll){
		if (dieRoll == 20) {
			gainXp();
			opponent.hitPoints -= Math.max(2 * baseDamage(), 1);
		} else if (dieRoll + attackModifier() >= opponent.getArmorClass()) {
			gainXp();
			opponent.hitPoints -= Math.max(baseDamage(), 1);
		}
	}

	private void gainXp() {
		xp += 10;
		if (xp == 1000 * level)
			levelUp();
	}

	private void levelUp() {
		level++;
		hitPoints += getHitpointIncrease(constitution);
	}

    private int getHitpointIncrease(int constitution) {
        return Math.max(5 + getModifier(constitution),1);
    }

	private int attackModifier() {
		return getModifier(strength) + level / 2;
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