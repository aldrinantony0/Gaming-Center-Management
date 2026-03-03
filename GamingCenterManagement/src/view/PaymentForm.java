package view;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentForm extends JFrame {

    private JTextField txtBookingId, txtAmount;
    private JTable table;
    private DefaultTableModel model;

    public PaymentForm() {
        UITheme.applyGlobalDefaults();
        setTitle("Payment Management");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ── HEADER ───────────────────────────────────────────────────────────
        JLabel header = new JLabel("💳  Payment Management");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(UITheme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        root.add(header, BorderLayout.NORTH);

        // ── FORM CARD ─────────────────────────────────────────────────────────
        JPanel formCard = UITheme.createTitledPanel("Process Payment");
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 8, 6, 8);

        txtBookingId = new JTextField();
        txtAmount = new JTextField();
        UITheme.styleTextField(txtBookingId);
        UITheme.styleTextField(txtAmount);

        String[] lbls = { "Booking ID:", "Amount (₹):" };
        JTextField[] flds = { txtBookingId, txtAmount };

        for (int i = 0; i < lbls.length; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 0.0;
            JLabel lbl = UITheme.createLabel(lbls[i]);
            lbl.setPreferredSize(new Dimension(110, 36));
            formCard.add(lbl, gc);
            gc.gridx = 1;
            gc.weightx = 1.0;
            formCard.add(flds[i], gc);
            if (i < lbls.length - 1) {
                gc.gridx = 2;
                gc.weightx = 0.05;
                formCard.add(Box.createHorizontalStrut(16), gc);
            }
        }

        // Buttons
        JButton btnPay = UITheme.createButton("💳  Pay Now", UITheme.SUCCESS);
        JButton btnRefresh = UITheme.createButton("⟳  Refresh", new Color(0x1E, 0x6A, 0x3E));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.BG_SURFACE);
        btnRow.add(btnPay);
        btnRow.add(btnRefresh);

        gc.gridx = 0;
        gc.gridy = lbls.length;
        gc.gridwidth = 3;
        gc.weightx = 1.0;
        gc.insets = new Insets(10, 0, 0, 0);
        formCard.add(btnRow, gc);

        // ── TABLE – Full payment details via JOIN ─────────────────────────────
        model = new DefaultTableModel(
                new String[] { "Payment ID", "Booking ID", "Customer Name",
                        "Game Name", "Hours", "Amount (₹)", "Payment Date" },
                0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        UITheme.styleTable(table);

        // Click row → fill Booking ID
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                txtBookingId.setText(model.getValueAt(row, 1).toString());
            }
        });

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_DARK);
        center.add(formCard, BorderLayout.NORTH);
        center.add(UITheme.createScrollPane(table), BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // ── ACTIONS ──────────────────────────────────────────────────────────
        btnPay.addActionListener(e -> {
            insertPayment();
            viewPayments();
        });
        btnRefresh.addActionListener(e -> viewPayments());

        setContentPane(root);
        setVisible(true);
        viewPayments();
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    private void insertPayment() {
        String sql = "INSERT INTO payments (payment_id, booking_id, amount, payment_date) " +
                "VALUES (payments_seq.NEXTVAL, ?, ?, SYSDATE)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtBookingId.getText()));
            ps.setDouble(2, Double.parseDouble(txtAmount.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Payment Successful!");
            txtBookingId.setText("");
            txtAmount.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Payment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── VIEW – JOIN payments → bookings → customers → games ──────────────────
    private void viewPayments() {
        model.setRowCount(0);
        String sql = "SELECT p.payment_id, " +
                "       p.booking_id, " +
                "       c.name       AS customer_name, " +
                "       g.name       AS game_name, " +
                "       b.hours, " +
                "       p.amount, " +
                "       TO_CHAR(p.payment_date, 'DD-Mon-YYYY') AS pay_date " +
                "FROM payments p " +
                "JOIN bookings  b ON p.booking_id  = b.booking_id " +
                "JOIN customers c ON b.customer_id  = c.customer_id " +
                "JOIN games     g ON b.game_id      = g.game_id " +
                "ORDER BY p.payment_id DESC";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("payment_id"),
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("game_name"),
                        rs.getInt("hours"),
                        rs.getDouble("amount"),
                        rs.getString("pay_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}