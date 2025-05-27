package Components;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class CustomComboBox<T> extends JComboBox<T> {

    // Thuộc tính tuỳ chỉnh
    private Color defaultBg = Color.WHITE;
    private Color defaultFg = Color.BLACK;
    private Color selectedBg = new Color(173, 216, 230);
    private Color selectedFg = Color.WHITE;
    private Color dividerColor = Color.LIGHT_GRAY;
    private Border itemBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    private boolean showDivider = true;

    // Constructor nhận danh sách item
    public CustomComboBox(List<T> items) {
        super(new DefaultComboBoxModel<>((T[]) items.toArray()));
        initRenderer();
    }

    // Constructor không nhận sẵn item
    public CustomComboBox() {
        super();
        initRenderer();
    }

    private void initRenderer() {
        this.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);

                if (index >= 0) { // Dropdown list
                    if (isSelected) {
                        label.setBackground(selectedBg);
                        label.setForeground(selectedFg);
                    } else {
                        label.setBackground(defaultBg);
                        label.setForeground(defaultFg);
                    }

                    Border divider = BorderFactory.createMatteBorder(0, 0, showDivider ? 1 : 0, 0, dividerColor);
                    label.setBorder(BorderFactory.createCompoundBorder(divider, itemBorder));
                } else { // Ô chọn
                    label.setBackground(defaultBg);
                    label.setForeground(defaultFg);
                    label.setBorder(itemBorder);
                }

                return label;
            }
        });
    }

    // Getter và Setter

    public Color getDefaultBg() {
        return defaultBg;
    }

    public void setDefaultBg(Color defaultBg) {
        this.defaultBg = defaultBg;
        repaint();
    }

    public Color getDefaultFg() {
        return defaultFg;
    }

    public void setDefaultFg(Color defaultFg) {
        this.defaultFg = defaultFg;
        repaint();
    }

    public Color getSelectedBg() {
        return selectedBg;
    }

    public void setSelectedBg(Color selectedBg) {
        this.selectedBg = selectedBg;
        repaint();
    }

    public Color getSelectedFg() {
        return selectedFg;
    }

    public void setSelectedFg(Color selectedFg) {
        this.selectedFg = selectedFg;
        repaint();
    }

    public Color getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(Color dividerColor) {
        this.dividerColor = dividerColor;
        repaint();
    }

    public Border getItemBorder() {
        return itemBorder;
    }

    public void setItemBorder(Border itemBorder) {
        this.itemBorder = itemBorder;
        repaint();
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
        repaint();
    }
}
