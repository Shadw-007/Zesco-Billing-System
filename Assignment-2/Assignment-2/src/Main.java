import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.text.DecimalFormat;

// A class representing the GUI for the Electricity Billing System
class ElectricityBillingSystemGUI extends JFrame implements ActionListener {
    // Components for user input and display
    private final JFormattedTextField totalUnitsField;
    private final JFormattedTextField consumedUnitsField;
    private final JTextField startDateField;
    private final JButton calculateButton;
    private final JTextArea resultArea;

    // Constructor for initializing the GUI components
    public ElectricityBillingSystemGUI() {
        setTitle("Zesco Electricity Billing System (ZEBS)"); // Set the title of the GUI window
        setSize(550, 400); // Set the size of the GUI window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        setLayout(null); // Use absolute positioning for components

        // Labels and text fields for input
        JLabel totalUnitsLabel = new JLabel("Total Units (kWh):");
        totalUnitsLabel.setBounds(20, 20, 150, 30);
        add(totalUnitsLabel);
        totalUnitsField = new JFormattedTextField(createFormatter());
        totalUnitsField.setBounds(180, 20, 240, 30);
        add(totalUnitsField);

        JLabel consumedUnitsLabel = new JLabel("Consumed Units (kWh):");
        consumedUnitsLabel.setBounds(20, 60, 150, 30);
        add(consumedUnitsLabel);
        consumedUnitsField = new JFormattedTextField(createFormatter());
        consumedUnitsField.setBounds(180, 60, 240, 30);
        add(consumedUnitsField);

        JLabel startDateLabel = new JLabel("Recharged? (yyyy-mm-dd):");
        startDateLabel.setBounds(20, 100, 150, 30);
        add(startDateLabel);
        startDateField = new JTextField();
        startDateField.setBounds(180, 100, 240, 30);
        add(startDateField);

        // Button to trigger calculation
        calculateButton = new JButton("Calculate");
        calculateButton.setBounds(180, 140, 100, 30);
        calculateButton.addActionListener(this);
        add(calculateButton);

        // Text area to display results
        resultArea = new JTextArea();
        resultArea.setBounds(20, 180, 500, 150);
        resultArea.setEditable(false);
        add(resultArea);

        setVisible(true); // Make the GUI visible
    }

    // Action performed when the Calculate button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton) {
            try {
                // Parse input values
                double totalUnits = Double.parseDouble(totalUnitsField.getText());
                double consumedUnits = Double.parseDouble(consumedUnitsField.getText());
                LocalDate startDate = LocalDate.parse(startDateField.getText());

                // Calculate days passed since start date
                LocalDate currentDate = LocalDate.now();
                long daysPassed = startDate.until(currentDate).getDays() + 1;

                // Calculate average daily consumption
                double averageDailyConsumption = consumedUnits / daysPassed;

                // Limit average daily consumption to two decimal places
                averageDailyConsumption = Math.round(averageDailyConsumption * 100.0) / 100.0;

                // Calculate remaining units and days remaining
                double remainingUnits = totalUnits - consumedUnits;
                int daysRemaining = (int) (remainingUnits / averageDailyConsumption);

                // Calculate total cost in Kwacha
                double ratePerKWh = 106; // 106 Ngwee/kWh
                double totalCostInNgwee = consumedUnits * ratePerKWh;
                double totalCostInKwacha = totalCostInNgwee / 100.0;

                // Format total cost to two decimal places
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String formattedTotalCostInKwacha = decimalFormat.format(totalCostInKwacha);

                // Display results in the text area
                resultArea.setText(" \n Average daily consumption of units: " + String.format("%.2f", averageDailyConsumption) + " kWh\n" +
                        " Number of days remaining until units are depleted/no power: " + daysRemaining + " days" + "\n" +
                        " Equivalent amount in Kwacha if rate is 106 Ngwee/kWh: K" + formattedTotalCostInKwacha);
            } catch (NumberFormatException | NullPointerException ex) {
                resultArea.setText(" Invalid input. Please enter valid numbers and date.");
            } catch (java.time.format.DateTimeParseException ex) {
                resultArea.setText(" Invalid date format. Please use yyyy-mm-dd format.");
            }
        }
    }

    // Main method to create an instance of the GUI
    public static void main() {
        new ElectricityBillingSystemGUI();
    }

    // Helper method to create a number formatter for two decimal places
    private NumberFormatter createFormatter() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Double.class);
        return formatter;
    }
}
