package clients.backDoor;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 */


public class BackDoorClient
{
   public static void main (String args[])
   {
     String stockURL = args.length < 1     // URL of stock RW
                     ? Names.STOCK_RW      //  default  location
                     : args[0];            //  supplied location
     String orderURL = args.length < 2     // URL of order
                     ? Names.ORDER         //  default  location
                     : args[1];            //  supplied location
     
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo( stockURL );
    mrf.setOrderInfo  ( orderURL );        //
    displayGUI(mrf);                       // Create GUI
  }
  
  private static void displayGUI(MiddleFactory mf)
  {     
	  JFrame window = new JFrame("BackDoor Client (MVC RMI)");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setSize(400, 300);  // Assuming standard size for consistency

	    JPanel mainPanel = new JPanel(); // Main panel that may contain more components
	    window.add(mainPanel);  // Add the panel to the frame
    
    BackDoorModel      model = new BackDoorModel(mf);
    BackDoorView       view  = new BackDoorView(mf );
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model - view is observer, model is Observable
    window.setVisible(true);         // Display Screen
  }
}
