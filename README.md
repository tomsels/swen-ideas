# swen-ideas

The frame rate isn't constant ( need to fix this, and make all physics reliant on the frame rate for their change ) so you might want to change:

 new Timer(7, (e)-> { 	    	
	    	m.ping();
	    }	    
	    ).start();
      
in the View class if it's playing up

- or reduce the node / fruit count since it's quite cpu intensive at the moment
