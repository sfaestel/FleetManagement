import java.io.*;
import java.util.*;

/**
 * Manages a fleet of boats and provides operations such as adding, removing, and tracking expenses.
 * @author sarahfaestel
 */

public class FleetManagement {
    /**
     * Constructs a FleetManagement object and initializes the fleet.
     */
    private List<Boat> fleet = new ArrayList<>();
    private static final String DATA_FILE = "FleetData.db";

    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        FleetManagement system = new FleetManagement();
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        // Load data
        if (args.length > 0) {
            system.loadFromCSV(args[0]);
        } else {
            system.loadFromSerializedFile();
        }

        // Main menu loop
        system.runMenu();

        // Save data
        system.saveToSerializedFile();
        System.out.println("\nExiting the Fleet Management System");
    }

    /**
     * Loads boat data from a CSV file.
     * @param fileName the path to the CSV file.
     */
    private void loadFromCSV(String fileName) {
        String line;
        String[] parts;
        BoatType type;
        String name;
        int year;
        String makeModel;
        int length;
        double purchasePrice;
        double expenses;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                parts = line.split(",");
                type = BoatType.valueOf(parts[0].toUpperCase());
                name = parts[1];
                year = Integer.parseInt(parts[2]);
                makeModel = parts[3];
                length = Integer.parseInt(parts[4]);
                purchasePrice = Double.parseDouble(parts[5]);
                expenses = 0.0;

                fleet.add(new Boat(type, name, year, makeModel, length, purchasePrice, expenses));
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Loads fleet data from a serialized file.
     */

    private void loadFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            fleet = (List<Boat>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    /**
    * Saves fleet data to a serialized file.
    */
    private void saveToSerializedFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Runs the menu for managing the fleet.
     */
    private void runMenu() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    printFleet();
                    break;
                case "A":
                    addBoat(scanner);
                    break;
                case "R":
                    removeBoat(scanner);
                    break;
                case "E":
                    addExpense(scanner);
                    break;
                case "X":
                    break;
                default:
                    System.out.println("Invalid menu option, try again.");
            }
        } while (!choice.equals("X"));
    }

    /**
     * Prints the fleet report, showing each boat's details and the total paid and spent.
     */
    private void printFleet() {
        double totalPaid = 0, totalSpent = 0;

        System.out.println("\nFleet report:");
        for (Boat boat : fleet) {
            System.out.println("\t" + boat);
            totalPaid += boat.purchasePrice;
            totalSpent += boat.expenses;
        }
        System.out.printf("\tTotal  \t\t\t\t\t\t\t\t\t\t\t  : Paid $%10.2f : Spent $%10.2f%n", totalPaid, totalSpent);
    }

    /**
     * Adds a new boat to the fleet.
     * @param boat the Boat object to add.
     */
    private void addBoat(Scanner boat) {
        System.out.print("Please enter the new boat CSV data: ");
        String line = boat.nextLine();
        String[] parts = line.split(",");
        String name;
        int year;
        String makeModel;
        int length;
        double purchasePrice;

        try {
            BoatType type = BoatType.valueOf(parts[0].toUpperCase());
            name = parts[1];
            year = Integer.parseInt(parts[2]);
            makeModel = parts[3];
            length = Integer.parseInt(parts[4]);
            purchasePrice = Double.parseDouble(parts[5]);

            fleet.add(new Boat(type, name, year, makeModel, length, purchasePrice, 0));
        } catch (Exception e) {
            System.out.println("Invalid input. Boat not added.");
        }
    }

    /**
     * Removes a boat from the fleet by its name.
     * @param removeBoat the name of the boat to remove.
     * @return true if the boat was removed successfully, otherwise returns false.
     */
    private void removeBoat(Scanner removeBoat) {
        System.out.print("Which boat do you want to remove? ");
        String name = removeBoat.nextLine().trim();

        Boat boat = findBoatByName(name);
        if (boat != null) {
            fleet.remove(boat);
            System.out.println("Boat removed.");
        } else {
            System.out.println("Cannot find boat " + name);
        }
    }

    /**
     * Adds an expense to a boat's budget by its name.
     *
     * @param boatSpentOn   the name of the boat.
     * @return true if the expense was added successfully, false otherwise.
     */
    private void addExpense(Scanner boatSpentOn) {
        System.out.print("Which boat do you want to spend on? ");
        String name = boatSpentOn.nextLine().trim();
        double amount;

        Boat boat = findBoatByName(name);
        if (boat != null) {
            System.out.print("How much do you want to spend? ");
            amount = boatSpentOn.nextDouble();
            boatSpentOn.nextLine(); // Consume newline
            
            if (boat.addExpense(amount)) {
                System.out.printf("Expense authorized, $%.2f spent.%n", amount);
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.%n", boat.getRemainingBudget());
            }
        } else {
            System.out.println("Cannot find boat " + name);
        }
    }

    /**
     * Finds a boat in the fleet by its name (case-insensitive).
     * @param name the name of the boat to find.
     * @return the Boat object if found, otherwise returns null.
     */
    private Boat findBoatByName(String name) {
        return fleet.stream()
                .filter(boat -> boat.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
