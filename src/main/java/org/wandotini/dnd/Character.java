package org.wandotini.dnd;

import org.wandotini.dnd.armor.Armor;
import org.wandotini.dnd.armor.NoArmor;
import org.wandotini.dnd.items.Item;
import org.wandotini.dnd.weapons.Unarmed;
import org.wandotini.dnd.weapons.Weapon;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.wandotini.util.StreamUtils.add;

public class Character {

	private String name;
	private Alignment alignment;
	private int strength = 10, dexterity = 10, constitution = 10, wisdom = 10, intelligence = 10, charisma = 10;
	public int baseArmorClass = 10;
	public int hitPoints = 5;
	private int maxHitPoints = 5;
	private int xp = 0;
	private int level = 1;
	private CharacterClass myClass = CharacterClass.UNCLASSED;
	private CharacterRace race = CharacterRace.HUMAN;
	private Weapon weapon = new Unarmed();
	private Armor armor = new NoArmor();
	private Shield shield = new NoShield();
	private List<Item> items = new ArrayList<Item>();

	public Character(CharacterRace race, CharacterClass myClass, int strength, int dexterity, int constitution, int wisdom, int intelligence, int charisma) {
		this.race = race;
		this.myClass = myClass;
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.wisdom = wisdom;
		this.intelligence = intelligence;
		this.charisma = charisma;
		initStats();
	}

	private void initStats() {
		initHitPoints();
		initAlignment();
		initArmorClass();
	}

	private void initAlignment() {
		if (CharacterClass.PALADIN == myClass)
            alignment = Alignment.GOOD; // TODO possibly have validation check that disallows selecting other options
	}

	private void initHitPoints() {
        this.maxHitPoints = getHitpointIncrease();
        this.hitPoints = maxHitPoints;
    }

	private void initArmorClass() {
		baseArmorClass = CharacterRace.ORC == race ? 12 : 10;
	}

    public void attack(Character defender, int dieRoll){
		boolean criticalHit = isCriticalHit(dieRoll);
		if (criticalHit || attackHits(defender, dieRoll)) {
			applyDamage(defender, criticalHit);
			gainXp();
		}
	}

	private void applyDamage(Character defender, boolean critical) {
		int multiplier = 1;
		if (critical)
            multiplier = getCritDamageMultiplierVersus(defender);
		int damage = Math.max(multiplier * baseDamageVersus(defender), 1);
		defender.hitPoints -= Math.max(damage - defender.armor.damageReduction(), 0);
	}

	private boolean isCriticalHit(int dieRoll) {
		int criticalRangeBonus = CharacterRace.ELF == race ? 1 : 0;
		return dieRoll + criticalRangeBonus >= 20;
	}

	private int getCritDamageMultiplierVersus(Character defender) {
		boolean paladinFightingEvil = CharacterClass.PALADIN == myClass && Alignment.EVIL == defender.getAlignment();
		boolean rogue = CharacterClass.ROGUE == myClass;
		int classModifier = paladinFightingEvil || rogue ? 3 : 2;
		return classModifier + weapon.critModifier();
	}

	private boolean attackHits(Character defender, int dieRoll) {
        return dieRoll + attackModifierVersus(defender) >= defender.getArmorClassVersusAttacker(this);
    }

    private int getArmorClassVersusAttacker(Character attacker){
        int armorClass = baseArmorClass;

        if (myClass == CharacterClass.MONK)
            armorClass += Math.max(getWisdomModifier(), 0);
        if (attacker.myClass == CharacterClass.ROGUE)
            armorClass += Math.min(getDexterityModifier(), 0); // rogue ignores positive dex modifiers on attacker
        else
            armorClass += getDexterityModifier();

		// racial bonuses
		return armorClass + getRacialArmorClassBonusVersus(attacker) + getEquipmentArmorClassBonuses();
    }

	private int getEquipmentArmorClassBonuses() {
		return armor.armorClassBonus() +
				shield.armorClassBonus() +
				items.stream()
						.map(Item::armorClassBonus)
						.reduce(0, add());
	}

	private int getRacialArmorClassBonusVersus(Character attacker) {
		boolean elfAttackedByOrc = CharacterRace.ELF == race && CharacterRace.ORC == attacker.getRace();
		boolean halflingAttackedByNonHalfling = race == CharacterRace.HALFLING && attacker.getRace() != CharacterRace.HALFLING;
		if (elfAttackedByOrc || halflingAttackedByNonHalfling)
            return 2;
		return 0;
	}


	private int attackModifierVersus(Character opponent) {
		int abilityModifier = CharacterClass.ROGUE == myClass ? getDexterityModifier() : getStrengthModifier();
		return abilityModifier +
				myClass.levelAttackBonus(level) +
				getAttackRollBonusAgainst(opponent) +
				getEquipmentAttackModifiers(opponent);
	}

	private int getAttackRollBonusAgainst(Character opponent) {
		int bonus = 0;
		boolean paladinFightingEvil = CharacterClass.PALADIN == myClass && Alignment.EVIL == opponent.getAlignment();
		boolean dwarfFightingOrc = CharacterRace.DWARF == race && CharacterRace.ORC == opponent.getRace();
		if (paladinFightingEvil)
			bonus += 2;
		if (dwarfFightingOrc)
			bonus += 2;
		return bonus;
	}

	private int getEquipmentAttackModifiers(Character opponent) {
		return weapon.attackModifier(opponent) +
				armor.attackModifier() +
				shield.attackModifier(this) +
				items.stream()
						.map(item -> item.attackModifier(this, opponent))
						.reduce(0, add());
	}

	private int baseDamageVersus(Character opponent) {
		int base = weapon.baseDamage();
		boolean paladinFightingEvil = CharacterClass.PALADIN == myClass && Alignment.EVIL == opponent.getAlignment();
		boolean dwarfFightingOrc = CharacterRace.DWARF == race && CharacterRace.ORC == opponent.getRace();
		if (paladinFightingEvil)
			base += 2;
		if (dwarfFightingOrc)
			base += 2;
		return base + getStrengthModifier() + weapon.bonusDamage(opponent);
	}

	private void gainXp() {
		xp += 10;
		if (xp % 1000 == 0)
			levelUp();
	}

	private void levelUp() {
		level++;
		// TODO do hitpoints increase on level up in addition to max hp?
		hitPoints += getHitpointIncrease();
		maxHitPoints += getHitpointIncrease();
	}

	private int getHitpointIncrease() {
		int constitutionBonus = getConstitutionModifier();
		if (race == CharacterRace.DWARF && constitutionBonus > 0)
			constitutionBonus *= 2;
		return Math.max(myClass.baseHitPoints() + constitutionBonus,1);
	}

	private int getBaseModifier(int ability) {
		return ability / 2 - 5;
	}

	public int getStrengthModifier() {
		int base = getBaseModifier(getStrength());
		if (race == CharacterRace.ORC)
    		return 2 + base;
    	if (race == CharacterRace.HALFLING)
    		return -1 + base;
		return base;
	}

	public int getDexterityModifier() {
		int base = getBaseModifier(dexterity);
		if (EnumSet.of(CharacterRace.ELF, CharacterRace.HALFLING).contains(race))
    		return 1 + base;
		return base;
	}

	public int getConstitutionModifier() {
		int base = getBaseModifier(constitution);
		if (race == CharacterRace.DWARF)
    		return 1 + base;
    	else if (race == CharacterRace.ELF)
    		return -1 + base;
		return base;
	}

	public int getWisdomModifier() {
		int base = getBaseModifier(wisdom);
		if (race == CharacterRace.ORC)
			return -1 + base;
		return base;
	}

	public int getIntelligenceModifier() {
		int base = getBaseModifier(intelligence);
		if (race == CharacterRace.ORC)
			return -1 + base;
    	return base;
	}

	public int getCharismaModifier() {
		int base = getBaseModifier(charisma);
		if (EnumSet.of(CharacterRace.ORC, CharacterRace.DWARF).contains(race))
			return -1 + base;
    	return base;
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
	    if (race == CharacterRace.HALFLING && alignment == Alignment.EVIL)
	    	throw new IllegalStateException("Halfling alignment may not be evil");
		this.alignment = alignment;
	}

	public Alignment getAlignment(){
		return alignment;
	}

	public int getStrength() {
		return strength + items.stream()
				.map(Item::strengthBonus)
				.reduce(0, add());
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

	public CharacterClass getCharacterClass() {
		return myClass;
	}

	public CharacterRace getRace() {
		return race;
	}

	public void equip(Weapon weapon) {
		this.weapon = weapon;
		this.weapon.setWielder(this);
	}

	public void equip(Armor armor) {
		if (! armor.canBeEquippedBy(this))
			throw new IllegalStateException();
		this.armor = armor;
		this.armor.setWearer(this);
	}

	public void equip(Shield shield) {
		this.shield = shield;
	}

	public void equip(Item item) {
		this.items.add(item);
	}
}