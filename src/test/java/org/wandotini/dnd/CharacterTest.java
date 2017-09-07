package org.wandotini.dnd;

import org.junit.Before;
import org.junit.Test;
import org.wandotini.dnd.armor.ElvenChainmail;
import org.wandotini.dnd.armor.LeatherArmor;
import org.wandotini.dnd.armor.LeatherArmorOfDamageReduction;
import org.wandotini.dnd.armor.PlateArmor;
import org.wandotini.dnd.items.AmuletOfTheHeavens;
import org.wandotini.dnd.items.BeltOfGiantStrength;
import org.wandotini.dnd.items.RingOfProtection;
import org.wandotini.dnd.weapons.ElvenLongsword;
import org.wandotini.dnd.weapons.Longsword;
import org.wandotini.dnd.weapons.Nunchucks;
import org.wandotini.dnd.weapons.WarAxePlus2;

import static org.junit.Assert.assertEquals;

public class CharacterTest {
    private Character defender;
    private Character attacker;

    @Before
    public void setup() {
        defender = new Character();
        attacker = new Character();
    }

    @Test
    public void testCanSetNGetName() {
        defender.setName("Jandrew");

        assertEquals("Jandrew", defender.getName());
    }

    @Test
    public void testCanSetNGetAlignment() {  //uses the enum you made in Alignment.java
        defender.setAlignment(Alignment.EVIL);

        assertEquals(Alignment.EVIL, defender.getAlignment());
    }

    @Test
    public void testVerifyArmorNHitPointValue() {
        assertEquals(10, defender.baseArmorClass);
        assertEquals(5, defender.hitPoints);
    }

    @Test
    public void testStrongAttack() {
        assertSuccessfulAttack(19, 1);
    }

    @Test
    public void testWeakAttack() {
        assertFailedAttack(3);
    }

    @Test
    public void testPerfectAttack() {
        assertSuccessfulAttack(20, 2);
    }

    @Test
    public void abilityScoresDefaultToTen() {
        assertEquals(10, defender.getStrength());
        assertEquals(10, defender.getDexterity());
        assertEquals(10, defender.getConstitution());
        assertEquals(10, defender.getWisdom());
        assertEquals(10, defender.getIntelligence());
        assertEquals(10, defender.getCharisma());
    }

    @Test
    public void strengthModifierIsAddedToAttackRollAndDamage() throws Exception {
        attacker = new Character(17, 10, 10, 10, 10, 10);

        assertSuccessfulAttack(7, 4);
    }

    @Test
    public void strengthModifierIsDoubledForDamageOnCrit() throws Exception {
        attacker = new Character(17, 10, 10, 10, 10, 10);

        assertSuccessfulAttack(20, 8);
    }

    @Test
    public void attackCannotBeModifiedBelowOne() throws Exception {
        attacker = new Character(1, 10, 10, 10, 10, 10);

        assertSuccessfulAttack(19,1);

        defender = new Character();

        assertSuccessfulAttack(20,1);
    }

    @Test
    public void dexterityModifierIsAddedToArmorClassForDeterminingHit() throws Exception {
        defender = new Character(10, 14, 10, 10, 10, 10);
        attacker = new Character(10, 4, 10, 10, 10, 10);

        assertFailedAttack(11);

        defender = new Character(10, 4, 10, 10, 10, 10);

        assertSuccessfulAttack(7, 1);
    }

    @Test
    public void constitutionModifierIsAddedToHp() throws Exception {
        defender = new Character(10, 10, 16, 10, 10, 10);
        assertEquals(8, defender.hitPoints);
        defender = new Character(10, 10, 4, 10, 10, 10);
        assertEquals(2, defender.hitPoints);
    }

    @Test
    public void startingHpCannotBeReducedBelowOne() throws Exception {
        defender = new Character(10, 10, 1, 10, 10, 10);
        assertEquals(1, defender.hitPoints);
    }

    @Test
    public void characterGainsExperienceWhenHitting() throws Exception {
        assertXpGained(0, defender);
        defender.attack(attacker, 10);
        assertXpGained(10, defender);
        defender.attack(attacker, 9);
        assertXpGained(10, defender);

        // gains on crit
        defender.attack(attacker, 20);
        assertXpGained(20, this.defender);
    }

    @Test
    public void characterLevelsUpEveryThousandXp() throws Exception {
        assertEquals(1, defender.getLevel());
        for (int i = 0; i < 100; i++)
            defender.attack(new Character(), 10);
        assertEquals(2, defender.getLevel());
        for (int i = 0; i < 100; i++)
            defender.attack(new Character(), 10);
        assertEquals(3, defender.getLevel());
    }

    @Test
    public void onLevelingUpGainHitpointsEqualToFivePlusConsModifier() throws Exception {
        assertEquals(5, defender.hitPoints);
        levelUp(defender, 1);
        assertEquals(10, defender.hitPoints);

        defender = new Character(10, 10, 16, 10, 10, 10);
        assertEquals(8, defender.hitPoints);
        levelUp(defender, 1);
        assertEquals(16, defender.hitPoints);
    }

    @Test
    public void evenCharactersWithWorstConstitutionGainOneHp() throws Exception {
        defender = new Character(10, 10, 1, 10, 10, 10);
        assertUndamaged(defender);
        levelUp(defender, 1);
        assertEquals(2, defender.hitPoints);
    }

    @Test
    public void forEveryEvenLevelAdd1ToAttackRolls() throws Exception {
        assertFailedAttack(9);

        levelUp(attacker, 1);

        assertSuccessfulAttack(9, 1);

        levelUp(attacker, 2);
        defender = new Character();

        assertSuccessfulAttack(8, 1);
    }

    @Test
    public void fighterHasTenHitPointsPerLevel() throws Exception {
        defender = new Character(CharacterClass.FIGHTER);
        assertEquals(10, defender.getMaxHitPoints());
        levelUp(defender, 1);
        assertEquals(20, defender.getMaxHitPoints());
    }

    @Test
    public void fighterGetsPlusOneToAttackEachLevel() throws Exception {
        attacker = new Character(CharacterClass.FIGHTER);

        assertFailedAttack(9);

        levelUp(attacker, 1);

        assertSuccessfulAttack(9, 1);

        levelUp(attacker, 1);
        defender = new Character();

        assertSuccessfulAttack(8, 1);
    }

    @Test
    public void rogueGetsTripleDamageOnCriticalHits() throws Exception {
        attacker = new Character(CharacterClass.ROGUE);

        assertSuccessfulAttack(20, 3);
    }

    @Test
    public void rogueAddsDexterityModifierInsteadOfStrengthModifierToAttackRolls() throws Exception {
        attacker = new Character(CharacterClass.ROGUE, 10, 16, 10, 10, 10, 10);

        assertSuccessfulAttack(7, 1);
    }

    @Test
    public void rogueDoesntUseStrengthModifierForAttackRolls() throws Exception {
        attacker = new Character(CharacterClass.ROGUE, 16, 10, 10, 10, 10, 10);

        assertFailedAttack(7);
    }

    @Test
    public void rogueIgnoresOpponentDexterityModifierForArmorClassIfPositive() throws Exception {
        attacker = new Character(CharacterClass.ROGUE);
        defender = new Character(10, 16, 10, 10, 10, 10);

        assertSuccessfulAttack(10, 1);
    }

    @Test
    public void rogueTakesAdvantageOfOpponentDexterityModifierForArmorClassIfNegative() throws Exception {
        attacker = new Character(CharacterClass.ROGUE);
        defender = new Character(10, 4, 10, 10, 10, 10);

        assertSuccessfulAttack(7, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void roguesCannotHaveGoodAlignment() throws Exception {
        attacker = new Character(CharacterClass.ROGUE);
        attacker.setAlignment(Alignment.GOOD);
    }

    @Test
    public void monkGains6HitPointsPerlevel() throws Exception {
        defender = new Character(CharacterClass.MONK);
        assertEquals(6, defender.getMaxHitPoints());
        levelUp(defender, 1);
        assertEquals(12, defender.getMaxHitPoints());
    }

    @Test
    public void monkDoes3DamageOnSuccessfulHitAnd6onSuccessfulCrit() throws Exception {
        attacker = new Character(CharacterClass.MONK);
        assertSuccessfulAttack(10, 3);

        defender = new Character();
        assertSuccessfulAttack(20, 6);
    }

    @Test
    public void monkAddsWisdomToDexterityModifierForDefending() throws Exception {
        defender = new Character(CharacterClass.MONK, 10, 14, 10, 14, 10, 10);
        assertFailedAttack(13);

        attacker = new Character(CharacterClass.ROGUE);

        assertFailedAttack(11);

        assertSuccessfulAttack(12, 1);
    }

    @Test
    public void monkDoesntAddNegativeWisdomToDefendingModifier() throws Exception {
        defender = new Character(CharacterClass.MONK, 10, 10, 10, 1, 10, 10);

        assertFailedAttack(9);
    }

    @Test
    public void monkAttackRollIsIncreasedEverySecondAndThirdLevels() throws Exception {
        attacker = new Character(CharacterClass.MONK);

        assertFailedAttack(9);

        levelUp(attacker, 1);
        assertSuccessfulAttack(9, 3);

        levelUp(attacker, 1);
        assertSuccessfulAttack(8, 6);

        attacker = new Character(CharacterClass.MONK);
        defender = new Character();
        levelUp(attacker, 5);

        assertFailedAttack(5);
        assertSuccessfulAttack(6, 3);
    }

    @Test
    public void paladinGains8hitPointsPerLevel() throws Exception {
        defender = new Character(CharacterClass.PALADIN);
        assertEquals(8, defender.getMaxHitPoints());
        levelUp(defender, 1);
        assertEquals(16, defender.getMaxHitPoints());
    }

    @Test
    public void paladinHasPlus2ToDamageWhenAttackingEvilCharacters() throws Exception {
        attacker = new Character(CharacterClass.PALADIN);

        assertSuccessfulAttack(10, 1);

        defender = new Character();
        defender.setAlignment(Alignment.EVIL);

        assertSuccessfulAttack(10, 3);
    }

    @Test
    public void paladinHasPlus2ToRollsWhenAttackingEvilCharacters() throws Exception {
        attacker = new Character(CharacterClass.PALADIN);

        assertFailedAttack(9);

        defender = new Character();
        defender.setAlignment(Alignment.EVIL);

        assertSuccessfulAttack(9, 3);

        defender = new Character();
        defender.setAlignment(Alignment.EVIL);

        assertSuccessfulAttack(8, 3);
    }

    @Test
    public void paladinDoesTripleDamageOnCritAgainstEvil() throws Exception {
        attacker = new Character(CharacterClass.PALADIN);

        // normal crit
        assertSuccessfulAttack(20, 2);

        // evil crit
        defender = new Character();
        defender.setAlignment(Alignment.EVIL);
        assertSuccessfulAttack(20, 9);
    }

    @Test
    public void paladinGetsPlusOneToAttackEachLevel() throws Exception {
        attacker = new Character(CharacterClass.PALADIN);
        assertFailedAttack(9);

        levelUp(attacker, 1);
        assertSuccessfulAttack(9, 1);

        levelUp(attacker, 1);
        assertSuccessfulAttack(8, 2);
    }

    @Test
    public void paladinDefaultsToGoodAlignment() throws Exception {
        defender = new Character(CharacterClass.PALADIN);
        assertEquals(Alignment.GOOD, defender.getAlignment());
    }

    @Test(expected = IllegalStateException.class)
    public void paladinRestrictedToGoodAlignment() throws Exception {
        defender = new Character(CharacterClass.PALADIN);
        defender.setAlignment(Alignment.NEUTRAL);
    }

    @Test
    public void charactersDefaultToHuman() throws Exception {
      defender = new Character();
      assertEquals(CharacterRace.HUMAN, defender.getRace());
    }

    @Test
    public void orcModifiers() throws Exception {
        defender = new Character(CharacterRace.ORC);
        assertEquals(2, defender.getStrengthModifier());
        assertEquals(-1, defender.getIntelligenceModifier());
        assertEquals(-1, defender.getWisdomModifier());
        assertEquals(-1, defender.getCharismaModifier());
    }

    @Test
    public void orcsHavePlusTwoToArmorClass() throws Exception {
        defender = new Character(CharacterRace.ORC);
        assertFailedAttack(10);
        assertSuccessfulAttack(12, 1);
    }

    @Test
    public void dwarfModifiers() throws Exception {
        defender = new Character(CharacterRace.DWARF);
        assertEquals(1, defender.getConstitutionModifier());
        assertEquals(-1, defender.getCharismaModifier());
    }

    @Test
    public void dwarfConstitutionModifierIsDoubledForHitPointGainIfPositive() throws Exception {
        defender = new Character(CharacterRace.DWARF, 10, 10, 12, 10, 10, 10);
        // doubled (1 basemod + 1 racemod)
        assertEquals(9, defender.getMaxHitPoints());

        defender = new Character(CharacterRace.DWARF, 10, 10, 6, 10, 10, 10);
        // not doubled (-2 basemod + 1 racemod)
        assertEquals(4, defender.getMaxHitPoints());
    }

    @Test
    public void dwarfHasPlus2ToAttackRollAndDamageVersusOrc() throws Exception {
        attacker = new Character(CharacterRace.HUMAN);
        defender = new Character(CharacterRace.ORC);
        assertFailedAttack(10);

        attacker = new Character(CharacterRace.DWARF);

        assertSuccessfulAttack(10, 3);

        assertSuccessfulAttack(20, 9);
    }

    @Test
    public void elfModifiers() throws Exception {
        defender = new Character(CharacterRace.ELF);
        assertEquals(1, defender.getDexterityModifier());
        assertEquals(-1, defender.getConstitutionModifier());
    }

    @Test
    public void elfHasPlusOneToCriticalRange() throws Exception {
        attacker = new Character(CharacterRace.ELF);

        assertSuccessfulAttack(19, 2);

        assertSuccessfulAttack(20, 4);
    }

    @Test
    public void elfHasPlus2ArmorClassWhenAttackedByOrcs() throws Exception {
        defender = new Character(CharacterRace.HUMAN);
        attacker = new Character(CharacterRace.ORC);

        assertSuccessfulAttack(9, 3);

        defender = new Character(CharacterRace.ELF);

        assertFailedAttack(9);
        assertFailedAttack(10);

        assertSuccessfulAttack(11, 3);
    }

    @Test
    public void halflingModifiers() throws Exception {
        // +1 to Dexterity Modifier, -1 to Strength Modifier
        defender = new Character(CharacterRace.HALFLING);
        assertEquals(1, defender.getDexterityModifier());
        assertEquals(-1, defender.getStrengthModifier());
    }

    @Test
    public void halflingsHavePlus2ArmorClassWhenAttackedByNonHalflings() throws Exception {
        defender = new Character(CharacterRace.HALFLING);
        assertFailedAttack(11);

        attacker = new Character(CharacterRace.HALFLING, 12, 10, 10, 10, 10, 10);
        assertSuccessfulAttack(12, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void halflingsCannotHaveEvilAlignment() throws Exception {
        defender = new Character(CharacterRace.HALFLING);
        defender.setAlignment(Alignment.EVIL);
    }

    @Test
    public void longSwordHasBaseDamageOf5() throws Exception {
        attacker.equip(new Longsword());
        assertSuccessfulAttack(10, 5);
    }

    @Test
    public void plus2WaraxeHasBaseDamageOfSixAndAdds2toAttackAndDamage() throws Exception {
        attacker.equip(new WarAxePlus2());
        assertSuccessfulAttack(8, 8);
    }

    @Test
    public void plus2WaraxeHasTripleDamageOnCriticalForNonRogue() throws Exception {
        attacker.equip(new WarAxePlus2());
        assertSuccessfulAttack(20, 24);
    }

    @Test
    public void plus2WaraxeHasQuadrupleDamageOnCriticalForRogue() throws Exception {
        attacker = new Character(CharacterClass.ROGUE);
        attacker.equip(new WarAxePlus2());
        assertSuccessfulAttack(20, 32);
    }

    @Test
    public void elvenLongswordHasBaseDamageOfFiveWithPlus1ToAttackAndDamage() throws Exception {
        attacker.equip(new ElvenLongsword());
        assertSuccessfulAttack(9, 6);
    }

    @Test
    public void elvenLongSwordhasPlus2ToAttackAndDamageWhenWieldedByAnElf() throws Exception {
        attacker = new Character(CharacterRace.ELF);
        attacker.equip(new ElvenLongsword());
        assertSuccessfulAttack(8, 7);
    }

    @Test
    public void elvenLongSwordhasPlus2ToAttackAndDamageWhenWieldedAgainstAnOrc() throws Exception {
        defender = new Character(CharacterRace.ORC);
        attacker.equip(new ElvenLongsword());
        assertSuccessfulAttack(10, 7);
    }

    @Test
    public void elvenLongSwordhasPlus5ToAttackAndDamageWhenWieldedByAnElfAgainstAnOrc() throws Exception {
        attacker = new Character(CharacterRace.ELF);
        defender = new Character(CharacterRace.ORC);
        attacker.equip(new ElvenLongsword());
        assertSuccessfulAttack(7, 10);
    }

    @Test
    public void nunchucksDoSixPointsOfDamage() throws Exception {
        attacker.equip(new Nunchucks());
        assertSuccessfulAttack(16, 6);
    }

    @Test
    public void nunchucksHaveMinus4PenaltyToAttackWhenUsedByNonMonk() throws Exception {
        attacker.equip(new Nunchucks());
        assertFailedAttack(13);

        attacker = new Character(CharacterClass.MONK);
        attacker.equip(new Nunchucks());
        assertSuccessfulAttack(10, 6);
    }

    @Test
    public void leatherArmorAddsTwoToArmorClass() throws Exception {
        defender.equip(new LeatherArmor());
        assertFailedAttack(11);
        assertSuccessfulAttack(13, 1);
    }

    @Test
    public void plateArmorAddsEightToArmorClass() throws Exception {
        defender = new Character(CharacterRace.DWARF);
        defender.equip(new PlateArmor());
        assertFailedAttack(17);
        attacker.attack(defender, 18);
    }

    @Test(expected = IllegalStateException.class)
    public void mustBeDwarfOrFighterToWearPlateArmor() throws Exception {
        defender.equip(new PlateArmor());
    }

    @Test
    public void dwarfCanWearPlateArmor() throws Exception {
        defender = new Character(CharacterRace.DWARF);
        defender.equip(new PlateArmor());
    }

    @Test
    public void fighterCanWearPlateArmor() throws Exception {
        defender = new Character(CharacterClass.FIGHTER);
        defender.equip(new PlateArmor());
    }

    @Test
    public void magicalLeatherOfDamageReductionAdds2toArmorClass() throws Exception {
        defender.equip(new LeatherArmorOfDamageReduction());
        attacker.equip(new Longsword());
        assertFailedAttack(10);
        assertFailedAttack(11);
        assertSuccessfulAttack(12, 3);
    }

    @Test
    public void magicalLeatherOfDamageReductionReducesDamageBy2() throws Exception {
        defender.equip(new LeatherArmorOfDamageReduction());
        assertFailedAttack(12);
        assertFailedAttack(20);

        attacker.equip(new Longsword()); // +5 to damage
        assertSuccessfulAttack(15, 3);
    }

    @Test
    public void elvenChainmailAddsPlus5ToArmorClassForNonElves() throws Exception {
        defender.equip(new ElvenChainmail());
        assertFailedAttack(14);
        assertSuccessfulAttack(15, 1);
    }

    @Test
    public void elvenChainmailAddsPlus8ToArmorClassForElves() throws Exception {
        defender = new Character(CharacterRace.ELF);
        defender.equip(new ElvenChainmail());
        assertFailedAttack(18); // elves have +1 AC
        assertSuccessfulAttack(19, 1);
    }

    @Test
    public void elvenChainmailGivesElvesPlus1ToAttack() throws Exception {
        attacker.equip(new ElvenChainmail());
        assertFailedAttack(9);

        attacker = new Character(CharacterRace.ELF);
        attacker.equip(new ElvenChainmail());

        assertSuccessfulAttack(9, 1);
    }

    @Test
    public void shieldAddsPlus3ToArmorClass() throws Exception {
        defender.equip(new Shield());
        assertFailedAttack(12);
    }

    @Test
    public void shieldReducesAttackBy4() throws Exception {
        attacker.equip(new Shield());
        assertFailedAttack(13);

        assertSuccessfulAttack(14, 1);
    }

    @Test
    public void shieldReducesAttackBy2ForFighters() throws Exception {
        attacker = new Character(CharacterClass.FIGHTER);
        attacker.equip(new Shield());
        assertFailedAttack(11);
        assertSuccessfulAttack(12, 1);
    }

    @Test
    public void canEquipMultipleItemsThatGiveACBonuses() throws Exception {
        defender.equip(new RingOfProtection());
        assertFailedAttack(11);
        defender.equip(new RingOfProtection());
        assertFailedAttack(13);
    }

    @Test
    public void beltOfGiantStrengthAddsFourToStrength() throws Exception {
        defender.equip(new BeltOfGiantStrength());
        defender.equip(new RingOfProtection());
        assertEquals(defender.getStrength(), 14);
    }

    @Test
    public void amuletOfTheHeavensAddsOneToAttackAgainstNeutralEnemiesAndTwoAgainstEvilEnemies() throws Exception {
        attacker.equip(new AmuletOfTheHeavens());
        defender.setAlignment(Alignment.GOOD);

        assertFailedAttack(8);
        assertFailedAttack(9);

        defender.setAlignment(Alignment.NEUTRAL);

        assertSuccessfulAttack(9, 1);

        defender = new Character();
        defender.setAlignment(Alignment.EVIL);

        assertSuccessfulAttack(8, 1);
    }

    @Test
    public void amuletOfTheHeavensBonusIsDoubledForPaladin() throws Exception {
        attacker = new Character(CharacterClass.PALADIN);
        attacker.equip(new AmuletOfTheHeavens());
        defender.setAlignment(Alignment.GOOD);
        assertFailedAttack(6);
        assertFailedAttack(7);

        defender.setAlignment(Alignment.NEUTRAL);

        assertSuccessfulAttack(8, 1);

        defender = new Character();
        defender.setAlignment(Alignment.EVIL);

        assertSuccessfulAttack(6, 3);
    }

    private void assertSuccessfulAttack(int dieRoll, int expectedDamage) {
        attacker.attack(defender, dieRoll);
        assertHpLost(expectedDamage, defender);
    }

    private void assertFailedAttack(int roll) {
        attacker.attack(defender, roll);
        assertUndamaged(defender);
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