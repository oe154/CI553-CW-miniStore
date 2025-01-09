package clients.cashier;

import catalogue.Basket;

import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CashierView extends JPanel implements Observer {
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private static final String CHECK = "Check/Scan";
    private static final String BUY = "Add";
    private static final String CLEAR = "Clear";
    private static final String DISCOUNT = "Discnt%";
    private static final String BOUGHT = "Bought";

    private JLabel pageTitle = new JLabel("Thank You for Shopping at MiniStore");
    private JLabel theAction = new JLabel("Welcome, please check or scan order number then press buy");
    private JTextField theInput = new JTextField();
    private JTextArea theOutput = new JTextArea();
    private JScrollPane theSP = new JScrollPane(theOutput);
    private JButton theBtCheck = new JButton(CHECK);
    private JButton theBtBuy = new JButton(BUY);
    private JButton theBtClear = new JButton(CLEAR);
    private JButton theBtDiscount = new JButton(DISCOUNT);
    private JButton theBtBought = new JButton(BOUGHT);

    private Font buttonFont = new Font("Arial", Font.BOLD, 12);

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    public CashierView(MiddleFactory mf) {
    	
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        setLayout(null);
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);

        setupLabelsAndFields();
        setupButtons();

        setVisible(true);
        theInput.requestFocus(); // Focus is here
    }

    private void setupLabelsAndFields() {
        pageTitle.setForeground(Color.WHITE);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 16));
        pageTitle.setBounds(600, 0, 450, 20);
        add(pageTitle);

        theAction.setFont(new Font("Arial", Font.PLAIN, 14));
        theAction.setForeground(Color.WHITE);
        theAction.setBounds(600, 25, 600, 20);
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
        setupButton(theBtCheck, "Check/Scan", new Rectangle(498, 25, 95, 40));
        setupButton(theBtBuy, "Add", new Rectangle(506, 70, 80, 40));
        setupButton(theBtClear, "Clear", new Rectangle(506, 115, 80, 40));
        setupButton(theBtDiscount, "Discnt%", new Rectangle(506, 205, 80, 40)); // Discount button
        setupButton(theBtBought, "Bought", new Rectangle(506, 160, 80, 40));
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
                case "Check/Scan":
                    cont.doCheck(theInput.getText());
                    
                    break;
                case "Add":
                    cont.doBuy();
                    theInput.setText("");
                    break;
                case "Clear":
                    cont.doClear();
                    theOutput.setText("");
                    theAction.setText("Welcome, please check or scan order number then press buy");
                    theInput.setText("");
                    break;
                case "Discnt%":
                    applyDiscount();
                    break;
                case "Bought":
                    cont.doBought();
                    theInput.setText("");
                    break;
            }
        });
        add(button);
    }
    
    private void applyDiscount() {
        try {
            String inputText = theInput.getText().trim();
            if (inputText.matches("\\d+%?")) {  // Matches digits followed optionally by '%'
                double discountRate = Double.parseDouble(inputText.replace("%", "")) / 100.0;
                String totalText = theOutput.getText();
                Matcher m = Pattern.compile("Total\\s*£\\s*(\\d+\\.\\d{2})").matcher(totalText);
                if (m.find()) {
                    double total = Double.parseDouble(m.group(1));
                    double discountedTotal = total * (1 - discountRate);
                    theOutput.setText(totalText.replaceAll("Total\\s*£\\s*\\d+\\.\\d{2}", "Total £ " + String.format("%.2f", discountedTotal)));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cannot perform discount: Enter a valid percentage", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for discount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setController(CashierController c) {
        cont = c;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = arg.toString();
        theAction.setText(message);

        CashierModel model = (CashierModel) o;
        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("No items in basket");
        }

        theInput.requestFocus(); // Focus is here
    }
}