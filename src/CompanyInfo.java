import javax.swing.*;
import java.awt.*;

public class CompanyInfo extends JPanel{
    public CompanyInfo() {
        setLayout(new BorderLayout());

        // Jlabel for the title
        JLabel titleLabel = new JLabel("Company Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Column names for the Table
        String[] columnNames = {"id", "Company Name", "Billing Address"};

        // Temporary Sample Data for the table
        Object[][] data = {
            {"1", "International Shipping Co", "Billing Address"}
        };

        // This is to create the table 
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table); //This is to add scrollong to the table

        // Adding the components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
