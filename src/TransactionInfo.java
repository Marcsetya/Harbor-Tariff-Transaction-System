import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class TransactionInfo extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField shipIdField, arrivalDateField, departureDateField, searchField;
    private JTable table;
    private JComboBox<String> tariffIdCombo;

    public TransactionInfo() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Transaction Info", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // formPanel using BorderLayout for better alignment
        JPanel formPanel = new JPanel(new BorderLayout(10, 10)); // Adds spacing

        // left side: labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel tariffIdLabel = new JLabel("Tariff ID:");
        JLabel shipIdLabel = new JLabel("Ship ID:");
        JLabel arrivalDateLabel = new JLabel("Arrival Date:");
        JLabel departureDateLabel = new JLabel("Departure Date:");
        labelPanel.add(tariffIdLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(shipIdLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(arrivalDateLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(departureDateLabel);

        // Right side: input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        tariffIdCombo = new JComboBox<>();
        shipIdField = new JTextField();
        arrivalDateField = new JTextField();
        departureDateField = new JTextField();

        // Set fixed size for input fields
        Dimension inputSize = new Dimension(200, 25);
        tariffIdCombo.setPreferredSize(inputSize);
        tariffIdCombo.setMaximumSize(inputSize);
        shipIdField.setPreferredSize(inputSize);
        shipIdField.setMaximumSize(inputSize);
        arrivalDateField.setPreferredSize(inputSize);
        arrivalDateField.setMaximumSize(inputSize);
        departureDateField.setPreferredSize(inputSize);
        departureDateField.setMaximumSize(inputSize);

        inputPanel.add(tariffIdCombo);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(shipIdField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(arrivalDateField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(departureDateField);

        // Adding label and input panels to the formPanel
        formPanel.add(labelPanel, BorderLayout.WEST);
        formPanel.add(inputPanel, BorderLayout.CENTER);

        // Wrap everything in a center-aligned panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout()); // Centers components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

        centerPanel.add(formPanel, gbc);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Ensures alignment

        // Declaring the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View");
        JButton searchButton = new JButton("Search");

        // Styling the buttons
        Color buttonColor = new Color(50, 120, 200); // Light Blue
        Color textColor = Color.WHITE;
        JButton[] buttons = { addButton, editButton, deleteButton, viewButton, searchButton };
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(textColor);
        }

        // Adding the buttons to the buttonPanel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Table
        tableModel = new DefaultTableModel(
        new String[] { "ID", "Tariff ID", "Ship ID", "User ID", "Arrival Date", "Departure Date", "Total Amount", "Transaction Date" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Button actions
        addButton.addActionListener(e -> addTransaction());
        editButton.addActionListener(e -> editTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());
        viewButton.addActionListener(e -> refreshTable());
        searchButton.addActionListener(e -> searchTransaction());

        // Wrapper for titleLabel & searchPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Center content wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BorderLayout()); // Stack elements vertically
        centerWrapper.add(centerPanel, BorderLayout.NORTH);
        centerWrapper.add(buttonPanel, BorderLayout.CENTER); // Move button panel below input fields

        // Adding components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        loadTariffID();
    }

    // Adds a new transaction info to the database
    private void addTransaction() {
        String Tariff = (String) tariffIdCombo.getSelectedItem();
        String Ship = shipIdField.getText();
        String Arrival = arrivalDateField.getText();
        String Departure = departureDateField.getText();
        double total_amount = calculateTariff(Ship, Arrival, Departure);
        int userId = getCurrentUserId();
        if (userId == -1) {
            JOptionPane.showMessageDialog(null, "Cannot add transaction. No valid user ID.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (tariff_id, ship_id, user_id, arrival_date, departure_date, total_amount) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, Tariff);
            stmt.setString(2, Ship);
            stmt.setInt(3, userId);
            stmt.setString(4, Arrival);
            stmt.setString(5, Departure);
            stmt.setDouble(6, total_amount);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Transaction Successfully Added!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Edits the selected transaction info record
    private void editTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a transaction to edit.");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String Tariff = (String) tariffIdCombo.getSelectedItem();
        String Ship = shipIdField.getText();
        String Arrival = arrivalDateField.getText();
        String Departure = departureDateField.getText();
        double total_amount = calculateTariff(Ship, Arrival, Departure);
        int userId = getCurrentUserId();
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
            PreparedStatement stmt = conn.prepareStatement("UPDATE transactions SET tariff_id = ?, ship_id = ?, arrival_date = ?, departure_date = ? WHERE id = ?")) {
            stmt.setString(1, Tariff);
            stmt.setString(2, Ship);
            stmt.setString(3, Arrival);
            stmt.setString(4, Departure);
            stmt.setDouble(5, total_amount);
            stmt.setInt(6, userId);
            stmt.setInt(7, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Transactiion Successfully Edited!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Deletes the selected ship info record
    private void deleteTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a transaction to remove.");
            return;
        }
        
        int id = (int) table.getValueAt(selectedRow, 0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM transactions WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Transaction Info Successfully Removed!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Searches for a transaction based on user input
    private void searchTransaction() {
        String keyword = searchField.getText();
        tableModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE arrival_date LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tableModel.addRow(new Object[] { 
                rs.getInt("id"), rs.getInt("tariff_id"), rs.getInt("ship_id"),
                rs.getInt("user_id"), rs.getTimestamp("arrival_date"), rs.getTimestamp("departure_date"),
                rs.getString("total_amount"), rs.getTimestamp("transaction_date") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Refreshes the table data
    private void refreshTable() {
        tableModel.setRowCount(0);
        System.out.println("Refreshing table..."); // Debugging log

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "")) {
            System.out.println("Database connected successfully!"); // Debugging log
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] { 
                rs.getInt("id"), rs.getInt("tariff_id"), rs.getInt("ship_id"),
                rs.getInt("user_id"), rs.getTimestamp("arrival_date"), rs.getTimestamp("departure_date"),
                rs.getString("total_amount"), rs.getTimestamp("transaction_date") });
            }
            System.out.println("Data fetched successfully! Rows: " + tableModel.getRowCount());
        } catch (SQLException ex) {
            System.err.println("Database connection failed!");
            ex.printStackTrace();
        }
    }

    private void loadTariffID() {
        tariffIdCombo.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM tariff")) {
            while (rs.next()) {
                tariffIdCombo.addItem(rs.getString("id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Returns the id of the currently logged-in user
    private int getCurrentUserId() {
        String username = SessionManager.getLoggedInUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(null, "No user is logged in!");
            return -1; // Indicate error (or handle it accordingly)
        }
    
        int userId = -1; // Default value
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM user WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                userId = rs.getInt("id");
            } else {
                JOptionPane.showMessageDialog(null, "User not found in database!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userId;
    }

    // Calculates the tariff based on ship's stay duration and category
    private double calculateTariff(String shipId, String arrival, String departure) {
        double totalAmount = 0.0;
        tableModel.setRowCount(0);
        System.out.println("Calculating tariff..."); // Debugging log

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "")) {
            
            // Get tariff_id and Tariff_Amount
            String shipQuery = "SELECT category_id FROM ships WHERE id = ?";
            try (PreparedStatement shipStmt = conn.prepareStatement(shipQuery)) {
                shipStmt.setString(1, shipId);
                ResultSet shipRs = shipStmt.executeQuery();
                if (shipRs.next()) {
                    int categoryId = shipRs.getInt("category_id");

                    // Get tariff amount from tariff table
                    String tariffQuery = "SELECT Tariff_Amount FROM tariff WHERE category_id = ?";
                    try (PreparedStatement tariffStmt = conn.prepareStatement(tariffQuery)) {
                        tariffStmt.setInt(1, categoryId);
                        ResultSet tariffRs = tariffStmt.executeQuery();
                        if (tariffRs.next()) {
                            double Tariff_Amount = tariffRs.getDouble("Tariff_Amount");

                            // Calculate days stayed
                            LocalDate arrivalDate = LocalDate.parse(arrival);
                            LocalDate departureDate = LocalDate.parse(departure);
                            long daysStayed = ChronoUnit.DAYS.between(arrivalDate, departureDate);
                            if (daysStayed < 1) daysStayed = 1;

                            double dailyTariff = Tariff_Amount;

                            // Exponential tariff increase
                            for (int i = 0; i < daysStayed; i++) {
                                totalAmount += dailyTariff; // Add daily tariff to total
                                dailyTariff *= 2; // Double the tariff for the next day
                            }

                            totalAmount = daysStayed * Tariff_Amount;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalAmount;
    }
}