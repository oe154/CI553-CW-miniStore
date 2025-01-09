package clients.backDoor;

import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class BackDoorView extends JPanel implements Observer {
    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private final JLabel pageTitle = new JLabel("Staff check and manage stock");
    private final JLabel theAction = new JLabel("Enter Product Number:");
    private final JLabel theActionNo = new JLabel("Add here:");
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField("0");
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane(theOutput);
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtRStock = new JButton(RESTOCK);
    private final JButton theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    public BackDoorView(MiddleFactory mf) {
    	
        try {
            theStock = mf.makeStockReadWriter(); // Database access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        setLayout(null);
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);

        Font titleFont = new Font("Arial", Font.BOLD, 16);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);

        pageTitle.setFont(titleFont);
        pageTitle.setForeground(Color.WHITE);
        pageTitle.setBounds(600, 0, 270, 20);
        add(pageTitle);

        setUpButton(theBtQuery, "Query", new Rectangle(506, 25, 80, 40), buttonFont);
        setUpButton(theBtRStock, "Add", new Rectangle(506, 70, 80, 40), buttonFont);
        setUpButton(theBtClear, "Clear", new Rectangle(506, 115, 80, 40), buttonFont);

        theAction.setFont(new Font("Arial", Font.PLAIN, 14));
        theAction.setForeground(Color.WHITE);
        theAction.setBounds(600, 25, 270, 20);
        add(theAction);
        
        theActionNo.setFont(new Font("Arial", Font.PLAIN, 14));
        theActionNo.setForeground(Color.WHITE);
        theActionNo.setBounds(770, 25, 270, 20);
        add(theActionNo);
        

        setupTextField(theInput, new Rectangle(600, 50, 120, 40) );
        setupTextField(theInputNo, new Rectangle(750, 50, 120, 40));

        theSP.setBounds(600, 100, 270, 160);
        theOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        theOutput.setEditable(false);
        theOutput.setBackground(Color.DARK_GRAY);
        theOutput.setForeground(Color.WHITE);
        theOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        add(theSP);    
    }

    private void setUpButton(JButton button, String text, Rectangle bounds, Font font) {
        button.setText(text);
        button.setFont(font);
        button.setBounds(bounds);
        button.setBackground(new Color(70, 70, 70)); // Dark grey
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        button.addActionListener(e -> {
            if ("Query".equals(text)) {
                cont.doQuery(theInput.getText());
                theActionNo.setText("");
            } else if ("Add".equals(text)) {
                cont.doRStock(theInput.getText(), theInputNo.getText());
            } else {
                cont.doClear();
                theActionNo.setText("Add Products Here:");
                theInput.setText("");
                theInputNo.setText("");
                
            }
        });
        add(button);
    }

    private void setupTextField(JTextField textField, Rectangle bounds) {
        textField.setBounds(bounds);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.WHITE);
        textField.setBackground(Color.DARK_GRAY);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        textField.setCaretColor(Color.WHITE);
        add(textField);
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        String message = (String) arg;
        theAction.setText(message);
        theOutput.setText(((BackDoorModel) modelC).getBasket().getDetails());
        theInput.requestFocus();
    }
}