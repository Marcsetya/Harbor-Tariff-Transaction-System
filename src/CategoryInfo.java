import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CategoryInfo extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField shipCategoryField, searchField;
    private JTable table;

    public CategoryInfo() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Ship Categories", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Form Panel using BorderLayout for better alignment
        JPanel formPanel = new JPanel(new BorderLayout(10, 10)); // Adds spacing

        // left side: Labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel shipCategoryLabel = new JLabel("Ship Category:");
        labelPanel.add(shipCategoryLabel);

        // Right side: Input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        shipCategoryField = new JTextField();

        // Set fixed size for input fields
        Dimension inputSize = new Dimension(200, 25);
        shipCategoryField.setPreferredSize(inputSize);
        shipCategoryField.setMaximumSize(inputSize); // Prevents stretching

        inputPanel.add(shipCategoryField);

        // Adding label and input panel to the formPanel
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

        // Declaring the Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View");
        JButton searchButton = new JButton("Search");

        // Styling Buttons
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
        tableModel = new DefaultTableModel(new String[] { "ID", "Ship Category Name" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Button Actions
        addButton.addActionListener(_ -> addShipCategory());
        editButton.addActionListener(_ -> editShipCategory());
        deleteButton.addActionListener(_ -> deleteShipCategory());
        // viewButton.addActionListener(_ -> refreshShipCategory());
        searchButton.addActionListener(_ -> searchShipCategory());

        // Wrapper for Title & SearchPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Center content wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BorderLayout()); // Stack elements vertically
        centerWrapper.add(centerPanel, BorderLayout.NORTH);
        centerWrapper.add(buttonPanel, BorderLayout.CENTER); // Move button panel below input fields

        // Adding Components to Panel
        add(topPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    /** Adds a new ship category to the database */
    private void addShipCategory() {
        String shipCategory = shipCategoryField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("INSERT INTO category (ship_size) VALUES (?)")) {
            stmt.setString(1, shipCategory);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Added!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Edits the selected ship category record */
    private void editShipCategory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a ship category to edit.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        String shipCategory = shipCategoryField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE category SET ship_size = ? WHERE id = ?")) {
            stmt.setString(1, shipCategory);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ship Category Successfully Edited!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Deletes the selected ship Size record */
    private void deleteShipCategory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a company to delete.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM category WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ship Category Successfully Deleted!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Searches for a ship Category based on user input */
    private void searchShipCategory() {
        String keyword = searchField.getText();
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM category WHERE ship_size LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getString("ship_size") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Refreshes the table data */
    private void refreshTable() {
        tableModel.setRowCount(0);
        System.out.println("Refreshing table..."); // Debugging log

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "")) {
            System.out.println("Database connected successfully!"); // Debugging log
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM category");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getString("ship_size") });
            }
            System.out.println("Data fetched successfully! Rows: " + tableModel.getRowCount());
        } catch (SQLException ex) {
            System.err.println("Database connection failed!");
            ex.printStackTrace();
        }
    }
}
