import java.awt.Color;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {

	// Size of the world
	int height = 400;
	int width = 400;
	
	// Grid which shows which spaces are occupied
	// Currently 0 = empty, 1 = node, 2 = enemy node, 3 = hunter
	int[][] pixels = new int[height][width];
	
	public int ppn; // Pixels per node for the display
	
	// Viewing position for the camera
	public int viewX = 300;
	public int viewY = -150;
	
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<EnemyNode> enemyNodes = new ArrayList<EnemyNode>();
	ArrayList<Fruit> fruit = new ArrayList<Fruit>();
	ArrayList<HunterNode> hunters = new ArrayList<HunterNode>();
	
	public static double e = 2.7182818;
	
	public Model(){
		
		// Pixels per node
		this.ppn = 2;
		
		//###########################################
		//											#
		// 			Initial game generation			#
		//											#
		//###########################################
		
		// Initialise nodes
		for(int i = 0; i < 500; i++){
			int x = (int)(Math.random()*width - 1);
			int y = (int)(Math.random()*height/2 - 1);
			nodes.add( new Node(y, x, this, new DNA(1000, -10)) );			
		}		
		
		// Initialise enemy nodes
		for(int i = 0; i < 500; i++){
			int x = (int)(Math.random()*width - 1);
			int y = (int)(Math.random()*height/2 + height/2 - 1);
			enemyNodes.add( new EnemyNode(y, x, this, new DNA(1000, -10)) );			
		}		
		
		// Initialise fruit
		// ( with clusters )
		for(int i = 0; i < 10; i++){
			int x = (int)(Math.random()*width - 1);
			int y = (int)(Math.random()*height - 1);
			fruit.add( new Fruit(y,x,this,true));
		}
		// without clusters
		for(int i = 0; i < 20; i++){
			int x = (int)(Math.random()*width - 1);
			int y = (int)(Math.random()*height - 1);
			fruit.add( new Fruit(y,x,this,false));
		}
		
		// Create hunters
		for(int i = 0; i < 20; i++){
			int x = (int)(Math.random()*width - 1);
			int y = (int)(Math.random()*height - 1);
			hunters.add( new HunterNode(y,x,this));
		}
	}
	
	public void ping(){
		
		for(int i = 0; i < nodes.size(); i++){ nodes.get(i).ping(); }
		for(int i = 0; i < enemyNodes.size(); i++){ enemyNodes.get(i).ping(); }
		for(int i = 0; i < fruit.size(); i++){ fruit.get(i).ping(); }
		for(int i = 0; i < hunters.size(); i++){ hunters.get(i).ping(); }
		this.setChanged();
		this.notifyObservers();
		
	}
	
	// These need fixing / are slightly wonky
	public void zoomIn(){		
		if(ppn < 16){ 
			this.viewX -= (int)((float)1200/(float)(ppn+1)/(float)2);
			ppn++; 
		}		
	}
	public void zoomOut(){
		if(ppn > 1){ 
			this.viewX += (int)((float)1200/(float)(ppn+1)/(float)2);
			ppn--; 
		}		
	}
	
	public void directNodes(int y, int x){
		for(Node n : nodes){
			n.manualDirection(-viewY/ppn + y/ppn, -viewX/ppn + x/ppn);
		}
	}
	
	public void createNode(int y, int x, DNA dna){
		nodes.add( new Node(y, x, this, dna) );	
	}
	
	public void createEnemyNode(int y, int x, DNA dna){
		enemyNodes.add( new EnemyNode(y, x, this, dna) );	
	}

	public void createFruit(int y, int x){
		fruit.add( new Fruit(y,x,this,false));	
	}
	
	public void createHunter(int y, int x){
		hunters.add( new HunterNode(y,x,this));	
	}
	
	public void removeNode(Node toRemove){ nodes.remove(toRemove); }
	public void removeEnemyNode(EnemyNode toRemove){ enemyNodes.remove(toRemove); }		
	
	public ArrayList<Node> getNodes(){ return this.nodes; }
	public ArrayList<EnemyNode> getEnemyNodes(){ return this.enemyNodes; }
	public ArrayList<Fruit> getFruit(){ return this.fruit; }
	public ArrayList<HunterNode> getHunters(){ return this.hunters; }
	
	public int getDimension(){ return this.height; }

	

	
}
