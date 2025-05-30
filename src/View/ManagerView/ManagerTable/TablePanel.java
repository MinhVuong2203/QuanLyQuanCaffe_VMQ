package View.ManagerView.ManagerTable;

import Controller.ManagerController.TableManagerController;
import Model.Table;
import Repository.Table.ITableRespository;
import Repository.Table.TableRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    public List<Table> listTable;
    public JPanel panel_Center;

    public TablePanel() {
        ActionListener ac = new TableManagerController(this);
        setLayout(new BorderLayout(0, 0));

        // Center phần bảng
        panel_Center = new JPanel();
        add(panel_Center, BorderLayout.CENTER);
        panel_Center.setLayout(new GridLayout(1, 3, 10, 0));

        try {
            ITableRespository tableRepository = new TableRepository();
            listTable = tableRepository.getTableFromSQL();
            int total = listTable.size();
            String[] columnNames = {"Mã bàn", "Tên bàn", "Trạng thái"};
            int perTable = (int) Math.ceil(total / 3.0);

            for (int i = 0; i < 3; i++) {
                int start = i * perTable;
                int end = Math.min(start + perTable, total);
                Object[][] data = new Object[end - start][3];
                for (int j = start; j < end; j++) {
                    data[j - start][0] = listTable.get(j).getTableID();
                    data[j - start][1] = listTable.get(j).getTableName();
                    data[j - start][2] = listTable.get(j).getStatus();
                }

                table = new JTable();
                table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                table.setRowHeight(50);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table.setShowGrid(false); // Tắt grid lines
                table.setBorder(null); // Tắt border của JTable
                table.setIntercellSpacing(new Dimension(0, 0)); // Tắt khoảng cách giữa các ô

                // Renderer cho header
                table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        c.setBackground(new Color(255, 204, 204)); // Màu nền hồng nhạt
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.BOLD, 16));
                        setHorizontalAlignment(CENTER);
                        setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1));
                        return c;
                    }
                });

                // Renderer cho ô: căn giữa và màu xen kẽ
                table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setHorizontalAlignment(CENTER); // Căn giữa nội dung
                        if (!isSelected) {
                            if (row % 2 == 0) {
                                c.setBackground(new Color(240, 240, 240)); // Màu cho hàng chẵn
                            } else {
                                c.setBackground(Color.WHITE); // Màu cho hàng lẻ
                            }
                        } else {
                            c.setBackground(table.getSelectionBackground()); // Màu khi chọn hàng
                        }
                        return c;
                    }
                });

                table.setModel(new DefaultTableModel(data, columnNames) {
                    private static final long serialVersionUID = 1L;
                    boolean[] columnEditables = new boolean[] { false, false, false };

                    public boolean isCellEditable(int row, int column) {
                        return columnEditables[column];
                    }
                });

                table.getColumnModel().getColumn(0).setPreferredWidth(40);
                table.getColumnModel().getColumn(1).setPreferredWidth(100);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(null); // Tắt border của JScrollPane
                panel_Center.add(scrollPane);
            }

        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
        }

        // Dưới phần nút điều hướng
        JPanel panel_South = new JPanel();
        panel_South.setBackground(new Color(253, 183, 193));
        panel_South.setPreferredSize(new Dimension(100, 100));
        add(panel_South, BorderLayout.SOUTH);
        panel_South.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 25));

        JButton btnThem = new JButton("Thêm bàn");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThem.setPreferredSize(new Dimension(160, 50));
        btnThem.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\plus.png").getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH)));
        btnThem.setBorder(null);
        btnThem.setBackground(new Color(144, 238, 144));
        btnThem.addActionListener(ac);
        panel_South.add(btnThem);

        JButton btnSuaban = new JButton("Sửa bàn");
        btnSuaban.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSuaban.setPreferredSize(new Dimension(160, 50));
        btnSuaban.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\edit.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        btnSuaban.setBorder(null);
        btnSuaban.setBackground(new Color(255, 128, 128));
        btnSuaban.addActionListener(ac);
        panel_South.add(btnSuaban);

        JButton btnBaotri = new JButton("Bảo trì");
        btnBaotri.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBaotri.setPreferredSize(new Dimension(160, 50));
        btnBaotri.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\repair.png").getImage().getScaledInstance(55, 55, java.awt.Image.SCALE_SMOOTH)));
        btnBaotri.setBorder(null);
        btnBaotri.setBackground(new Color(255, 250, 205));
        btnBaotri.addActionListener(ac);
        panel_South.add(btnBaotri);
    }

    public void updateTableData(List<Table> listTable) {
        panel_Center.removeAll();

        try {
            String[] columnNames = {"Mã bàn", "Tên bàn", "Trạng thái"};
            int total = listTable.size();
            int perTable = (int) Math.ceil(total / 3.0);

            for (int i = 0; i < 3; i++) {
                int start = i * perTable;
                int end = Math.min(start + perTable, total);
                Object[][] data = new Object[end - start][3];
                for (int j = start; j < end; j++) {
                    data[j - start][0] = listTable.get(j).getTableID();
                    data[j - start][1] = listTable.get(j).getTableName();
                    data[j - start][2] = listTable.get(j).getStatus();
                }

                table = new JTable(new DefaultTableModel(data, columnNames) {
                    private static final long serialVersionUID = 1L;
                    boolean[] columnEditables = new boolean[] { false, false, false };

                    public boolean isCellEditable(int row, int column) {
                        return columnEditables[column];
                    }
                });

                table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                table.setRowHeight(50);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table.setShowGrid(false); // Tắt grid lines
                table.setBorder(null); // Tắt border của JTable
                table.setIntercellSpacing(new Dimension(0, 0)); // Tắt khoảng cách giữa các ô

                // Renderer cho header
                table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        c.setBackground(new Color(255, 204, 204));
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.BOLD, 16));
                        setHorizontalAlignment(CENTER);
                        setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1));
                        return c;
                    }
                });

                // Renderer cho ô: căn giữa và màu xen kẽ
                table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setHorizontalAlignment(CENTER); // Căn giữa nội dung
                        if (!isSelected) {
                            if (row % 2 == 0) {
                                c.setBackground(new Color(240, 240, 240)); // Màu cho hàng chẵn
                            } else {
                                c.setBackground(Color.WHITE); // Màu cho hàng lẻ
                            }
                        } else {
                            c.setBackground(table.getSelectionBackground()); // Màu khi chọn hàng
                        }
                        return c;
                    }
                });

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(null); // Tắt border của JScrollPane
                panel_Center.add(scrollPane);
            }

            panel_Center.revalidate();
            panel_Center.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}