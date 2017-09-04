
public class DNA {
	
	private int aggression;
	private int curiosity;
	private int mutationFactor = 2;
	private int birthThreshold;
	private int maxHealth;
	
	public DNA(int birthThreshold, int a){
		
		this.aggression = a + (int)(Math.random()*20 - 10);		
		this.birthThreshold = birthThreshold + (int)(Math.random()*50 - 25);
		this.maxHealth = 5000 - this.birthThreshold + (int)(Math.random()*500 - 50) - 1000;
	}
	
	public int getMaxHealth(){ return this.maxHealth; }
	public int getBirthThreshold(){ return this.birthThreshold; }
	public int getAggression(){ return this.aggression; }
}
