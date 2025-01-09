package clients;

import clients.backDoor.BackDoorController;

import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Starts all the clients (user interface) as a single application using a tabbed interface.
 * Good for testing the system using a single application.
 * @author Mike Smith University of Brighton
 * @version 2.0
 * @author Shine University of Brighton
 * @version year-2024
 */
public class Main {
    private JFrame mainFrame;
    private MiddleFactory middleFactory;

    public static void main(String[] args) {
        new Main().begin();
    }

    public void begin() {
        middleFactory = new LocalMiddleFactory();
        mainFrame = new JFrame("The MiniStore");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Customer", createCustomerPanel(middleFactory));
        tabbedPane.add("Cashier", createCashierPanel(middleFactory));
        tabbedPane.add("Packing", createPackingPanel(middleFactory));
        tabbedPane.add("BackDoor", createBackDoorPanel(middleFactory));

        mainFrame.add(tabbedPane);
        mainFrame.pack();  
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        mainFrame.setVisible(true);
    }

    private JPanel createCustomerPanel(MiddleFactory mlf) {
        CustomerModel model = new CustomerModel(mlf);
        CustomerView view = new CustomerView(mlf);
        CustomerController controller = new CustomerController(model, view);
        view.setController(controller);
        model.addObserver(view);
        return view;
    }

    private JPanel createCashierPanel(MiddleFactory mlf) {
        CashierModel model = new CashierModel(mlf);
        CashierView view = new CashierView(mlf);
        CashierController controller = new CashierController(model, view);
        view.setController(controller);
        model.addObserver(view);
        return view;
    }

    private JPanel createPackingPanel(MiddleFactory mlf) {
        PackingModel model = new PackingModel(mlf);
        PackingView view = new PackingView(mlf);
        PackingController controller = new PackingController(model, view);
        view.setController(controller);
        model.addObserver(view);
        return view;
    }

    private JPanel createBackDoorPanel(MiddleFactory mlf) {
        BackDoorModel model = new BackDoorModel(mlf);
        BackDoorView view = new BackDoorView(mlf);
        BackDoorController controller = new BackDoorController(model, view);
        view.setController(controller);
        model.addObserver(view);
        return view;
    }
}