import java.awt.Color;
import java.util.ArrayList;

public class Fruit{
	
		private Model m;
		
		private int timeUntilBloom;
		private int fruit = 0;
		private int x, y;
		
		int rPink = 244;
		int gPink = 66;
		int bPink = 167;
		
		int rYellow = 65;
		int gYellow = 89;
		int bYellow = 244;
		
		private boolean hasFruit;
		private ArrayList<Object> feeders = new ArrayList<Object>();
		private int eaterCount = 0;
		
		public Fruit(int y, int x, Model m, boolean createCluster){
			this.y = y;
			this.x = x;
			this.m = m;			
			if(createCluster){ createCluster(); }			
			hasFruit = false;
			this.timeUntilBloom = (int)(Math.random()*100);
		}
		
		public void ping(){
			
			if(!hasFruit){
				timeUntilBloom++;
				if(timeUntilBloom > 500){ hasFruit = true; }
			}
			if(hasFruit){
				timeUntilBloom++;
				if(timeUntilBloom < 1500){ fruit+=5; }
				// If nodes have eaten all fruit
				if(fruit < 0){ 
					hasFruit = false; 
					timeUntilBloom = (int)(Math.random()*100);
					
					// this isn't currently being used,
					// the idea is the more feeders at one time
					// the less food they get per feeder
					this.feeders.clear(); 	
				}
			}			
		}
		
		public void createCluster(){
			
			int size = (int)(Math.random()*5) + 5;
			
			for(int i = 0; i < size; i++){
				int xx = (int)(Math.random()*40) - 10 + this.x;
				int yy = (int)(Math.random()*40) - 10 + this.y;
				
				if(yy > 0 && yy < m.height-1 && xx > 0 && xx < m.width-1){
					m.createFruit( yy, xx );
				}				
			}
		}
		
		// Fades into red from white as the time until bloom gets closer
		// On bloom, fruit is displayed as blue
		public Color getColor(){
			
			int r = 0;
			int g = 0;
			int b = 0;
			
			if(hasFruit){
				float percent = (float)this.fruit / (float)5000;
				r = (int)(rPink*percent);
				g = (int)(gPink*percent);
				b = (int)(bPink*percent);
				
				r+= (int)( (1 - percent)*rYellow ); 
				g+= (int)( (1 - percent)*gYellow ); 
				b+= (int)( (1 - percent)*bYellow ); 
				
			}
			else{
				float percent = (float)this.timeUntilBloom / (float)1000;
				r = (int)(rPink*percent);
				g = (int)(gPink*percent);
				b = (int)(bPink*percent);
				
				r+= (int)( (1 - percent)*255 ); 
				g+= (int)( (1 - percent)*255 ); 
				b+= (int)( (1 - percent)*255 ); 
			}
			
			return new Color(r,g,b);			
		}
		
		public void eatFruit(){ this.fruit--; }
		
		// Currently unused
		public int feed(Object eater){
			if(!this.feeders.contains(eater)){ 
				this.feeders.add(eater); 
				this.eaterCount++;
			}
			this.fruit -= (int)((float)10/(float)eaterCount);
			return (int)((float)50/(float)eaterCount);
		}
		
		public int getX(){ return this.x; }
		public int getY(){ return this.y;}		
		public boolean hasFruit(){ return hasFruit; }
		
	}