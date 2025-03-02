import javax.swing.*;
import java.awt.*;

public class TransactionInfo extends JPanel {
    public TransactionInfo() {
        setLayout(new BorderLayout());

        // JLabel for title
        JLabel titleLabel = new JLabel("Transaction Info", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Column Names for the Table
        String[] columnNames = {"id","Tariff ID", "Ship ID", "User ID", "Arrival Date", "Departure Date", "Total Amount", "Transaction Date"};

        // Temporary sample data for the table
        Object[][] data = {
            {"1", "2", "3", "1", "02-03-2025", "08-03-2025", "$500000", "15:25:13 02-03-2025"}
        };

        // This is to create the table
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Addming the components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
