import java.awt.Color;
import java.util.Random;

public class Node{
		
		private int y, x;
		
		private int[][] pixels;
		private int height;
		private int width;
		private Model m;
		
		private int health;
		int rPink = 244;
		int gPink = 66;
		int bPink = 167;
		
		int rBlue = 65;
		int gBlue = 89;
		int bBlue = 244;
		
		int directionX;
		int directionY;
		private boolean directionMode;
		
		// DNA stores the nodes inherited characteristics
		private DNA dna;
		
		private int seenEnemyX;
		private int seenEnemyY;

		
		Node(int y, int x, Model m, DNA dna){
			this.y = y;
			this.x = x;
			this.pixels = m.pixels;
			this.height = m.height;
			this.width = m.width;
			this.m = m;
			pixels[y][x] = 1;
			this.directionMode = false; // when mouse is clicked, directionMode is activated
			
			this.dna = new DNA(dna.getBirthThreshold(), dna.getAggression());
			this.health = this.dna.getMaxHealth();
			
			
		}
		
		public DNA getDNA(){ return this.dna; }

		
		public void ping(){
		
			this.health--;
			// Death
			if(this.health < 0){ m.removeNode(this); }
			
			// If super healthy, generate offspring
			if(this.health > this.dna.getMaxHealth()){ m.createNode(y, x, this.dna); this.health-=500;}
			
			pixels[y][x] = 0;
			
			// When mouse is clicked, that takes the priority
			if(directionMode){ moveDirection(); }
			
			else{
				
				HunterNode h = findHunter(Math.abs( this.dna.getAggression() ));
				// If there is a nearby hunter and the node is scared, run away
				if(h != null && this.dna.getAggression() < 0){
					this.seenEnemyX = h.getX();
					this.seenEnemyY = h.getY();
					runAway(h); 					
				}
				// If there is a nearby hunter and the node is aggressive, attack
				else if(h != null && this.dna.getAggression() > 0 && h.getHealth() < 1500){
					this.seenEnemyX = h.getX();
					this.seenEnemyY = h.getY();
					attackHunter(h); 
				} 
				else{
					// If there is fruit which can be eaten nearby move towards it
					Fruit foundFruit = findFruit();
					if(foundFruit != null){ moveTowardsFruit(foundFruit); }
					// Otherwise move randomly
					else{ moveRandom(); }
				}
			}
			
			// Mark the nodes new position
			pixels[y][x] = 1;
			
			// Find adjacent enemies
			for(int yy = y-1; yy <= y+1; yy++){
				for(int xx = x-1; xx<=x+1; xx++){
					if(yy > 0 && xx > 0 && yy < height-1 && xx < width-1){
						if(this.pixels[yy][xx] == 2){
							if(this.health < 4000){ m.removeNode(this);}
							else{this.health-=1000;}
						}
						else if(this.pixels[yy][xx] == 3){
							m.removeNode(this);
						}
					}					
				}
			}			
		}
		
		public HunterNode findHunter(int range){
			
			int minDistance = 9999;
			HunterNode closest = null;
			for(HunterNode h : m.getHunters()){
				// Euclidean distance, of all hunter nodes, this needs to be more efficient				
				int distance = (int) Math.pow( (Math.pow(h.getY() - this.y, 2) + Math.pow(h.getX() - this.x, 2)), 0.5);
				if(distance < range*3){ 
					if(distance < minDistance){
						minDistance = distance;
						closest = h;
					}						
				}								
			}			
			return closest;			
		}
		
		public void runAway(HunterNode h){
			
			int distance = (int) Math.pow( (Math.pow(h.getY() - this.y, 2) + Math.pow(h.getX() - this.x, 2)), 0.5);
			
			if(distance < 5){ this.health-=10; }
			// Lazy check all for out of bounds
			else if(y-1 > 0 && x-1 > 0 && y+1 < height-1 && x+1 < width-1){
				
				int newX = x;
				int newY = y;
				
				if(h.getX() < this.x){ newX = x+1; }
				if(h.getX() > this.x){ newX = x-1; }
				
				if(h.getY() < this.y){ newY = y+1; }
				if(h.getY() > this.y){ newY = y-1; }
				
				this.x = newX; this.y = newY;				
			}	
			else{
				moveRandom();
			}
		}
		
		public void attackHunter(HunterNode h){
			
			int distance = (int) Math.pow( (Math.pow(h.getY() - this.y, 2) + Math.pow(h.getX() - this.x, 2)), 0.5);
			
			if(distance < 5){
				this.health-=500;
				h.isAttacked(100);
			}
			else if(y-1 > 0 && x-1 > 0 && y+1 < height-1 && x+1 < width-1){
				
				int newX = x;
				int newY = y;
				// Move in direction towards hunter
				if(h.getX() < this.x){ newX = x-1; }
				if(h.getX() > this.x){ newX = x+1; }				
				if(h.getY() < this.y){ newY = y-1; }
				if(h.getY() > this.y){ newY = y+1; }
				
				this.x = newX; this.y = newY;				
			}	
			else{
				moveRandom();
			}
		}
		
		/**
		 * Moves in the direction of a mouse click if within range
		 * @param y
		 * @param x
		 */
		public void manualDirection(int y, int x){
			this.directionX = x;
			this.directionY = y;
			
			int distance = (int) Math.pow( (Math.pow(directionY - this.y, 2) + Math.pow(directionX - this.x, 2)), 0.5);			
			if(distance < 75){ directionMode = true; }
		}
		
		public void moveDirection(){
			
			int distance = (int) Math.pow( (Math.pow(directionY - this.y, 2) + Math.pow(directionX - this.x, 2)), 0.5);
			
			if(distance < 5){ directionMode = false; }
			else{
				
				if(y-1 > 0 && x-1 > 0 && y+1 < height-1 && x+1 < width-1){
				
					int newX = x;
					int newY = y;
					// Move in direction towards mouse click
					if(directionX < this.x){ newX = x-1; }
					if(directionX > this.x){ newX = x+1; }
					
					if(directionY < this.y){ newY = y-1; }
					if(directionY > this.y){ newY = y+1; }
					
					// To stop the nodes from crowing round in the same spot, next to fruit
					// if there is a larger distance between x or y pick the biggest
					
					// This is quite messy, could be done elegantly?
					if(pixels[newY][newX] == 0){
												
						if( Math.abs(directionY - y) > Math.abs(directionX - x)){
							if(directionY < y){
								if(pixels[y-1][x] == 0){y--;}
								else if(pixels[y-1][x+1] == 0){x++; y--;}
								else if(pixels[y-1][x-1] == 0){x--; y--;}
							}
							else{
								if(pixels[y+1][x] == 0){y++;}
								else if(pixels[y+1][x+1] == 0){x++; y++;}
								else if(pixels[y+1][x-1] == 0){x--; y++;}
							}
						}
						else{
							if(directionX < x){
								if(pixels[y][x-1] == 0){x--;}
								else if(pixels[y-1][x-1] == 0){x--; y--;}
								else if(pixels[y+1][x-1] == 0){x--; y++;}
							}
							else{
								if(pixels[y][x+1] == 0){x++;}
								else if(pixels[y+1][x+1] == 0){x++; y++;}
								else if(pixels[y-1][x+1] == 0){x++; y--;}
							}
						}
					}									
				}
				else{ moveRandom(); }
			}
			
		}
		
		private void moveTowardsFruit(Fruit f){
			// Euclidean distance
			int distance = (int) Math.pow( (Math.pow(f.getY() - this.y, 2) + Math.pow(f.getX() - this.x, 2)), 0.5);
			
			if(distance < 5){
				f.eatFruit();
				//this.health+= f.feed(this);
				this.health+=2;
			}
			else{				
				int newX = x;
				int newY = y;
				
				if(f.getX() < this.x){ newX = x-1; }
				if(f.getX() > this.x){ newX = x+1; }				
				if(f.getY() < this.y){ newY = y-1; }
				if(f.getY() > this.y){ newY = y+1; }
				
				// To stop the nodes from crowing round in the same spot, next to fruit
				// if there is a larger distance between x or y pick the biggest
				
				// This is quite messy, could be done elegantly?
				if(pixels[newY][newX] == 0){
					
					if(y-1 > 0 && x-1 > 0 && y+1 < height-1 && x+1 < width-1){
						if( Math.abs(f.getY() - y) > Math.abs(f.getX() - x)){
							if(f.getY() < y){
								if(pixels[y-1][x] == 0){y--;}
								else if(pixels[y-1][x+1] == 0){x++; y--;}
								else if(pixels[y-1][x-1] == 0){x--; y--;}
							}
							else{
								if(pixels[y+1][x] == 0){y++;}
								else if(pixels[y+1][x+1] == 0){x++; y++;}
								else if(pixels[y+1][x-1] == 0){x--; y++;}
							}
						}
						else{
							if(f.getX() < x){
								if(pixels[y][x-1] == 0){x--;}
								else if(pixels[y-1][x-1] == 0){x--; y--;}
								else if(pixels[y+1][x-1] == 0){x--; y++;}
							}
							else{
								if(pixels[y][x+1] == 0){x++;}
								else if(pixels[y+1][x+1] == 0){x++; y++;}
								else if(pixels[y-1][x+1] == 0){x++; y--;}
							}
						}
					}									
				}
				else{ x = newX; y = newY; }
			}			
		}
		
		/**
		 * Finds the closest fruit, which can be eaten
		 * 
		 * @return the closest found fruit ( null if none within range )
		 */
		private Fruit findFruit(){
			
			int maxRange = 45;
			int minDistance = 9999;
			Fruit closest = null;
			for(Fruit f : m.getFruit()){
				
				if(f.hasFruit()){
					int distance = (int) Math.pow( (Math.pow(f.getY() - this.y, 2) + Math.pow(f.getX() - this.x, 2)), 0.5);
					if(distance < maxRange){ 
						if(distance < minDistance){
							minDistance = distance;
							closest = f;
						}						
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
		
		// As the nodes health decreases its colour becomes blue
		public Color getColor(){ 
			
			if(health > 7000){ return new Color(rPink, gPink, bPink); }			
			float percent = (float)this.health / (float)7000;
			
			int r = (int)(rPink*percent);
			int g = (int)(gPink*percent);
			int b = (int)(bPink*percent);
			
			r+= (int)( (1 - percent)*rBlue ); 
			g+= (int)( (1 - percent)*gBlue ); 
			b+= (int)( (1 - percent)*bBlue ); 
			
			return new Color(r,g,b);					
		}
		
		public int getX(){ return this.x; }
		public int getY(){ return this.y; }
		
		
		
	}