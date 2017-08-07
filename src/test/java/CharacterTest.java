import org.junit.Test; //to use @Test
import org.junit.Before;
import org.junit.Assert; //to use assertEquals to make sure things are equal

import static org.junit.Assert.assertEquals;

public class CharacterTest {

    private static final int BASE_HP = 5;
    private Character mainCharacter;
	private Character worstCharacter;
	
	@Before
	public void setup(){
		
		mainCharacter = new Character();
		worstCharacter = new Character();
		
	}

	@Test
	public void testCanSetNGetName(){
		
		mainCharacter.setName("Jandrew");
		
		assertEquals("Jandrew", mainCharacter.getName());
	}
	
	@Test
	public void testCanSetNGetAlignment(){  //uses the enum you made in Alignment.java
		
		mainCharacter.setAlignment(Alignment.EVIL);
		
		assertEquals(Alignment.EVIL, mainCharacter.getAlignment());
	}
	
	@Test
	public void testVerifyArmorNHitPointValue(){
	
		assertEquals(10, mainCharacter.baseArmorClass);
		assertUndamaged(mainCharacter);

	}
	
	@Test
	public void testStrongAttack(){
	
		int dieRoll = 19;
		
		mainCharacter.attack(worstCharacter,dieRoll);

		assertHpLost(1, worstCharacter);

	}
	
	@Test
	public void testWeakAttack(){
	
		int dieRoll = 3;
		
		mainCharacter.attack(worstCharacter,dieRoll);

		assertUndamaged(worstCharacter);

	}
	
	@Test
	public void testPerfectAttack(){
	
		int dieRoll = 20;
		
		mainCharacter.attack(worstCharacter,dieRoll);

		assertHpLost(2, worstCharacter);

	}

	@Test
	public void abilityScoresDefaultToTen(){
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
		mainCharacter = new Character(10,14, 10, 10, 10, 10);
		worstCharacter = new Character(10,4, 10, 10, 10, 10);

		worstCharacter.attack(mainCharacter, 11);

		assertUndamaged(this.mainCharacter);

		this.mainCharacter.attack(worstCharacter, 7);

		assertHpLost(1, this.worstCharacter);
	}

	@Test
	public void constitutionModifierIsAddedToHp() throws Exception {
		mainCharacter = new Character(10, 10, 16,10,10,10);
		assertEquals(8, mainCharacter.hitPoints);
        mainCharacter = new Character(10, 10, 4,10,10,10);
        assertEquals(2, mainCharacter.hitPoints);
	}

    @Test
    public void startingHpCannotBeReducedBelowOne() throws Exception {
        mainCharacter = new Character(10, 10, 1,10,10,10);
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

	private void assertXpGained(int xp, Character mainCharacter) {
		assertEquals(xp, mainCharacter.getXP());
	}

	private void assertHpLost(int expected, Character character) {
		assertEquals(BASE_HP - expected, character.hitPoints);
	}

	private void assertUndamaged(Character character) {
		assertHpLost(0, character);
	}
}