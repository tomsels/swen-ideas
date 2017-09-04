import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener{

	private Model m;
	
	public KeyboardController(Model m){
		this.m = m;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		// Move the viewing angle		
		if(e.getKeyChar() == 'a'){ this.m.viewX+=25; }
		else if(e.getKeyChar() == 'd'){ this.m.viewX-=25; }
		else if(e.getKeyChar() == 'w'){ this.m.viewY+=25; }
		else if(e.getKeyChar() == 's'){ this.m.viewY-=25; }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
