import org.junit.Test;
import org.junit.Assert;

public class CharacterTest {
	@Test
	public void testCanSetNGetName(){
	
		Character mainCharacter = new Character();
		
		mainCharacter.setName("Jandrew");
		
		Assert.assertEquals("Jandrew", mainCharacter.getName());
	}
	
	@Test
	public void testCanSetNGetAlignment(){
	
		Character worstCharacter = new Character();
		
		worstCharacter.setAlignment(Alignment.EVIL);
		
		Assert.assertEquals(Alignment.EVIL, worstCharacter.getAlignment());
		
			
		Character bestCharacter = new Character();
		
		bestCharacter.setAlignment(Alignment.GOOD);
		
		Assert.assertEquals(Alignment.GOOD, bestCharacter.getAlignment());
		
			
		Character dorkCharacter = new Character();
		
		dorkCharacter.setAlignment(Alignment.NEUTRAL);
		
		Assert.assertEquals(Alignment.NEUTRAL, dorkCharacter.getAlignment());
	}
}