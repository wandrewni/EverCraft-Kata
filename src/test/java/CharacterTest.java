import org.junit.Test;
import org.junit.Assert;

public class CharacterTest
{
	@Test
	public void testCanSetName(){
	
		Character mainCharacter = new Character();
		mainCharacter.setName("Jandrew");
		mainCharacter.getName();
		
		Assert.assertEquals("Jandrew", mainCharacter.getName());
	}
}