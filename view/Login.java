package view;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.sql.*;

public class Login extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public Login() {
        UITheme.applyGlobalDefaults();

        setTitle("Gaming Center – Login");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Root panel – side by side
        JPanel root = new JPanel(new GridLayout(1, 2, 0, 0));

        // ── LEFT PANEL: Branding ──────────────────────────────────────────────
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0x0D, 0x0D, 0x2E),
                        getWidth(), getHeight(), new Color(0x3B, 0x1D, 0x8B));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(0x7C, 0x3A, 0xED, 40));
                g2.fillOval(-60, -60, 300, 300);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 250, 250);

                g2.setColor(new Color(0x22, 0xD3, 0xEE, 25));
                g2.fillOval(30, getHeight() - 160, 160, 160);
            }
        };
        leftPanel.setLayout(new GridBagLayout());

        JLabel ico = new JLabel("🎮");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        ico.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel("GAMING CENTER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.ACCENT_CYAN);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(0xB0, 0xC0, 0xD0));
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.add(ico);
        leftContent.add(Box.createVerticalStrut(14));
        leftContent.add(title);
        leftContent.add(Box.createVerticalStrut(6));
        leftContent.add(sub);
        leftPanel.add(leftContent);

        // ── RIGHT PANEL: Login Card ───────────────────────────────────────────
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(UITheme.BG_DARK);
        rightPanel.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(UITheme.BG_SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(UITheme.FONT_TITLE);
        loginTitle.setForeground(UITheme.TEXT_PRIMARY);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginSub = new JLabel("Sign in to continue");
        loginSub.setFont(UITheme.FONT_SMALL);
        loginSub.setForeground(UITheme.TEXT_SECONDARY);
        loginSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(UITheme.FONT_HEADER);
        lblUser.setForeground(UITheme.TEXT_SECONDARY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUser = new JTextField(18);
        UITheme.styleTextField(txtUser);
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UITheme.FONT_HEADER);
        lblPass.setForeground(UITheme.TEXT_SECONDARY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPass = new JPasswordField(18);
        UITheme.stylePasswordField(txtPass);
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        btnLogin = UITheme.createButton("LOGIN", UITheme.ACCENT_PRIMARY);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> login());

        // Enter key triggers login
        txtPass.addActionListener(e -> login());

        card.add(loginTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(loginSub);
        card.add(Box.createVerticalStrut(28));
        card.add(lblUser);
        card.add(Box.createVerticalStrut(6));
        card.add(txtUser);
        card.add(Box.createVerticalStrut(16));
        card.add(lblPass);
        card.add(Box.createVerticalStrut(6));
        card.add(txtPass);
        card.add(Box.createVerticalStrut(24));
        card.add(btnLogin);

        rightPanel.add(card);
        root.add(leftPanel);
        root.add(rightPanel);

        setContentPane(root);
        setVisible(true);
    }

    private void login() {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtUser.getText());
            ps.setString(2, new String(txtPass.getPassword()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new MainMenu();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
