package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        UITheme.applyGlobalDefaults();
        setTitle("Gaming Center Management – Dashboard");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        // ── SIDEBAR ───────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UITheme.BG_SURFACE);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER_COLOR));

        // Logo block
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(UITheme.BG_SURFACE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(28, 20, 24, 20));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel logoIco = new JLabel("🎮");
        logoIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        logoIco.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoText = new JLabel("Gaming Center");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoText.setForeground(UITheme.TEXT_PRIMARY);
        logoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoSub = new JLabel("Management System");
        logoSub.setFont(UITheme.FONT_SMALL);
        logoSub.setForeground(UITheme.TEXT_SECONDARY);
        logoSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(logoIco);
        logoPanel.add(Box.createVerticalStrut(6));
        logoPanel.add(logoText);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(logoSub);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER_COLOR);
        sep.setBackground(UITheme.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Nav section label
        JLabel navLabel = new JLabel("  NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(UITheme.TEXT_SECONDARY);
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLabel.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 0));
        navLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Nav buttons
        String[][] navItems = {
                { "👥", "Customer", "Manage customers" },
                { "🎲", "Game", "Manage games" },
                { "📅", "Booking", "New bookings" },
                { "💳", "Payment", "Process payments" },
        };

        sidebar.add(logoPanel);
        sidebar.add(sep);
        sidebar.add(navLabel);

        for (String[] item : navItems) {
            sidebar.add(createNavButton(item[0], item[1]));
        }

        sidebar.add(Box.createVerticalGlue());

        // Exit button at bottom
        JButton btnExit = createNavButton("🚪", "Exit");
        btnExit.addActionListener(e -> System.exit(0));
        sidebar.add(btnExit);
        sidebar.add(Box.createVerticalStrut(16));

        // Wire navigation (re-use listeners on the matching buttons)
        registerNavActions(sidebar);

        // ── CONTENT AREA ─────────────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG_DARK);

        // Top bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        topBar.setBackground(UITheme.BG_SURFACE);
        topBar.setPreferredSize(new Dimension(0, 56));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR));

        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
        topBar.add(pageTitle);

        // Welcome + stat cards
        JPanel welcomeArea = new JPanel();
        welcomeArea.setBackground(UITheme.BG_DARK);
        welcomeArea.setLayout(new BoxLayout(welcomeArea, BoxLayout.Y_AXIS));
        welcomeArea.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));

        JLabel welcome = new JLabel("Welcome to Gaming Center 🎮");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(UITheme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Select a module from the sidebar to get started.");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomeArea.add(welcome);
        welcomeArea.add(Box.createVerticalStrut(8));
        welcomeArea.add(sub);
        welcomeArea.add(Box.createVerticalStrut(36));

        // Stat cards row
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(UITheme.BG_DARK);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        cards.add(makeStatCard("👥", "Customers", "Manage", UITheme.ACCENT_PRIMARY));
        cards.add(makeStatCard("🎲", "Games", "Available", UITheme.ACCENT_CYAN));
        cards.add(makeStatCard("📅", "Bookings", "Today", new Color(0xF59E0B)));
        cards.add(makeStatCard("💳", "Payments", "Processed", UITheme.SUCCESS));

        welcomeArea.add(cards);

        content.add(topBar, BorderLayout.NORTH);
        content.add(welcomeArea, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JButton createNavButton(String icon, String label) {
        JButton btn = new JButton(icon + "  " + label);
        btn.setName(label); // store label for lookup
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(UITheme.TEXT_SECONDARY);
        btn.setBackground(UITheme.BG_SURFACE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0x7C, 0x3A, 0xED, 60));
                btn.setForeground(UITheme.TEXT_PRIMARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(UITheme.BG_SURFACE);
                btn.setForeground(UITheme.TEXT_SECONDARY);
            }
        });
        return btn;
    }

    private void registerNavActions(JPanel sidebar) {
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                String name = btn.getName();
                if (name == null)
                    continue;
                switch (name) {
                    case "Customer":
                        btn.addActionListener(e -> new CustomerForm());
                        break;
                    case "Game":
                        btn.addActionListener(e -> new GameForm());
                        break;
                    case "Booking":
                        btn.addActionListener(e -> new BookingForm());
                        break;
                    case "Payment":
                        btn.addActionListener(e -> new PaymentForm());
                        break;
                }
            }
        }
    }

    private JPanel makeStatCard(String icon, String title, String sub, Color accent) {
        JPanel card = new JPanel();
        card.setBackground(UITheme.BG_SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel icoLbl = new JLabel(icon);
        icoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        icoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(accent);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(UITheme.FONT_SMALL);
        subLbl.setForeground(UITheme.TEXT_SECONDARY);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(icoLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(subLbl);
        return card;
    }
}