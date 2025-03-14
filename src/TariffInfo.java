import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TariffInfo extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField shipCategoryField, tariffAmountField, searchField;
    private JTable table;

    public TariffInfo() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Tariff Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // formPanel using BorderLayout for better alignment
        JPanel formPanel = new JPanel(new BorderLayout(10, 10)); // Adds spacing

        // left side: labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel shipCategoryLabel = new JLabel("Ship Category:");
        JLabel tariffAmountLabel = new JLabel("Tariff Amount:");
        labelPanel.add(shipCategoryLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(tariffAmountLabel);

        // Right side: input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        shipCategoryField = new JTextField();
        tariffAmountField = new JTextField();

        // Set fixed size for input fields
        Dimension inputSize = new Dimension(200, 25);
        shipCategoryField.setPreferredSize(inputSize);
        shipCategoryField.setMaximumSize(inputSize);
        tariffAmountField.setPreferredSize(inputSize);
        tariffAmountField.setMaximumSize(inputSize);

        inputPanel.add(shipCategoryField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(tariffAmountField);

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
                new String[] { "ID", "Category ID", "Tariff Amount" }, 0);
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

    // Adds a new tariff to the database
    private void addShip() {
        String Category = shipCategoryField.getText();
        String Tariff = tariffAmountField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("INSERT INTO tariff (category_id, Tariff_Amount) VALUES (?, ?)")) {
            stmt.setString(1, Category);
            stmt.setString(2, Tariff);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tariff Successfully Added!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Edits the selected tariff record
    private void editShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a tariff to edit.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        String Category = shipCategoryField.getText();
        String Tariff = tariffAmountField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE tariff SET category_id = ?, Tariff_Amount = ? WHERE id = ?")) {
            stmt.setString(1, Category);
            stmt.setString(2, Tariff);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tariff Successfully Edited!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Deletes the selected tariff record
    private void deleteShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a tariff to remove.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM tariff WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tariff Successfully Removed!");
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
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tariff WHERE Tariff_Amount LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(
                        new Object[] { rs.getInt("id"), rs.getInt("category_id"), rs.getString("Tariff_Amount") });
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tariff");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(
                        new Object[] { rs.getInt("id"), rs.getInt("category_id"), rs.getString("Tariff_Amount") });
            }
            System.out.println("Data fetched successfully! Rows: " + tableModel.getRowCount());
        } catch (SQLException ex) {
            System.err.println("Database connection failed!");
            ex.printStackTrace();
        }
    }
}