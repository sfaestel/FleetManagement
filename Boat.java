import java.io.Serializable;

/**
 * Represents a single boat with details such as name, year, model, length, price, and spent budget.
 */

public class Boat implements Serializable {
    private BoatType type;
    private String name;
    private int yearOfManufacture;
    private String makeModel;
    private int length; // in feet
    double purchasePrice;
    double expenses = 0.0;

    /**
     * Constructs a new Boat object.
     *
     * @param type type of boat
     * @param name the name of the boat.
     * @param yearOfManufacture the year of the boat's manufacture.
     * @param makeModel the model of the boat.
     * @param length the length of the boat in feet.
     * @param purchasePrice the price of the boat in dollars.
     * @param expenses the amount spent on boat in dollars.
     */
    public Boat(BoatType type, String name, int yearOfManufacture, String makeModel, int length, double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /**
     * Gets the name of the boat.
     *
     * @return the boat's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the remaining budget for the boat.
     *
     * @return the remaining budget (price - expenses).
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Adds an expense to the boat's budget if within the available budget.
     *
     * @param amount the amount to spend.
     * @return true if the expense was added successfully, otherwise returns false.
     */
    public boolean addExpense(double amount) {
        if (amount <= getRemainingBudget()) {
            expenses += amount;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the boat.
     *
     * @return a formatted string of the boat's details.
     */
    @Override
    public String toString() {
        return String.format(
                "%-7s %-20s %4d %-10s %3d' : Paid $%10.2f : Spent $%10.2f",
                type, name, yearOfManufacture, makeModel, length, purchasePrice, expenses
        );
    }
}

