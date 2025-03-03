import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyInfo extends JPanel{
    private DefaultTableModel tableModel;

    public CompanyInfo() {
        setLayout(new BorderLayout());

        // JLabel for the Title
        JLabel titleLabel = new JLabel("Company Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // JLabel and JField for Company Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField nameField = new JTextField();
        
        // JLabel and JField for Billing Address
        JLabel billingLabel = new JLabel("Billing Address:");
        billingLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField billingField = new JTextField();

        // JField for Search
        JTextField searchField = new JTextField();

        // This is to create the table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Company Name");
        tableModel.addColumn("Billing Address");
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table); //This is to add scrollong to the table

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(50, 120, 200)); // Light Blue
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String billing = billingField.getText();
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO company (company_name, billing_address) VALUES (?, ?)");
                    stmt.setString(1, name);
                    stmt.setString(2, billing);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Company Successfully Added!");
                    refreshTable();
                } catch (SQLException ex) {
                ex.printStackTrace();
                }
            }
        });

        // Edit Button
        JButton editButton = new JButton("Edit");
        editButton.setBackground(new Color(50, 120, 200)); // Light Blue
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please pick the Company to Edit");
                    return;
                }
                int id = (int) table.getValueAt(selectedRow, 0);
                String name = nameField.getText();
                String billing = billingField.getText();

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                    PreparedStatement stmt = conn.prepareStatement("UPDATE company SET company_name = ?, billing_address = ? WHERE id = ?");
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
        });

        // Delete Button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(50, 120, 200)); // Light Blue
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please pick the Company to Delete");
                    return;
                }
                int id = (int) table.getValueAt(selectedRow, 0);

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM company WHERE id = ?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Company Successfully Deleted!");
                    refreshTable();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // View Button
        JButton viewButton = new JButton("View");
        viewButton.setBackground(new Color(50, 120, 200)); // Light Blue
        viewButton.setForeground(Color.WHITE);
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM company");
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("company_name");
                        String billing = rs.getString("billing_address");
                        tableModel.addRow(new Object[]{id, name, billing});
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(50, 120, 200)); // Light Blue
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                tableModel.setRowCount(0);

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM company WHERE company_name LIKE ?");
                    stmt.setString(1, "%" + keyword + "%");
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("company_name");
                        String billing = rs.getString("billing_address");
                        tableModel.addRow(new Object[]{id, name, billing});
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Adding the components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(viewButton, BorderLayout.LINE_END);
        add(scrollPane, BorderLayout.SOUTH);
    }
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear tabel sebelum menambahkan data baru
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM company");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String billing = rs.getString("name");
                tableModel.addRow(new Object[]{id, name, billing});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}