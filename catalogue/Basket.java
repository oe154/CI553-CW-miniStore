package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> implements Serializable {
    private static final long serialVersionUID = 1;
    private int theOrderNum = 0; // Order number

    public Basket() {
        theOrderNum = 0;
    }

    public void setOrderNum(int anOrderNum) {
        theOrderNum = anOrderNum;
    }

    public int getOrderNum() {
        return theOrderNum;
    }

    @Override
    public boolean add(Product pr) {
        return super.add(pr); // Call add in ArrayList
    }

    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String csign = (Currency.getInstance(uk)).getSymbol();
        double total = 0.00;

        if (theOrderNum != 0)
            fr.format("Order number: %03d\n", theOrderNum);

        if (this.size() > 0) {
            for (Product pr : this) {
                int number = pr.getQuantity();
                double priceAfterDiscount = pr.getPrice(); // This now considers the discount
                fr.format("%-7s", pr.getProductNum());
                fr.format("%-14.14s ", pr.getDescription());
                fr.format("(%3d) ", number);
                fr.format("%s%7.2f", csign, priceAfterDiscount * number);
                fr.format("\n");
                total += priceAfterDiscount * number;
            }
            fr.format("----------------------------\n");
            fr.format("Total                       ");
            fr.format("%s%7.2f\n", csign, total);
            fr.close();
        }
        return sb.toString();
    }

    /**
     * Apply a discount to all items in the basket.
     * @param discountPercentage The discount percentage to apply (e.g., 20 for 20% off).
     */
    public void applyDiscount(double discountPercentage) {
        for (Product product : this) {
            double newPrice = product.getPrice() * (1 - (discountPercentage / 100.0));
            product.setPrice(newPrice); // Ensure there's a setPrice method in Product class
        }
    }
}
