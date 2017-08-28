package org.wandotini.dnd;

import org.junit.Before;
import org.junit.Test;
import org.wandotini.dnd.armor.LeatherArmor;
import org.wandotini.dnd.armor.PlateArmor;
import org.wandotini.dnd.weapons.ElvenLongsword;
import org.wandotini.dnd.weapons.Longsword;
import org.wandotini.dnd.weapons.Nunchucks;
import org.wandotini.dnd.weapons.WarAxePlus2;

import static org.junit.Assert.assertEquals;

public class CharacterTest {

    private static final int BASE_HP = 5;
    private Character mainCharacter;
    private Character worstCharacter;

    @Before
    public void setup() {

        mainCharacter = new Character();
        worstCharacter = new Character();

    }

    @Test
    public void testCanSetNGetName() {

        mainCharacter.setName("Jandrew");

        assertEquals("Jandrew", mainCharacter.getName());
    }

    @Test
    public void testCanSetNGetAlignment() {  //uses the enum you made in Alignment.java

        mainCharacter.setAlignment(Alignment.EVIL);

        assertEquals(Alignment.EVIL, mainCharacter.getAlignment());
    }

    @Test
    public void testVerifyArmorNHitPointValue() {

        assertEquals(10, mainCharacter.baseArmorClass);
        assertUndamaged(mainCharacter);

    }

    @Test
    public void testStrongAttack() {

        int dieRoll = 19;

        mainCharacter.attack(worstCharacter, dieRoll);

        assertHpLost(1, worstCharacter);

    }

    @Test
    public void testWeakAttack() {

        int dieRoll = 3;

        mainCharacter.attack(worstCharacter, dieRoll);

        assertUndamaged(worstCharacter);

    }

    @Test
    public void testPerfectAttack() {

        int dieRoll = 20;

        mainCharacter.attack(worstCharacter, dieRoll);

        assertHpLost(2, worstCharacter);

    }

    @Test
    public void abilityScoresDefaultToTen() {
        assertEquals(10, mainCharacter.getStrength());
        assertEquals(10, mainCharacter.getDexterity());
        assertEquals(10, mainCharacter.getConstitution());
        assertEquals(10, mainCharacter.getWisdom());
        assertEquals(10, mainCharacter.getIntelligence());
        assertEquals(10, mainCharacter.getCharisma());
    }

    @Test
    public void strengthModifierIsAddedToAttackRollAndDamage() throws Exception {
        mainCharacter = new Character(17, 10, 10, 10, 10, 10);
        mainCharacter.attack(worstCharacter, 7);

        assertHpLost(4, worstCharacter);
    }

    @Test
    public void strengthModifierIsDoubledForDamageOnCrit() throws Exception {
        mainCharacter = new Character(17, 10, 10, 10, 10, 10);
        mainCharacter.attack(worstCharacter, 20);

        assertHpLost(8, worstCharacter);
    }

    @Test
    public void attackCannotBeModifiedBelowOne() throws Exception {
        mainCharacter = new Character(1, 10, 10, 10, 10, 10);
        mainCharacter.attack(worstCharacter, 19);

        assertHpLost(1, worstCharacter);

        worstCharacter = new Character();
        mainCharacter.attack(worstCharacter, 20);

        assertHpLost(1, worstCharacter);
    }

    @Test
    public void dexterityModifierIsAddedToArmorClassForDeterminingHit() throws Exception {
        mainCharacter = new Character(10, 14, 10, 10, 10, 10);
        worstCharacter = new Character(10, 4, 10, 10, 10, 10);

        worstCharacter.attack(mainCharacter, 11);

        assertUndamaged(this.mainCharacter);

        this.mainCharacter.attack(worstCharacter, 7);

        assertHpLost(1, this.worstCharacter);
    }

    @Test
    public void constitutionModifierIsAddedToHp() throws Exception {
        mainCharacter = new Character(10, 10, 16, 10, 10, 10);
        assertEquals(8, mainCharacter.hitPoints);
        mainCharacter = new Character(10, 10, 4, 10, 10, 10);
        assertEquals(2, mainCharacter.hitPoints);
    }

    @Test
    public void startingHpCannotBeReducedBelowOne() throws Exception {
        mainCharacter = new Character(10, 10, 1, 10, 10, 10);
        assertEquals(1, mainCharacter.hitPoints);
    }

    @Test
    public void characterGainsExperienceWhenHitting() throws Exception {
        assertXpGained(0, mainCharacter);
        mainCharacter.attack(worstCharacter, 10);
        assertXpGained(10, mainCharacter);
        mainCharacter.attack(worstCharacter, 9);
        assertXpGained(10, mainCharacter);

        // gains on crit
        mainCharacter.attack(worstCharacter, 20);
        assertXpGained(20, this.mainCharacter);
    }

    @Test
    public void characterLevelsUpEveryThousandXp() throws Exception {
        assertEquals(1, mainCharacter.getLevel());
        for (int i = 0; i < 100; i++)
            mainCharacter.attack(new Character(), 10);
        assertEquals(2, mainCharacter.getLevel());
        for (int i = 0; i < 100; i++)
            mainCharacter.attack(new Character(), 10);
        assertEquals(3, mainCharacter.getLevel());
    }

    @Test
    public void onLevelingUpGainHitpointsEqualToFivePlusConsModifier() throws Exception {
        assertEquals(5, mainCharacter.hitPoints);
        levelUp(mainCharacter, 1);
        assertEquals(10, mainCharacter.hitPoints);

        mainCharacter = new Character(10, 10, 16, 10, 10, 10);
        assertEquals(8, mainCharacter.hitPoints);
        levelUp(mainCharacter, 1);
        assertEquals(16, mainCharacter.hitPoints);
    }

    @Test
    public void evenCharactersWithWorstConstitutionGainOneHp() throws Exception {
        mainCharacter = new Character(10, 10, 1, 10, 10, 10);
        assertUndamaged(mainCharacter);
        levelUp(mainCharacter, 1);
        assertEquals(2, mainCharacter.hitPoints);
    }

    @Test
    public void forEveryEvenLevelAdd1ToAttackRolls() throws Exception {
        assertUndamaged(worstCharacter);
        mainCharacter.attack(worstCharacter, 9);
        assertUndamaged(worstCharacter);

        levelUp(mainCharacter, 1);

        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(1, worstCharacter);

        levelUp(mainCharacter, 2);

        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(2, worstCharacter);
    }

    @Test
    public void fighterHasTenHitPointsPerLevel() throws Exception {
        mainCharacter = new Character(CharacterClass.FIGHTER);
        assertEquals(10, mainCharacter.getMaxHitPoints());
        levelUp(mainCharacter, 1);
        assertEquals(20, mainCharacter.getMaxHitPoints());
    }

    @Test
    public void fighterGetsPlusOneToAttackEachLevel() throws Exception {
        mainCharacter = new Character(CharacterClass.FIGHTER);
        mainCharacter.attack(worstCharacter, 9);
        assertUndamaged(worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(1, worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(2, worstCharacter);
    }

    @Test
    public void rogueGetsTripleDamageOnCriticalHits() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE);
        assertUndamaged(worstCharacter);

        mainCharacter.attack(worstCharacter, 20);

        assertHpLost(3, worstCharacter);
    }

    @Test
    public void rogueAddsDexterityModifierInsteadOfStrengthModifierToAttackRolls() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE, 10, 16, 10, 10, 10, 10);

        mainCharacter.attack(worstCharacter, 7);

        assertHpLost(1, worstCharacter);
    }

    @Test
    public void rogueDoesntUseStrengthModifierForAttackRolls() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE, 16, 10, 10, 10, 10, 10);

        mainCharacter.attack(worstCharacter, 7);

        assertUndamaged(worstCharacter);
    }

    @Test
    public void rogueIgnoresOpponentDexterityModifierForArmorClassIfPositive() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE);
        worstCharacter = new Character(10, 16, 10, 10, 10, 10);

        mainCharacter.attack(worstCharacter, 10);

        assertHpLost(1, worstCharacter);
    }

    @Test
    public void rogueTakesAdvantageOfOpponentDexterityModifierForArmorClassIfNegative() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE);
        worstCharacter = new Character(10, 4, 10, 10, 10, 10);

        mainCharacter.attack(worstCharacter, 7);

        assertHpLost(1, worstCharacter);
    }

    @Test(expected = IllegalStateException.class)
    public void roguesCannotHaveGoodAlignment() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE);
        mainCharacter.setAlignment(Alignment.GOOD);
    }

    @Test
    public void monkGains6HitPointsPerlevel() throws Exception {
        mainCharacter = new Character(CharacterClass.MONK);
        assertEquals(6, mainCharacter.getMaxHitPoints());
        levelUp(mainCharacter, 1);
        assertEquals(12, mainCharacter.getMaxHitPoints());
    }

    @Test
    public void monkDoes3DamageOnSuccessfulHitAnd6onSuccessfulCrit() throws Exception {
        mainCharacter = new Character(CharacterClass.MONK);
        mainCharacter.attack(worstCharacter, 10);
        assertHpLost(3, worstCharacter);

        worstCharacter = new Character();
        mainCharacter.attack(worstCharacter, 20);
        assertHpLost(6, worstCharacter);
    }

    @Test
    public void monkAddsWisdomToDexterityModifierForDefending() throws Exception {
        mainCharacter = new Character(CharacterClass.MONK, 10, 14, 10, 14, 10, 10);
        worstCharacter.attack(mainCharacter, 13);

        assertUndamaged(mainCharacter);

        worstCharacter = new Character(CharacterClass.ROGUE);

        worstCharacter.attack(mainCharacter, 11);
        assertUndamaged(mainCharacter);

        worstCharacter.attack(mainCharacter, 12);
        assertHpLost(1, mainCharacter);
    }

    @Test
    public void monkDoesntAddNegativeWisdomToDefendingModifier() throws Exception {
        mainCharacter = new Character(CharacterClass.MONK, 10, 10, 10, 1, 10, 10);

        worstCharacter.attack(mainCharacter, 9);

        assertUndamaged(mainCharacter);
    }

    @Test
    public void monkAttackRollIsIncreasedEverySecondAndThirdLevels() throws Exception {
        mainCharacter = new Character(CharacterClass.MONK);

        mainCharacter.attack(worstCharacter, 9);
        assertUndamaged(worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(3, worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(6, worstCharacter);

        worstCharacter = new Character();
        mainCharacter = new Character(CharacterClass.MONK);
        levelUp(mainCharacter, 5);

        mainCharacter.attack(worstCharacter, 5);
        assertUndamaged(worstCharacter);
        mainCharacter.attack(worstCharacter, 6);
        assertHpLost(3, worstCharacter);
    }

    @Test
    public void paladinGains8hitPointsPerLevel() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);
        assertEquals(8, mainCharacter.getMaxHitPoints());
        levelUp(mainCharacter, 1);
        assertEquals(16, mainCharacter.getMaxHitPoints());
    }

    @Test
    public void paladinHasPlus2ToDamageWhenAttackingEvilCharacters() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);

        mainCharacter.attack(worstCharacter, 10);

        assertHpLost(1, worstCharacter);

        worstCharacter = new Character();
        worstCharacter.setAlignment(Alignment.EVIL);

        mainCharacter.attack(worstCharacter, 10);
        assertHpLost(3, worstCharacter);
    }

    @Test
    public void paladinHasPlus2ToRollsWhenAttackingEvilCharacters() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);

        mainCharacter.attack(worstCharacter, 9);

        assertUndamaged(worstCharacter);

        worstCharacter = new Character();
        worstCharacter.setAlignment(Alignment.EVIL);

        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(3, worstCharacter);

        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(6, worstCharacter);
    }

    @Test
    public void paladinDoesTripleDamageOnCritAgainstEvil() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);

        // normal crit
        mainCharacter.attack(worstCharacter, 20);
        assertHpLost(2, worstCharacter);

        // evil crit
        worstCharacter = new Character();
        worstCharacter.setAlignment(Alignment.EVIL);
        mainCharacter.attack(worstCharacter, 20);

        assertHpLost(9, worstCharacter);
    }

    @Test
    public void paladinGetsPlusOneToAttackEachLevel() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);
        mainCharacter.attack(worstCharacter, 9);
        assertUndamaged(worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(1, worstCharacter);

        levelUp(mainCharacter, 1);
        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(2, worstCharacter);
    }

    @Test
    public void paladinDefaultsToGoodAlignment() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);
        assertEquals(Alignment.GOOD, mainCharacter.getAlignment());
    }

    @Test(expected = IllegalStateException.class)
    public void paladinRestrictedToGoodAlignment() throws Exception {
        mainCharacter = new Character(CharacterClass.PALADIN);
        mainCharacter.setAlignment(Alignment.NEUTRAL);
    }

    @Test
    public void charactersDefaultToHuman() throws Exception {
      mainCharacter = new Character();
      assertEquals(CharacterRace.HUMAN, mainCharacter.getRace());
    }

    @Test
    public void orcModifiers() throws Exception {
        mainCharacter = new Character(CharacterRace.ORC);
        assertEquals(2, mainCharacter.getStrengthModifier());
        assertEquals(-1, mainCharacter.getIntelligenceModifier());
        assertEquals(-1, mainCharacter.getWisdomModifier());
        assertEquals(-1, mainCharacter.getCharismaModifier());
    }

    @Test
    public void orcsHavePlusTwoToArmorClass() throws Exception {
        worstCharacter = new Character(CharacterRace.ORC);
        mainCharacter.attack(worstCharacter, 10);
        assertUndamaged(worstCharacter);
        mainCharacter.attack(worstCharacter, 12);
        assertHpLost(1, worstCharacter);
    }

    @Test
    public void dwarfModifiers() throws Exception {
        mainCharacter = new Character(CharacterRace.DWARF);
        assertEquals(1, mainCharacter.getConstitutionModifier());
        assertEquals(-1, mainCharacter.getCharismaModifier());
    }

    @Test
    public void dwarfConstitutionModifierIsDoubledForHitPointGainIfPositive() throws Exception {
        mainCharacter = new Character(CharacterRace.DWARF, 10, 10, 12, 10, 10, 10);
        // doubled (1 basemod + 1 racemod)
        assertEquals(9, mainCharacter.getMaxHitPoints());

        mainCharacter = new Character(CharacterRace.DWARF, 10, 10, 6, 10, 10, 10);
        // not doubled (-2 basemod + 1 racemod)
        assertEquals(4, mainCharacter.getMaxHitPoints());
    }

    @Test
    public void dwarfHasPlus2ToAttackRollAndDamageVersusOrc() throws Exception {
        mainCharacter = new Character(CharacterRace.HUMAN);
        worstCharacter = new Character(CharacterRace.ORC);
        mainCharacter.attack(worstCharacter, 10); // orcs have base armor class of 12
        assertUndamaged(worstCharacter);

        mainCharacter = new Character(CharacterRace.DWARF);

        mainCharacter.attack(worstCharacter, 10); // 10 + 2 == 12, so meet the base armor class
        assertHpLost(3, worstCharacter);

        mainCharacter.attack(worstCharacter,20);
        assertHpLost(9, worstCharacter);
    }

    @Test
    public void elfModifiers() throws Exception {
        mainCharacter = new Character(CharacterRace.ELF);
        assertEquals(1, mainCharacter.getDexterityModifier());
        assertEquals(-1, mainCharacter.getConstitutionModifier());
    }

    @Test
    public void elfHasPlusOneToCriticalRange() throws Exception {
        mainCharacter = new Character(CharacterRace.ELF);

        mainCharacter.attack(worstCharacter, 19);

        assertHpLost(2, worstCharacter);

        mainCharacter.attack(worstCharacter, 20);

        assertHpLost(4, worstCharacter);
    }

    @Test
    public void elfHasPlus2ArmorClassWhenAttackedByOrcs() throws Exception {
        mainCharacter = new Character(CharacterRace.HUMAN);
        worstCharacter = new Character(CharacterRace.ORC);

        worstCharacter.attack(mainCharacter, 9); // orcs have +2 to attack rolls
        assertHpLost(3, mainCharacter);

        mainCharacter = new Character(CharacterRace.ELF);

        worstCharacter.attack(mainCharacter, 9);
        assertUndamaged(mainCharacter);

        worstCharacter.attack(mainCharacter, 10);
        assertUndamaged(mainCharacter);

        worstCharacter.attack(mainCharacter, 11);
        assertHpLost(3, mainCharacter); // orcs have +2 str mod
    }

    @Test
    public void halflingModifiers() throws Exception {
        // +1 to Dexterity Modifier, -1 to Strength Modifier
        mainCharacter = new Character(CharacterRace.HALFLING);
        assertEquals(1, mainCharacter.getDexterityModifier());
        assertEquals(-1, mainCharacter.getStrengthModifier());
    }

    @Test
    public void halflingsHavePlus2ArmorClassWhenAttackedByNonHalflings() throws Exception {
        mainCharacter = new Character(CharacterRace.HALFLING);
        worstCharacter.attack(mainCharacter, 11); // halflings have +1 dex, so AC is 11
        assertUndamaged(mainCharacter);

        worstCharacter = new Character(CharacterRace.HALFLING);
        worstCharacter.attack(mainCharacter, 12); //
        assertHpLost(1, mainCharacter);
    }

    @Test(expected = IllegalStateException.class)
    public void halflingsCannotHaveEvilAlignment() throws Exception {
        mainCharacter = new Character(CharacterRace.HALFLING);
        mainCharacter.setAlignment(Alignment.EVIL);
    }

    @Test
    public void longSwordHasBaseDamageOf5() throws Exception {
        Character mainCharacter = this.mainCharacter;
        mainCharacter.equip(new Longsword());
        this.mainCharacter.attack(worstCharacter, 10);
        assertHpLost(5, worstCharacter);
    }

    @Test
    public void plus2WaraxeHasBaseDamageOfSixAndAdds2toAttackAndDamage() throws Exception {
        mainCharacter.equip(new WarAxePlus2());
        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(8, worstCharacter);
    }

    @Test
    public void plus2WaraxeHasTripleDamageOnCriticalForNonRogue() throws Exception {
        mainCharacter.equip(new WarAxePlus2());
        mainCharacter.attack(worstCharacter, 20);
        assertHpLost(24, worstCharacter);
    }

    @Test
    public void plus2WaraxeHasQuadrupleDamageOnCriticalForRogue() throws Exception {
        mainCharacter = new Character(CharacterClass.ROGUE);
        Character mainCharacter = this.mainCharacter;
        mainCharacter.equip(new WarAxePlus2());
        this.mainCharacter.attack(worstCharacter, 20);
        assertHpLost(32, worstCharacter);
    }

    @Test
    public void elvenLongswordHasBaseDamageOfFiveWithPlus1ToAttackAndDamage() throws Exception {
        mainCharacter.equip(new ElvenLongsword());
        mainCharacter.attack(worstCharacter, 9);
        assertHpLost(6, worstCharacter);
    }

    @Test
    public void elvenLongSwordhasPlus2ToAttackAndDamageWhenWieldedByAnElf() throws Exception {
        mainCharacter = new Character(CharacterRace.ELF);
        mainCharacter.equip(new ElvenLongsword());
        mainCharacter.attack(worstCharacter, 8);
        assertHpLost(7, worstCharacter);
    }

    @Test
    public void elvenLongSwordhasPlus2ToAttackAndDamageWhenWieldedAgainstAnOrc() throws Exception {
        worstCharacter = new Character(CharacterRace.ORC);
        mainCharacter.equip(new ElvenLongsword());
        mainCharacter.attack(worstCharacter, 10); // orcs have 12 ac
        assertHpLost(7, worstCharacter);
    }

    @Test
    public void elvenLongSwordhasPlus5ToAttackAndDamageWhenWieldedByAnElfAgainstAnOrc() throws Exception {
        mainCharacter = new Character(CharacterRace.ELF);
        worstCharacter = new Character(CharacterRace.ORC);
        Character mainCharacter = this.mainCharacter;
        mainCharacter.equip(new ElvenLongsword());
        this.mainCharacter.attack(worstCharacter, 7); // orcs have 12 ac
        assertHpLost(10, worstCharacter);
    }

    @Test
    public void nunchucksDoSixPointsOfDamage() throws Exception {
        this.mainCharacter.equip(new Nunchucks());
        mainCharacter.attack(worstCharacter, 16);
        assertHpLost(6, worstCharacter);
    }

    @Test
    public void nunchucksHaveMinus4PenaltyToAttackWhenUsedByNonMonk() throws Exception {
        this.mainCharacter.equip(new Nunchucks());
        mainCharacter.attack(worstCharacter, 13);
        assertUndamaged(worstCharacter);

        mainCharacter = new Character(CharacterClass.MONK);
        mainCharacter.equip(new Nunchucks());
        mainCharacter.attack(worstCharacter, 10);
        assertHpLost(6, worstCharacter);
    }

    @Test
    public void leatherArmorAddsTwoToArmorClass() throws Exception {
        mainCharacter.equip(new LeatherArmor());
        worstCharacter.attack(mainCharacter, 11);
        assertUndamaged(mainCharacter);
        worstCharacter.attack(mainCharacter, 13);
        assertHpLost(1, mainCharacter);
    }

    @Test
    public void plateArmorAddsEightToArmorClass() throws Exception {
        mainCharacter = new Character(CharacterRace.DWARF);
        mainCharacter.equip(new PlateArmor());
        worstCharacter.attack(mainCharacter, 17);
        assertUndamaged(mainCharacter);
        worstCharacter.attack(mainCharacter, 18);
    }

    @Test(expected = IllegalStateException.class)
    public void mustBeDwarfOrFighterToWearPlateArmor() throws Exception {
        mainCharacter.equip(new PlateArmor());
    }

    @Test
    public void dwarfCanWearPlateArmor() throws Exception {
        mainCharacter = new Character(CharacterRace.DWARF);
        mainCharacter.equip(new PlateArmor());
    }

    @Test
    public void fighterCanWearPlateArmor() throws Exception {
        mainCharacter = new Character(CharacterClass.FIGHTER);
        mainCharacter.equip(new PlateArmor());
    }

    private void assertXpGained(int xp, Character mainCharacter) {
        assertEquals(xp, mainCharacter.getXP());
    }

    private void assertHpLost(int hpLost, Character character) {
        String message = "Expected character to lose " + hpLost + " hitpoints, but lost " + (character.getMaxHitPoints() - character.hitPoints);
        assertEquals(message, character.getMaxHitPoints() - hpLost, character.hitPoints);
    }

    private void assertUndamaged(Character character) {
        assertHpLost(0, character);
    }

    private void levelUp(Character character, int levels) {
        for (int i = 0; i < levels * 100; i++)
            character.attack(new Character(), 10);
    }

}