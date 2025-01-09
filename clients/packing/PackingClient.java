package clients.packing;


import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone warehouse Packing Client. warehouse staff to pack the bought order
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year 2024
 */
public class PackingClient {
    public static void main(String args[]) {
        String stockURL = args.length < 1  // URL of stock RW
                        ? Names.STOCK_RW  // default location
                        : args[0];        // supplied location
        String orderURL = args.length < 2  // URL of order
                        ? Names.ORDER     // default location
                        : args[1];        // supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);
        displayGUI(mrf);                  // Create GUI
    }

    private static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client (RMI MVC)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400, 300); // Standard sizing, adjust as necessary

        JPanel mainPanel = new JPanel();
        window.add(mainPanel); // Add panel to frame

        PackingModel model = new PackingModel(mf);
        PackingView view = new PackingView(mf); // Assumes constructor now takes JPanel
        PackingController cont = new PackingController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        window.setVisible(true); // Display Screen
    }
}

