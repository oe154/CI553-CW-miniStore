package catalogue;

import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class Product implements Serializable {
    private static final long serialVersionUID = 20092506;
    private String theProductNum;       // Product number
    private String theDescription;      // Description of product
    private double thePrice;            // Price of product
    private int    theQuantity;         // Quantity involved

    /**
     * Construct a product details
     * @param aProductNum Product number
     * @param aDescription Description of product
     * @param aPrice The price of the product
     * @param aQuantity The Quantity of the product involved
     */
    public Product(String aProductNum, String aDescription, double aPrice, int aQuantity) {
        theProductNum = aProductNum;
        theDescription = aDescription;
        thePrice = aPrice;
        theQuantity = aQuantity;
    }

    public String getProductNum() { return theProductNum; }
    public String getDescription() { return theDescription; }
    public double getPrice() { return thePrice; }
    public int getQuantity() { return theQuantity; }

    public void setProductNum(String aProductNum) {
        theProductNum = aProductNum;
    }

    public void setDescription(String aDescription) {
        theDescription = aDescription;
    }

    /**
     * Sets the price of the product.
     * This method can be used to adjust the price when a discount is applied.
     * @param aPrice New price of the product.
     */
    public void setPrice(double aPrice) {
        thePrice = aPrice;
    }

    public void setQuantity(int aQuantity) {
        theQuantity = aQuantity;
    }

    /**
     * Apply a discount to the price of this product.
     * @param discountPercentage The percentage discount to apply (e.g., 10 for 10% off).
     */
    public void applyDiscount(double discountPercentage) {
        if (discountPercentage > 0 && discountPercentage <= 100) {
            thePrice *= (1 - discountPercentage / 100);
        }
    }
}