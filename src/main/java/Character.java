public class Character {

	private String name;
	private Alignment alignment; //enum you made in Alignment.java
	
	public int armorClass = 10;
	public int hitPoints = 5;
	
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
	
	public void attack(Character opponent, int dieRoll){
	
		if (dieRoll >= armorClass && dieRoll < 20)
			opponent.hitPoints--;
		else if (dieRoll == 20)
			opponent.hitPoints = opponent.hitPoints - 2;
	
	}
		
}