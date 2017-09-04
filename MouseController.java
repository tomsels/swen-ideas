import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseController implements MouseListener{

	private Model m;
	
	public MouseController(Model m){
		this.m = m;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		// Directs nodes within radius to the point clicked
		m.directNodes(e.getY(), e.getX());
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
