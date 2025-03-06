import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Dashboard() {
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 120, 200)); // Light Blue
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setBackground(new Color(200, 50, 50)); // Red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose(); // Close dashboard
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(70, 130, 180)); // Dark Blue
        sidebarPanel.setPreferredSize(new Dimension(200, 0));

        // CardLayout for content switching
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create and add panels for each section
        JPanel transactionsPanel = new TransactionInfo(); // This one is using the TransactionInfo panel
        JPanel shipInfoPanel = new ShipInfo(); // This one is using the ShipInfo panel
        JPanel companyInfoPanel = new CompanyInfo(); // This one is using the CompanyInfo panel
        JPanel CategoryInfoPanel = new CategoryInfo(); // This one is using the CategoryInfo panel
        JPanel TariffInfoPanel = new TariffInfo(); // This one is using the TariffInfo panel

        // This is how to add card panels to the content panel
        contentPanel.add(transactionsPanel, "Transactions");
        contentPanel.add(shipInfoPanel, "Ship Information");
        contentPanel.add(companyInfoPanel, "Company Information");
        contentPanel.add(CategoryInfoPanel, "Ship Categories");
        contentPanel.add(TariffInfoPanel, "Tariff information");

        // Sidebar menu buttons
        String[] menuItems = { "Transactions", "Ship Information", "Company Information", "Ship Categories", "Tariff information" };
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.setMaximumSize(new Dimension(180, 40));
            menuButton.setBackground(new Color(90, 150, 210)); // Light Blue
            menuButton.setForeground(Color.WHITE);
            menuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, item); // Switch panel
                    System.out.println(item); // Debug message
                }
            });
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Add logout button at the bottom of the sidebar
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add panels to the main frame
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Show default panel
        cardLayout.show(contentPanel, "Transactions");

        // Debug message
        System.out.println("Default panel set to Transactions");

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}