import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ShipInfo extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField companyIdField, shipCategoryField, shipNameField, searchField;
    private JTable table;

    public ShipInfo() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Ship Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // formPanel using BorderLayout for better alignment
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));

        // Left side: labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel companyIdLabel = new JLabel("Company ID:");
        JLabel shipCategoryLabel = new JLabel("Ship Category:");
        JLabel shipnameLabel = new JLabel("Ship Name:");
        labelPanel.add(companyIdLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(shipCategoryLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(shipnameLabel);

        // Right side: input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        companyIdField = new JTextField();
        shipCategoryField = new JTextField();
        shipNameField = new JTextField();

        // Set fixed size for input fields
        Dimension inputSize = new Dimension(200, 25);
        companyIdField.setPreferredSize(inputSize);
        companyIdField.setMaximumSize(inputSize);
        shipCategoryField.setPreferredSize(inputSize);
        shipCategoryField.setMaximumSize(inputSize);
        shipNameField.setPreferredSize(inputSize);
        shipNameField.setMaximumSize(inputSize);

        inputPanel.add(companyIdField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(shipCategoryField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(shipNameField);

        // Adding label and input panels to formPanel
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
                new String[] { "ID", "Company ID", "Ship Category ID", "Ship Name", "Updated At" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Button actions
        addButton.addActionListener(e -> addShip());
        editButton.addActionListener(e -> editShip());
        deleteButton.addActionListener(e -> deleteShip());
        viewButton.addActionListener(e -> refreshTable());
        searchButton.addActionListener(e -> searchShip());

        // Wrapper for titleLabel & searchPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Center content wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BorderLayout()); // Stack elements vertically
        centerWrapper.add(centerPanel, BorderLayout.NORTH);
        centerWrapper.add(buttonPanel, BorderLayout.CENTER); // Move button panel below input fields
        
        // Middle wrapper
        JPanel middleWrapper = new JPanel();
        middleWrapper.setLayout(new BorderLayout());
        middleWrapper.add(centerWrapper, BorderLayout.NORTH);
        middleWrapper.add(scrollPane, BorderLayout.CENTER);

        // Adding components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(middleWrapper, BorderLayout.CENTER);

    }

    // Adds a new ship info to the database
    private void addShip() {
        String companyID = companyIdField.getText();
        String Category = shipCategoryField.getText();
        String ShipName = shipNameField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("INSERT INTO ships (company_id, category_id, ship_name) VALUES (?, ?, ?)")) {
            stmt.setString(1, companyID);
            stmt.setString(2, Category);
            stmt.setString(3, ShipName);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Added!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Edits the selected ship info record
    private void editShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a company to edit.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        String companyID = companyIdField.getText();
        String Category = shipCategoryField.getText();
        String ShipName = shipNameField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE ships SET company_id = ?, category_id = ?, ship_name = ? WHERE id = ?")) {
            stmt.setString(1, companyID);
            stmt.setString(2, Category);
            stmt.setString(3, ShipName);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Edited!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Deletes the selected ship info record
    private void deleteShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a ship to remove.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM ships WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ship Info Successfully Removed!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Searches for a ship info based on user input
    private void searchShip() {
        String keyword = searchField.getText();
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ships WHERE ship_name LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getInt("company_id"), rs.getInt("category_id"),
                        rs.getString("ship_name"), rs.getTimestamp("updated_time") });
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ships");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getInt("company_id"), rs.getInt("category_id"),
                        rs.getString("ship_name"), rs.getTimestamp("updated_time") });
            }
            System.out.println("Data fetched successfully! Rows: " + tableModel.getRowCount());
        } catch (SQLException ex) {
            System.err.println("Database connection failed!");
            ex.printStackTrace();
        }
    }
}