import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminForm extends JFrame {
    private JPanel contentPanel;
    public AdminForm() {
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // Panel Utama
        JPanel mainPanel = new JPanel(new BorderLayout());
 
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 120, 200)); // Warna biru muda
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
 
        // Tombol Logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setBackground(new Color(200, 50, 50)); // Warna merah
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose(); // Tutup dashboard
            }
        });
        mainPanel.add(headerPanel, BorderLayout.NORTH);
 
        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(70, 130, 180)); // Warna biru muda lebih gelap
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
 
        // Tombol Navigasi
        String[] menuItems = {"Transaction Information", "Tariff", "Ship Information", "Company Information", "Ship Category"};
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.setMaximumSize(new Dimension(180, 180));
            menuButton.setBackground(new Color(90, 150, 210)); // Warna biru muda
            menuButton.setForeground(Color.WHITE);
            menuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tampilkanForm(item); // Tampilkan form sesuai tombol yang diklik
                }
            });
            sidebarPanel.add(menuButton);
            sidebarPanel.add(logoutButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }    
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
            
        // Content Area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Tampilkan form default saat pertama kali dibuka
        //tampilkanForm("Informasi");
        add(mainPanel);
    }

    // Method untuk menampilkan form sesuai tombol yang diklik
    private void tampilkanForm(String formName) {
        contentPanel.removeAll(); // Hapus konten sebelumnya
        switch (formName) {
            case "Produk":
            //contentPanel.add(new FormProduk(), BorderLayout.CENTER);
            break;
            case "Manajemen User":
            //contentPanel.add(new FormManajemenUser(), BorderLayout.CENTER);
            break;
            case "Informasi":
            //contentPanel.add(new FormInformasi(), BorderLayout.CENTER);
            break;
            case "Laporan":
            //contentPanel.add(new FormLaporan(), BorderLayout.CENTER);
            break;
            case "Pencarian":
            //contentPanel.add(new FormPencarian(), BorderLayout.CENTER);
            break;   
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }
            
    public static void main(String[] args) {
        new AdminForm().setVisible(true);       
    }
}