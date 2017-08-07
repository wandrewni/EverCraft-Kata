public class Character {

	private String name;
	private Alignment alignment; //enum you made in Alignment.java
	private int strength = 10, dexterity = 10, constitution = 10, wisdom = 10, intelligence = 10, charisma = 10;
	public int baseArmorClass = 10;
	public int hitPoints = 5;
	private int xp = 0;

	public Character(){}

	public Character(int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.wisdom = wisdom;
		this.intelligence = intelligence;
		this.charisma = charisma;
		this.hitPoints = Math.max(5 + getModifier(constitution),1);
	}

	public void attack(Character opponent, int dieRoll){
		if (dieRoll == 20) {
			gainXp();
			opponent.hitPoints -= Math.max(2 * baseDamage(), 1);
		}
		else if (dieRoll + attackModifier() >= opponent.getArmorClass()) {
			gainXp();
			opponent.hitPoints -= Math.max(baseDamage(), 1);
		}
	}

	private void gainXp() {
		xp += 10;
	}

	private int attackModifier() {
		return getModifier(strength);
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

	public void setName(String name){
		this.name = name; //private name = the one specific to this method
	};
	public String getName(){ //object instead of void, because it returns an object
		return name;
	};

	public void setAlignment(Alignment alignment){
		this.alignment = alignment; //private alignment = one specific to method
	};

	public Alignment getAlignment(){
		return alignment;
	};

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
}