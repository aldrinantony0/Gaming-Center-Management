package view;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BookingForm extends JFrame {

  private JTextField txtCustId, txtGameId, txtHours;
  private JTable table;
  private DefaultTableModel model;

  public BookingForm() {
    UITheme.applyGlobalDefaults();
    setTitle("Booking Management");
    setSize(920, 640);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel root = new JPanel(new BorderLayout(0, 12));
    root.setBackground(UITheme.BG_DARK);
    root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

    // ── HEADER ───────────────────────────────────────────────────────────
    JLabel header = new JLabel("📅  Booking Management");
    header.setFont(new Font("Segoe UI", Font.BOLD, 20));
    header.setForeground(UITheme.TEXT_PRIMARY);
    header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
    root.add(header, BorderLayout.NORTH);

    // ── FORM CARD ─────────────────────────────────────────────────────────
    JPanel formCard = UITheme.createTitledPanel("New Booking");
    formCard.setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.insets = new Insets(6, 8, 6, 8);

    txtCustId = new JTextField();
    txtGameId = new JTextField();
    txtHours = new JTextField();
    UITheme.styleTextField(txtCustId);
    UITheme.styleTextField(txtGameId);
    UITheme.styleTextField(txtHours);

    String[] lbls = { "Customer ID:", "Game ID:", "Hours:" };
    JTextField[] flds = { txtCustId, txtGameId, txtHours };

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
    JButton btnBook = UITheme.createButton("📅  Book Now", UITheme.ACCENT_PRIMARY);
    JButton btnRefresh = UITheme.createButton("⟳  Refresh", new Color(0x1E, 0x6A, 0x3E));

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    btnRow.setBackground(UITheme.BG_SURFACE);
    btnRow.add(btnBook);
    btnRow.add(btnRefresh);

    gc.gridx = 0;
    gc.gridy = lbls.length;
    gc.gridwidth = 3;
    gc.weightx = 1.0;
    gc.insets = new Insets(10, 0, 0, 0);
    formCard.add(btnRow, gc);

    // ── TABLE – Full booking details via JOIN ─────────────────────────────
    model = new DefaultTableModel(
        new String[] { "Booking ID", "Customer Name", "Game Name", "Hours" }, 0) {
      @Override
      public boolean isCellEditable(int r, int c) {
        return false;
      }
    };
    table = new JTable(model);
    UITheme.styleTable(table);

    // Click row → fill IDs into form
    table.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
        int row = table.getSelectedRow();
        // We stored cust id and game id in hidden model; re-query on click is simpler
      }
    });

    JPanel center = new JPanel(new BorderLayout(0, 14));
    center.setBackground(UITheme.BG_DARK);
    center.add(formCard, BorderLayout.NORTH);
    center.add(UITheme.createScrollPane(table), BorderLayout.CENTER);
    root.add(center, BorderLayout.CENTER);

    // ── ACTIONS ──────────────────────────────────────────────────────────
    btnBook.addActionListener(e -> {
      insertBooking();
      viewBookings();
    });
    btnRefresh.addActionListener(e -> viewBookings());

    setContentPane(root);
    setVisible(true);
    viewBookings();
  }

  // ── INSERT ────────────────────────────────────────────────────────────────
  private void insertBooking() {
    String sql = "INSERT INTO bookings (booking_id, customer_id, game_id, hours) " +
        "VALUES (bookings_seq.NEXTVAL, ?, ?, ?)";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, Integer.parseInt(txtCustId.getText()));
      ps.setInt(2, Integer.parseInt(txtGameId.getText()));
      ps.setInt(3, Integer.parseInt(txtHours.getText()));
      ps.executeUpdate();
      JOptionPane.showMessageDialog(this, "✅ Booking confirmed!");
      txtCustId.setText("");
      txtGameId.setText("");
      txtHours.setText("");
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
          "Booking Failed", JOptionPane.ERROR_MESSAGE);
    }
  }

  // ── VIEW – JOIN with customers and games ──────────────────────────────────
  private void viewBookings() {
    model.setRowCount(0);
    String sql = "SELECT b.booking_id, c.name AS customer_name, g.game_name AS game_name, b.hours " +
        "FROM bookings b " +
        "JOIN customers c ON b.customer_id = c.customer_id " +
        "JOIN games     g ON b.game_id     = g.game_id " +
        "ORDER BY b.booking_id DESC";
    try (Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql)) {
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("booking_id"),
            rs.getString("customer_name"),
            rs.getString("game_name"),
            rs.getInt("hours")
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this,
          "Failed to load bookings:\n" + e.getMessage(),
          "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}