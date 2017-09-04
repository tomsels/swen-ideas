import java.awt.Color;
import java.util.Random;

public class HunterNode {

	private int health;
	private Model m;
	private int[][] pixels;
	private int y, x;
	private int width, height;
	int step = 0;
	
	
	public HunterNode(int y, int x, Model m){
		this.m = m;
		this.pixels = m.pixels;
		this.y = y;
		this.x = x;
		this.width = m.width;
		this.height = m.height;
		this.health = 2000;
		
	}
	
	public void ping(){
		
		
		
		// step added to slow down the hunter
		if(step > 3){ 
			
			this.health--;
			if(this.health < 0){ m.getHunters().remove(this); }
						
			pixels[y][x] = 0;
			step = 0; 
			Node n = findNode();
			if(n != null){ moveTowardsNode(n); }
			else{ moveRandom(); }		
			pixels[y][x] = 3;
			
		}
		step++;
		
	}
	
	
	private void moveTowardsNode(Node n){
		int distance = (int) Math.pow( (Math.pow(n.getY() - this.y, 2) + Math.pow(n.getX() - this.x, 2)), 0.5);
		
		if(distance < 3){
			// take a chomp on node
			this.health+=2;
			if(this.health > 2200){ m.createHunter(y, x); this.health-=400; }
		}
		else{
			
			int newX = x;
			int newY = y;
			
			if(n.getX() < this.x){ newX = x-1; }
			if(n.getX() > this.x){ newX = x+1; }
			
			if(n.getY() < this.y){ newY = y-1; }
			if(n.getY() > this.y){ newY = y+1; }
			
			x = newX; y = newY; 
		}			
	}
	
	/**
	 * Finds the closest fruit, which can be eaten
	 * 
	 * @return the closest found fruit ( null if none within range )
	 */
	private Node findNode(){
		
		int maxRange = 45;
		int minDistance = 9999;
		Node closest = null;
		for(Node n : m.getNodes()){
			
			
			int distance = (int) Math.pow( (Math.pow(n.getY() - this.y, 2) + Math.pow(n.getX() - this.x, 2)), 0.5);
			if(distance < maxRange){ 
				if(distance < minDistance){
					minDistance = distance;
					closest = n;
				}						
			}
							
		}			
		return closest;
	}
	
	
	private void moveRandom(){
		Random random = new Random();
		
		if(random.nextBoolean()){ 
			this.y++; 
			if(y > height-1){ y = height - 1; }
		}
		else{ 
			this.y--; 
			if(y < 0){ y = 0; }
		}			
		
		if(random.nextBoolean()){ 
			this.x++; 
			if(x > width-1){ x = width - 1; }
		}
		else{ 
			this.x--; 
			if(x < 0){ x = 0; }
		}	
	}
	
	public void isAttacked(int amount){ 
		this.health-= amount; 
		if(this.health < 0){ m.getHunters().remove(this); }
	}
	
	public int getX(){ return this.x; }
	public int getY(){ return this.y; }
	public int getHealth(){ return this.health; }
	
	
}
