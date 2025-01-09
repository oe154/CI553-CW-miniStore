package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable {
    private enum State {process, checked}

    private State theState = State.process; // Current state
    private Product theProduct = null; // Current product
    private Basket theBasket = null; // Bought items

    private String pn = ""; // Product being processed

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private double currentDiscount = 0; // Percentage discount

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process; // Current state
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process;                   // State process
        pn = productNum.trim();                     // Product no.
        int amount = 1;                             // & quantity
        try {
            if (theStock.exists(pn)) {              // Stock Exists?
                Product pr = theStock.getDetails(pn); // Get details
                if (pr.getQuantity() >= amount) {    // In stock?
                    // Apply discount if needed
                    double price = pr.getPrice();
                    if (currentDiscount > 0) {
                        price *= (1 - (currentDiscount / 100.0));
                    }
                    theAction = String.format("%s : %7.2f (%2d)", 
                        pr.getDescription(), price, pr.getQuantity());
                    theProduct = pr;                // Remember product
                    theProduct.setPrice(price);     // Set discounted price
                    theProduct.setQuantity(amount); // & quantity
                    theState = State.checked;       // OK await BUY
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            theAction = e.getMessage();
        }
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Buy the product
     */
    public void doBuy() {
        String theAction = "";
        int amount = 1; // & quantity
        try {
            if (theState != State.checked) { // Not checked
                theAction = "please check its availability";
            } else {
                boolean stockBought = theStock.buyStock(theProduct.getProductNum(), theProduct.getQuantity()); // may fail
                if (stockBought) { // Stock bought
                    makeBasketIfReq(); // new Basket ?
                    theBasket.add(theProduct); // Add to bought
                    theAction = "Item added to basket " + theProduct.getDescription(); // details
                } else { // F
                    theAction = "!!! Not in stock"; // Now no stock
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s",
                    "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }
        theState = State.process; // All Done
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void doBought() {
        String theAction = "";
        if (theBasket != null && theBasket.size() >= 1) { // items > 1
            applyDiscountToBasket(); // Adjust prices in the basket
            try {
                theOrder.newOrder(theBasket); // Process order with updated basket
                theBasket = null; // Clear the basket after processing
                theAction = "Start New Order"; // Reset for new order
            } catch (OrderException e) {
                theAction = e.getMessage(); // Handle OrderException
            }
        } else {
            theAction = "No items to process";
        }
        setChanged();
        notifyObservers(theAction); // Notify observers of new action
    }

    /**
     * Apply the current discount to the prices in the basket
     */
    private void applyDiscountToBasket() {
        for (Product product : theBasket) {
            double discountedPrice = product.getPrice() * (1 - currentDiscount / 100.0);
            product.setPrice(discountedPrice); // Assume there's a setter for price in Product class
        }
    }

    /**
     * Set discount for the current session
     */
    public void applyDiscount(String discountText) {
        try {
            currentDiscount = Double.parseDouble(discountText.replace("%", ""));
            setChanged();
            notifyObservers("Discount of " + currentDiscount + "% applied.");
        } catch (NumberFormatException e) {
            setChanged();
            notifyObservers("Invalid discount format.");
        }
    }

    /**
     * Clear data like basket or product details
     */
    public void clearData() {
        theBasket = new Basket();
        setChanged();
        notifyObservers("Data Cleared");
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    /**
     * Make a basket when required
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber(); // Unique order num.
                theBasket = makeBasket(); // basket list
                theBasket.setOrderNum(uon); // Add an order number
            } catch (OrderException e) {
                DEBUG.error("Comms failure\n" +
                        "CashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    /**
     * Return an instance of a new Basket
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}