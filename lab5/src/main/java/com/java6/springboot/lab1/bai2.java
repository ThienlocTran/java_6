package com.java6.springboot.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.java6.springboot.lab1.entity.Student;
import com.java6.springboot.lab1.entity.StudentMap;
import com.java6.springboot.lab1.util.HttpClient; // Đảm bảo đã import class này từ Bài 1

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class bai2 extends JFrame {
    private JPanel mainPanel; // Thường IntelliJ bắt buộc có 1 panel chính để hiển thị
    private JTextField txtId;
    private JTextField txtFullname;
    private JTextField txtGender; // Bỏ qua biến này, ta dùng radio button
    private JRadioButton rdoMale;
    private JRadioButton rdoFemale;
    private JTextField txtMark;
    private JButton btnCreate;
    private JButton btnReset;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTable table1;

    private final String host = "https://java6-6f9c1-default-rtdb.asia-southeast1.firebasedatabase.app";
    private final ObjectMapper mapper = new ObjectMapper();

    // Dùng để lưu lại các Key (-Oxyz...) từ Firebase
    private List<String> listKeys = new ArrayList<>();
    private String currentKey = null;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public bai2() {
        // 1. Cấu hình bảng
        String[] columnNames = {"Id", "Full Name", "Gender", "Mark"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);

        // 2. Gắn sự kiện cho các nút bấm
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createStudent();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        // 3. Sự kiện CLICK CHUỘT VÀO BẢNG (Đã sửa lại thành MouseListener)
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row >= 0) {
                    currentKey = listKeys.get(row); // Lấy Key tương ứng với dòng được click

                    txtId.setText(table1.getValueAt(row, 0).toString());
                    txtFullname.setText(table1.getValueAt(row, 1).toString());

                    boolean isMale = table1.getValueAt(row, 2).toString().equals("Male");
                    rdoMale.setSelected(isMale);
                    rdoFemale.setSelected(!isMale);

                    txtMark.setText(table1.getValueAt(row, 3).toString());
                }
            }
        });

        // Tự động load dữ liệu lên bảng khi khởi động
        loadData();
    }

    // ================== CÁC HÀM XỬ LÝ LOGIC API ==================

    // HÀM 1: Đọc tất cả sinh viên từ Firebase đổ vào Bảng
    private void loadData() {
        try {
            var url = host + "/students.json";
            var connection = HttpClient.openConnection("GET", url);
            var response = HttpClient.readData(connection);

            // Xóa sạch dữ liệu cũ trên bảng
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            listKeys.clear();

            if (response != null && response.length > 4) { // Có dữ liệu thực tế
                StudentMap map = mapper.readValue(response, StudentMap.class);
                for (String key : map.keySet()) {
                    Student sv = map.get(key);
                    listKeys.add(key); // Cất key đi để xài cho nút Sửa/Xóa
                    model.addRow(new Object[]{
                            sv.getId(), sv.getName(), sv.isGender() ? "Male" : "Female", sv.getMark()
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi loadData: " + e.getMessage());
        }
    }

    // HÀM 2: Thêm mới (POST)
    private void createStudent() {
        try {
            // Lấy dữ liệu gõ trên form gói thành 1 object Student
            Student sv = new Student(txtId.getText(), txtFullname.getText(), Double.parseDouble(txtMark.getText()), rdoMale.isSelected());
            byte[] data = mapper.writeValueAsBytes(sv);

            var url = host + "/students.json";
            var connection = HttpClient.openConnection("POST", url);
            HttpClient.writeData(connection, data);

            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadData(); // Tải lại bảng để thấy dữ liệu mới
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm mới: " + e.getMessage());
        }
    }

    // HÀM 3: Cập nhật (PUT)
    private void updateStudent() {
        if (currentKey == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn 1 sinh viên trên bảng trước khi bấm Update!");
            return;
        }
        try {
            Student sv = new Student(txtId.getText(), txtFullname.getText(), Double.parseDouble(txtMark.getText()), rdoMale.isSelected());
            byte[] data = mapper.writeValueAsBytes(sv);

            var url = host + "/students/" + currentKey + ".json";
            var connection = HttpClient.openConnection("PUT", url);
            HttpClient.writeData(connection, data);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage());
        }
    }

    // HÀM 4: Xóa (DELETE)
    private void deleteStudent() {
        if (currentKey == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn 1 sinh viên trên bảng trước khi bấm Delete!");
            return;
        }
        try {
            var url = host + "/students/" + currentKey + ".json";
            var connection = HttpClient.openConnection("DELETE", url);
            HttpClient.readData(connection);

            JOptionPane.showMessageDialog(this, "Đã xóa sinh viên khỏi Firebase!");
            loadData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    // HÀM 5: Reset các ô nhập liệu
    private void resetForm() {
        txtId.setText("");
        txtFullname.setText("");
        txtMark.setText("");
        rdoMale.setSelected(true);
        currentKey = null; // Bỏ chọn key hiện tại
        txtId.requestFocus();
    }

    // (Tùy chọn) Hàm main để chạy test riêng Form này
    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản lý sinh viên");
        frame.setContentPane(new bai2().mainPanel); // Hiển thị panel chính của form
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("id");
        panel1.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtId = new JTextField();
        panel1.add(txtId, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("fullname");
        panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFullname = new JTextField();
        panel1.add(txtFullname, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("grender");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtGender = new JTextField();
        panel1.add(txtGender, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rdoMale = new JRadioButton();
        rdoMale.setText("male");
        panel2.add(rdoMale, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rdoFemale = new JRadioButton();
        rdoFemale.setText("female");
        panel2.add(rdoFemale, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("everger mark");
        panel1.add(label4, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtMark = new JTextField();
        panel1.add(txtMark, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCreate = new JButton();
        btnCreate.setText("CREATE");
        panel3.add(btnCreate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        btnReset = new JButton();
        btnReset.setText("RESET");
        panel3.add(btnReset, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUpdate = new JButton();
        btnUpdate.setText("UPDATE");
        panel3.add(btnUpdate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDelete = new JButton();
        btnDelete.setText("DELETE");
        panel3.add(btnDelete, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        table1.setAutoCreateRowSorter(false);
        table1.setIntercellSpacing(new Dimension(4, 1));
        scrollPane1.setViewportView(table1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
