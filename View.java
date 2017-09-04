
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;


public class View extends JComponent implements Observer{
	
	private Model m;
	int ppn; // Pixels Per Node
	
	// Size of the display area
	private int boundingX = 1200;
	private int boundingY = 600;
	
	private JFrame f;

	public View(){
		
	    this.f = new JFrame(" meaow ");
	    this.m = new Model();
	    this.ppn = m.ppn;
	    
	    m.addObserver(this);
	    
	    // ######################
	    //						#
	    // 		Menu Area		#
	    //						#
	    //#######################
	    JPanel menu = new JPanel();
	    
	    JButton zoomIn = new JButton("Zoom in");
	    zoomIn.addActionListener(new ActionListener() {				
			public void actionPerformed(ActionEvent e) {
				m.zoomIn();
			}
		});	
	    zoomIn.setFocusable(false);
	    JButton zoomOut = new JButton("Zoom out");
	    zoomOut.addActionListener(new ActionListener() {				
			public void actionPerformed(ActionEvent e) {
				m.zoomOut();
			}
		});	
	    zoomOut.setFocusable(false);
	    menu.add(zoomIn);
	    menu.add(zoomOut);
	    //########################
	    
	    
	    JSplitPane window = new JSplitPane(JSplitPane.VERTICAL_SPLIT, menu, this);
	    
	    f.add(window);	    
	    f.setFocusable(true);	    
	    f.pack();
	    f.setVisible(true);
	    
	    this.addMouseListener( new MouseController(this.m) );
	    f.addKeyListener( new KeyboardController(this.m));
	    
	    new Timer(7, (e)-> { 	    	
	    	m.ping();
	    }	    
	    ).start();
	    	    
	    
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		f.repaint();
		this.ppn = m.ppn;
	}
		
	public void paintComponent(Graphics g) {
		
		// Outline of the game area
		g.setColor(Color.black);
		g.drawRect(m.viewX , m.viewY, m.width*ppn, m.height*ppn);
		
		// Draw fruits
		for(Fruit f : m.getFruit()){					
			g.setColor( f.getColor() );			
			g.fillOval(m.viewX + f.getX()*ppn, m.viewY + f.getY()*ppn, ppn*4, ppn*4);
		}
		
		// Draw users nodes
		for(Node n : m.getNodes()){
			g.setColor( n.getColor());
			g.fillRect(m.viewX + n.getX()*ppn, m.viewY + n.getY()*ppn, ppn, ppn);
		}
		// Draw enemy nodes
		g.setColor(Color.RED);
		for(EnemyNode n : m.getEnemyNodes()){			
			g.fillRect(m.viewX + n.getX()*ppn, m.viewY + n.getY()*ppn, ppn, ppn);
		}
		// Draw enemy hunters
		g.setColor(Color.RED);
		for(HunterNode h : m.getHunters()){			
			g.fillRect(m.viewX + h.getX()*ppn, m.viewY + h.getY()*ppn, ppn*3, ppn*3);
		}
		
		// Text statistics for the game:
		g.drawString("number of nodes: " + m.getNodes().size(), 10, 10);
		
		int averageThresh = 0;
		for(Node n : m.getNodes()){
			averageThresh += n.getDNA().getBirthThreshold();
		}
		averageThresh = (int)((float)averageThresh/(float)m.getNodes().size());
		g.drawString("average birth threshold: " + averageThresh, 10, 40);
		
		int agression = 0;
		for(Node n : m.getNodes()){
			agression += n.getDNA().getAggression();
		}
		agression = (int)((float)agression /(float)m.getNodes().size());
		g.drawString("agression/fear: " + agression , 10, 60);
		
	}
	
	public Dimension getPreferredSize() {return new Dimension(boundingX, boundingY);}
}
