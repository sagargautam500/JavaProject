package hospitalManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DoctorManagement {
    private JPanel panel;
    private JTable doctorTable;
    private DefaultTableModel doctorTableModel;
    private JTextField nameField, specializationField, contactField;
    private Connection connection;

    public DoctorManagement(Connection connection) {
        this.connection = connection;
        initializeUI();
        loadDoctors();
    }

    private void initializeUI() {
        panel = new JPanel(new BorderLayout());

        // Table model and JTable for Doctors
        doctorTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization", "Contact"}, 0);
        doctorTable = new JTable(doctorTableModel);
        JScrollPane tableScrollPane = new JScrollPane(doctorTable);

        // Input panel for Doctors
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Doctor Details"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Specialization:"));
        specializationField = new JTextField();
        inputPanel.add(specializationField);

        inputPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        inputPanel.add(contactField);

        // Buttons panel for Doctors
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Add action listeners for Doctors
        addButton.addActionListener(_ -> addDoctor());
        deleteButton.addActionListener(_ -> deleteDoctor());

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void loadDoctors() {
        try {
            doctorTableModel.setRowCount(0);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                doctorTableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("specialization"), rs.getString("contact")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error loading doctors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDoctor() {
        String name = nameField.getText();
        String specialization = specializationField.getText();
        String contact = contactField.getText();

        if (name.isEmpty() || specialization.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO doctors (name, specialization, contact) VALUES (?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, specialization);
            pstmt.setString(3, contact);
            pstmt.executeUpdate();
            loadDoctors();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error adding doctor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Please select a doctor to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) doctorTableModel.getValueAt(selectedRow, 0);

        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM doctors WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            loadDoctors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error deleting doctor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        specializationField.setText("");
        contactField.setText("");
    }
}
