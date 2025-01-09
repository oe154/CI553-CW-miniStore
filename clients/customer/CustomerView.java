package clients.customer;

import catalogue.Basket;

import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;
import middle.LocalMiddleFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CustomerView extends JPanel implements Observer {
    class Name {
        public static final String CHECK = "Check";
        public static final String CLEAR = "Clear";
    }

    private final JLabel pageTitle = new JLabel("Search products: 0001-0007");
    private final JLabel theAction = new JLabel("Enter Product Number");
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane(theOutput);
    private final JButton theBtCheck = new JButton(Name.CHECK);
    private final JButton theBtClear = new JButton(Name.CLEAR);
    
    private Font buttonFont = new Font("Arial", Font.BOLD, 12);

    private Picture thePicture = new Picture(80, 80);
    private StockReader theStock;
    private CustomerController cont;

    public CustomerView(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReader(); // Database Access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        

        setLayout(null);
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setVisible(true);
        
        
        
        setupLabelsAndFields();
        setupButtons();
        
        setVisible(true);
        theInput.requestFocus(); // Focus is here
        
        thePicture.setBounds(506, 140, 80, 80);
        add(thePicture);
    }
    
    private void setupLabelsAndFields() {
        pageTitle.setForeground(Color.WHITE);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 16));
        pageTitle.setBounds(600, 0, 270, 20);
        add(pageTitle);

        theAction.setFont(new Font("Arial", Font.PLAIN, 14));
        theAction.setForeground(Color.WHITE);
        theAction.setBounds(600, 25, 270, 20);
        add(theAction);

        theInput.setFont(new Font("Arial", Font.PLAIN, 14));
        theInput.setForeground(Color.WHITE);
        theInput.setBackground(Color.DARK_GRAY);
        theInput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        theInput.setCaretColor(Color.WHITE);
        theInput.setBounds(600, 50, 270, 40);
        add(theInput);

        theSP.setBounds(600, 100, 270, 160);
        theOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        theOutput.setEditable(false);
        theOutput.setBackground(Color.DARK_GRAY);
        theOutput.setForeground(Color.WHITE);
        theOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        add(theSP);
    }
    
    private void setupButtons() {
        setupButton(theBtCheck, "Check", new Rectangle(506, 25, 80, 40));       
        setupButton(theBtClear, "Clear", new Rectangle(506, 70, 80, 40));        
    }

    private void setupButton(JButton button, String text, Rectangle bounds) {
        button.setText(text);
        button.setFont(buttonFont);
        button.setBounds(bounds);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        button.addActionListener(e -> {
            switch (text) {
                case "Check":
                    cont.doCheck(theInput.getText());
                    theInput.setText("");
                    break;
                case "Clear":
                    cont.doClear();
                    theInput.setText("");
                    break;
                
            }
        });
        add(button);
    }

    public void setController(CustomerController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        ImageIcon image = model.getPicture();
        if (image == null) {
            thePicture.clear();
        } else {
            thePicture.set(image);
        }
        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }
}