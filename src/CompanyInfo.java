import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CompanyInfo extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField nameField, billingField, searchField;
    private JTable table;

    public CompanyInfo() {
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Company Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Form Panel using BorderLayout for better alignment
        JPanel formPanel = new JPanel(new BorderLayout(10, 10)); // Adds spacing

        // Left side: Labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Company Name:");
        JLabel billingLabel = new JLabel("Billing Address:");
        labelPanel.add(nameLabel);
        labelPanel.add(Box.createVerticalStrut(20)); // Adds spacing
        labelPanel.add(billingLabel);

        // Right side: Input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        nameField = new JTextField();
        billingField = new JTextField();

        // Set fixed size for input fields
        Dimension inputSize = new Dimension(200, 25);
        nameField.setPreferredSize(inputSize);
        nameField.setMaximumSize(inputSize); // Prevents stretching
        billingField.setPreferredSize(inputSize);
        billingField.setMaximumSize(inputSize);

        inputPanel.add(nameField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(billingField);

        // Add both panels to formPanel
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
        tableModel = new DefaultTableModel(new String[] { "ID", "Company Name", "Billing Address" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Button Actions
        addButton.addActionListener(_ -> addCompany());
        editButton.addActionListener(_ -> editCompany());
        deleteButton.addActionListener(_ -> deleteCompany());
        viewButton.addActionListener(_ -> refreshTable());
        searchButton.addActionListener(_ -> searchCompany());

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

    /** Adds a new company to the database */
    private void addCompany() {
        String name = nameField.getText();
        String billing = billingField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("INSERT INTO company (company_name, billing_address) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, billing);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Added!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Edits the selected company record */
    private void editCompany() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a company to edit.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String billing = billingField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE company SET company_name = ?, billing_address = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setString(2, billing);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Edited!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Deletes the selected company record */
    private void deleteCompany() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a company to delete.");
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM company WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Company Successfully Deleted!");
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Searches for a company based on user input */
    private void searchCompany() {
        String keyword = searchField.getText();
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM company WHERE company_name LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getString("company_name"),
                        rs.getString("billing_address") });
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM company");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("id"), rs.getString("company_name"),
                        rs.getString("billing_address") });
            }
            System.out.println("Data fetched successfully! Rows: " + tableModel.getRowCount());
        } catch (SQLException ex) {
            System.err.println("Database connection failed!");
            ex.printStackTrace();
        }
    }
}
