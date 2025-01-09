package clients.cashier;

import catalogue.*;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Cashier Client.
 */


public class CashierClient {
    public static void main(String args[]) {
        String stockURL = args.length < 1 ? Names.STOCK_RW : args[0];
        String orderURL = args.length < 2 ? Names.ORDER : args[1];

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);
        displayGUI(mrf);
    }

    private static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame("Cashier Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400, 300); // Standard sizing, adjust as necessary

        JPanel mainPanel = new JPanel();
        window.add(mainPanel); // Add panel to frame

        CashierModel model = new CashierModel(mf);
        CashierView view = new CashierView(mf); // Assumes constructor now takes JPanel
        CashierController cont = new CashierController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        window.setVisible(true); // Display Screen
        model.askForUpdate(); // Initial update
    }
}
