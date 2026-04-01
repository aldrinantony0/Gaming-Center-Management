package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UITheme – Neon Arena Design System
 * Centralised constants and helpers for the Gaming Center Management UI.
 */
public class UITheme {

    // ─── Colour Palette ────────────────────────────────────────────────────────
    public static final Color BG_DARK = new Color(0x0D, 0x11, 0x17); // #0D1117
    public static final Color BG_SURFACE = new Color(0x16, 0x1B, 0x22); // #161B22
    public static final Color BG_SURFACE2 = new Color(0x1C, 0x21, 0x28); // #1C2128
    public static final Color ACCENT_PRIMARY = new Color(0x7C, 0x3A, 0xED); // #7C3AED violet
    public static final Color ACCENT_HOVER = new Color(0x9F, 0x5E, 0xFF); // #9F5EFF
    public static final Color ACCENT_CYAN = new Color(0x22, 0xD3, 0xEE); // #22D3EE
    public static final Color SUCCESS = new Color(0x10, 0xB9, 0x81); // #10B981
    public static final Color DANGER = new Color(0xF4, 0x3F, 0x5E); // #F43F5E
    public static final Color TEXT_PRIMARY = new Color(0xF0, 0xF6, 0xFC); // #F0F6FC
    public static final Color TEXT_SECONDARY = new Color(0x8B, 0x94, 0x9E); // #8B949E
    public static final Color BORDER_COLOR = new Color(0x30, 0x36, 0x3D); // #30363D

    // ─── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);

    // ─── Global Look-and-Feel Defaults ─────────────────────────────────────────
    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.background", BG_SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", ACCENT_PRIMARY);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", FONT_BTN);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("TextField.background", BG_SURFACE2);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", ACCENT_CYAN);
        UIManager.put("PasswordField.background", BG_SURFACE2);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", ACCENT_CYAN);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Viewport.background", BG_DARK);
        UIManager.put("Table.background", BG_SURFACE);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", ACCENT_PRIMARY);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("TableHeader.background", BG_SURFACE2);
        UIManager.put("TableHeader.foreground", ACCENT_CYAN);
        UIManager.put("ScrollBar.thumb", BORDER_COLOR);
        UIManager.put("ScrollBar.track", BG_SURFACE);
    }

    // ─── Button Styling ────────────────────────────────────────────────────────

    /** Creates a fully styled button with hover effect. */
    public static JButton createButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(accent);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));

        Color hoverColor = accent.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(accent);
            }
        });
        return btn;
    }

    /** Styles an existing JButton in-place. */
    public static void styleButton(JButton btn, Color accent) {
        btn.setFont(FONT_BTN);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(accent);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));

        Color hoverColor = accent.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(accent);
            }
        });
    }

    // ─── Field Styling ─────────────────────────────────────────────────────────

    public static void styleTextField(JTextField field) {
        field.setBackground(BG_SURFACE2);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_CYAN);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setOpaque(true);
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setBackground(BG_SURFACE2);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_CYAN);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setOpaque(true);
    }

    // ─── Label Styling ─────────────────────────────────────────────────────────

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    // ─── Panel Helpers ─────────────────────────────────────────────────────────

    /** Returns a panel with a coloured titled border. */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(BG_SURFACE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_PRIMARY, 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_HEADER,
                ACCENT_CYAN);
        panel.setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return panel;
    }

    // ─── Table Styling ─────────────────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setForeground(TEXT_PRIMARY);
        table.setBackground(BG_SURFACE);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ACCENT_PRIMARY);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setOpaque(true);

        // Alternate row colouring via custom renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setFont(FONT_BODY);
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (isSelected) {
                    setBackground(ACCENT_PRIMARY);
                } else {
                    setBackground(row % 2 == 0 ? BG_SURFACE : BG_SURFACE2);
                }
                return this;
            }
        });

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setForeground(ACCENT_CYAN);
        header.setBackground(BG_SURFACE2);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_PRIMARY));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);
    }

    public static JScrollPane createScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return scroll;
    }

    // ─── Frame Setup ───────────────────────────────────────────────────────────

    public static void setupFrame(JFrame frame, String title, int w, int h) {
        frame.setTitle(title);
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_DARK);
    }
}
