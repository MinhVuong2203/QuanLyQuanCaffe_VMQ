package View.ManagerView.ManagerShift;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class test extends JFrame {
    private JTabbedPane tabbedPane;
    private int tabCount = 1;

    public test() {
        setTitle("Browser-like Tabs");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Nút thêm tab
        JButton newTabButton = new JButton("New Tab");
        newTabButton.addActionListener(e -> addNewTab());
        add(newTabButton, BorderLayout.NORTH);

        // Thêm tab đầu tiên
        addNewTab();
    }

    private void addNewTab() {
        String title = "Tab " + tabCount++;
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nội dung của " + title));

        // Tạo component tab có nút X
        JPanel tabHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabHeader.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 5, 0, 5));
        closeButton.setBorder(null);
        closeButton.setFocusable(false);

        // Đóng tab khi nhấn nút x
        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(tabHeader);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });

        tabHeader.add(titleLabel);
        tabHeader.add(closeButton);

        tabbedPane.addTab(title, panel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabHeader);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new test().setVisible(true));
    }
}
