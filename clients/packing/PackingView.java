package clients.packing;

import catalogue.Basket;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PackingView extends JPanel implements Observer {
    private static final String PACKED = "Packed";

    private JLabel pageTitle = new JLabel("Packing Bought Order");
    private JLabel theAction = new JLabel("");
    private JTextArea theOutput = new JTextArea();
    private JScrollPane theSP = new JScrollPane(theOutput);
    private JButton theBtPack = new JButton(PACKED);

    private OrderProcessing theOrder = null;
    private PackingController cont = null;

    public PackingView(MiddleFactory mf) {
    	
        try {
            theOrder = mf.makeOrderProcessing();  // Process order
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

        theAction.setFont(new Font("Arial", Font.PLAIN, 14));
        theAction.setForeground(Color.WHITE);
        theAction.setBounds(600, 25, 600, 20);
        add(theAction);

        setUpButton(theBtPack, "Packed", new Rectangle(506, 25, 80, 40), buttonFont);

        theSP.setBounds(600, 55, 270, 205);
        theOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        theOutput.setEditable(false);
        theOutput.setBackground(Color.DARK_GRAY);
        theOutput.setForeground(Color.WHITE);
        theOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        add(theSP);

        setVisible(true);  // Make visible
    }

    private void setUpButton(JButton button, String text, Rectangle bounds, Font font) {
        button.setText(text);
        button.setFont(font);
        button.setBounds(bounds);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        button.addActionListener(e -> cont.doPacked());
        add(button);
    }

    public void setController(PackingController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        String message = (String) arg;
        theAction.setText(message);

        PackingModel model = (PackingModel) modelC;
        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }
    }
}

