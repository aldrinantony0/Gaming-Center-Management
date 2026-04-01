package view;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerForm extends JFrame {

    private JTextField txtId, txtName, txtPhone, txtEmail;
    private JButton btnInsert, btnUpdate, btnDelete, btnView;
    private JTable table;
    private DefaultTableModel model;

    public CustomerForm() {
        UITheme.applyGlobalDefaults();
        setTitle("Customer Management");
        setSize(920, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ── HEADER ───────────────────────────────────────────────────────────
        JLabel header = new JLabel("👥  Customer Management");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(UITheme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        root.add(header, BorderLayout.NORTH);

        // ── FORM CARD ─────────────────────────────────────────────────────────
        JPanel formCard = UITheme.createTitledPanel("Customer Details");
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 8, 6, 8);

        txtId = new JTextField();
        txtName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        UITheme.styleTextField(txtId);
        UITheme.styleTextField(txtName);
        UITheme.styleTextField(txtPhone);
        UITheme.styleTextField(txtEmail);
        txtId.setPreferredSize(new Dimension(180, 36));

        String[] labels = { "Customer ID:", "Name:", "Phone:", "Email:" };
        JTextField[] fields = { txtId, txtName, txtPhone, txtEmail };

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 0.0;
            gc.ipadx = 0;
            JLabel lbl = UITheme.createLabel(labels[i]);
            lbl.setPreferredSize(new Dimension(110, 36));
            formCard.add(lbl, gc);

            gc.gridx = 1;
            gc.weightx = 1.0;
            formCard.add(fields[i], gc);

            if (i < labels.length - 1) {
                gc.gridx = 2;
                gc.weightx = 0.05;
                formCard.add(Box.createHorizontalStrut(16), gc);
            }
        }

        // Button row
        btnInsert = UITheme.createButton("＋ Insert", UITheme.ACCENT_PRIMARY);
        btnUpdate = UITheme.createButton("✎  Update", new Color(0x0E, 0x7C, 0x86));
        btnDelete = UITheme.createButton("✕  Delete", UITheme.DANGER);
        btnView = UITheme.createButton("⟳  Refresh", new Color(0x1E, 0x6A, 0x3E));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.BG_SURFACE);
        btnRow.add(btnInsert);
        btnRow.add(btnUpdate);
        btnRow.add(btnDelete);
        btnRow.add(btnView);

        gc.gridx = 0;
        gc.gridy = labels.length;
        gc.gridwidth = 3;
        gc.weightx = 1.0;
        gc.insets = new Insets(10, 0, 0, 0);
        formCard.add(btnRow, gc);

        // ── TABLE ─────────────────────────────────────────────────────────────
        model = new DefaultTableModel(
                new String[] { "ID", "Name", "Phone", "Email" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        UITheme.styleTable(table);
        JScrollPane scroll = UITheme.createScrollPane(table);

        // Click on row → populate fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtPhone.setText(model.getValueAt(row, 2).toString());
                txtEmail.setText(model.getValueAt(row, 3).toString());
            }
        });

        // ── LAYOUT ───────────────────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_DARK);
        center.add(formCard, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // ── ACTIONS ──────────────────────────────────────────────────────────
        btnInsert.addActionListener(e -> insertCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnView.addActionListener(e -> viewCustomers());

        setContentPane(root);
        setVisible(true);
        viewCustomers();
    }

    // ── CRUD methods (unchanged logic) ────────────────────────────────────────

    private void insertCustomer() {
        String sql = "INSERT INTO customers VALUES(customers_seq.NEXTVAL,?,?,?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtName.getText());
            ps.setString(2, txtPhone.getText());
            ps.setString(3, txtEmail.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer inserted successfully.");
            clearFields();
            viewCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCustomer() {
        String sql = "UPDATE customers SET name=?, phone=?, email=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtName.getText());
            ps.setString(2, txtPhone.getText());
            ps.setString(3, txtEmail.getText());
            ps.setInt(4, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer updated.");
            clearFields();
            viewCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a customer first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer ID " + txtId.getText() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer deleted.");
            clearFields();
            viewCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewCustomers() {
        model.setRowCount(0);
        String sql = "SELECT * FROM customers";
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }
}