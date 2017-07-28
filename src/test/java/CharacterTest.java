import org.junit.Test; //to use @Test
import org.junit.Before;
import org.junit.Assert; //to use assertEquals to make sure things are equal

public class CharacterTest {

	Character mainCharacter;
	Character worstCharacter;
	
	@Before
	public void setup(){
		
		mainCharacter = new Character();
		worstCharacter = new Character();
		
	}

	@Test
	public void testCanSetNGetName(){
		
		mainCharacter.setName("Jandrew");
		
		Assert.assertEquals("Jandrew", mainCharacter.getName());
	}
	
	@Test
	public void testCanSetNGetAlignment(){  //uses the enum you made in Alignment.java
		
		mainCharacter.setAlignment(Alignment.EVIL);
		
		Assert.assertEquals(Alignment.EVIL, mainCharacter.getAlignment());
	}
	
	@Test
	public void testVerifyArmorNHitPointValue(){
	
		Assert.assertEquals(10, mainCharacter.armorClass);
		Assert.assertEquals(5, mainCharacter.hitPoints);
	
	}
	
	@Test
	public void testStrongAttack(){
	
		int dieRoll = 19;
		
		mainCharacter.attack(worstCharacter,dieRoll);
		
		Assert.assertEquals(4, worstCharacter.hitPoints);
	
	}
	
	@Test
	public void testWeakAttack(){
	
		int dieRoll = 3;
		
		mainCharacter.attack(worstCharacter,dieRoll);
		
		Assert.assertEquals(5, worstCharacter.hitPoints);
	
	}
	
	@Test
	public void testPerfectAttack(){
	
		int dieRoll = 20;
		
		mainCharacter.attack(worstCharacter,dieRoll);
		
		Assert.assertEquals(3, worstCharacter.hitPoints);
	
	}
	
	
	
}