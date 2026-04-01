package view;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GameForm extends JFrame {

    private JTextField txtId, txtName, txtType, txtPrice;
    private JTable table;
    private DefaultTableModel model;

    public GameForm() {
        UITheme.applyGlobalDefaults();
        setTitle("Game Management");
        setSize(880, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ── HEADER ───────────────────────────────────────────────────────────
        JLabel header = new JLabel("🎲  Game Management");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(UITheme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        root.add(header, BorderLayout.NORTH);

        // ── FORM CARD ─────────────────────────────────────────────────────────
        JPanel formCard = UITheme.createTitledPanel("Game Details");
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 8, 6, 8);

        txtId = new JTextField();
        txtName = new JTextField();
        txtType = new JTextField();
        txtPrice = new JTextField();
        UITheme.styleTextField(txtId);
        UITheme.styleTextField(txtName);
        UITheme.styleTextField(txtType);
        UITheme.styleTextField(txtPrice);

        String[] labels = { "Game ID:", "Game Name:", "Game Type:", "Price / Hour (₹):" };
        JTextField[] fields = { txtId, txtName, txtType, txtPrice };

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 0.0;
            JLabel lbl = UITheme.createLabel(labels[i]);
            lbl.setPreferredSize(new Dimension(130, 36));
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

        JButton btnInsert = UITheme.createButton("＋ Insert", UITheme.ACCENT_PRIMARY);
        JButton btnDelete = UITheme.createButton("✕  Delete", UITheme.DANGER);
        JButton btnView = UITheme.createButton("⟳  Refresh", new Color(0x1E, 0x6A, 0x3E));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(UITheme.BG_SURFACE);
        btnRow.add(btnInsert);
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
                new String[] { "ID", "Name", "Type", "Price / Hour" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        UITheme.styleTable(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtType.setText(model.getValueAt(row, 2).toString());
                txtPrice.setText(model.getValueAt(row, 3).toString());
            }
        });

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_DARK);
        center.add(formCard, BorderLayout.NORTH);
        center.add(UITheme.createScrollPane(table), BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        btnInsert.addActionListener(e -> insertGame());
        btnDelete.addActionListener(e -> deleteGame());
        btnView.addActionListener(e -> viewGame());

        setContentPane(root);
        setVisible(true);
        viewGame();
    }

    // ── CRUD methods ──────────────────────────────────────────────────────────

    private void insertGame() {
        String sql = "INSERT INTO games VALUES(games_seq.NEXTVAL,?,?,?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtName.getText());
            ps.setString(2, txtType.getText());
            ps.setDouble(3, Double.parseDouble(txtPrice.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Game inserted.");
            clearFields();
            viewGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteGame() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a game first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete game ID " + txtId.getText() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        String sql = "DELETE FROM games WHERE game_id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Game deleted.");
            clearFields();
            viewGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGame() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM games")) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getDouble(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtType.setText("");
        txtPrice.setText("");
    }
}