import javax.swing.*;
import java.awt.*;

public class ShipInfo extends JPanel {
    public ShipInfo() {
        setLayout(new BorderLayout());

        // Setting a JLabel as the title
        JLabel titleLabel = new JLabel("Ship Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Column Names for the Table
        String[] columnNames = {"id","Company ID","Ship Name", "Ship Category", "Updated At"};

        // Sample Data for the Table (for now)
        Object [][] data = {
            {"1", "1", "USS Gyatt", "USS Gyatt", "01-03-2025"}
        };

        // This is to create the Table
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table); // This is to add a scrolling feature
        
        // Adding components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
