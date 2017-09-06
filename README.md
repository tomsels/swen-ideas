# swen-ideas

The frame rate isn't constant ( need to fix this, and make all physics relient on the frame rate for their change ) so you might want to change:

 new Timer(7, (e)-> { 	    	
	    	m.ping();
	    }	    
	    ).start();
      
in the view component if it's playing up
