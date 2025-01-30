package hospitalManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PatientManagement {
    private JPanel panel;
    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JTextField nameField, ageField, diseaseField, contactField;
    private JComboBox<String> genderBox;
    private JFormattedTextField admissionDateField;
    private JComboBox<Integer> doctorComboBox;  // ComboBox for doctor selection
    private Connection connection;

    public PatientManagement(Connection connection) {
        this.connection = connection;
        initializeUI();
        loadPatients();
        loadDoctors();  // Load doctors into ComboBox
    }

    private void initializeUI() {
        panel = new JPanel(new BorderLayout());

        // Table model and JTable for Patients
        patientTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Disease", "Contact", "Admission Date", "Doctor"}, 0);
        patientTable = new JTable(patientTableModel);
        JScrollPane tableScrollPane = new JScrollPane(patientTable);

        // Input panel for Patients
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5)); // Increased row count to 8
        inputPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        inputPanel.add(genderBox);

        inputPanel.add(new JLabel("Disease:"));
        diseaseField = new JTextField();
        inputPanel.add(diseaseField);

        inputPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        inputPanel.add(contactField);

        inputPanel.add(new JLabel("Admission Date:"));
        admissionDateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        admissionDateField.setColumns(10); // Set width for the field
        inputPanel.add(admissionDateField);

        inputPanel.add(new JLabel("Doctor:"));
        doctorComboBox = new JComboBox<>();
        inputPanel.add(doctorComboBox);

        // Buttons panel for Patients
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Add action listeners for Patients
        addButton.addActionListener(_ -> addPatient());
        deleteButton.addActionListener(_ -> deletePatient());

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void loadPatients() {
        try {
            patientTableModel.setRowCount(0);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT patients.id, patients.name, patients.age, patients.gender, patients.disease, patients.contact, patients.admission_date, doctors.name AS doctor_name FROM patients LEFT JOIN doctors ON patients.doctor_id = doctors.id");
            while (rs.next()) {
                patientTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("disease"),
                        rs.getString("contact"),
                        rs.getDate("admission_date"),  // Displaying the admission_date field
                        rs.getString("doctor_name")  // Displaying the doctor's name
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error loading patients: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDoctors() {
        try {
            doctorComboBox.removeAllItems();  // Clear previous items
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM doctors");
            while (rs.next()) {
                doctorComboBox.addItem(rs.getInt("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error loading doctors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPatient() {
        String name = nameField.getText();
        String age = ageField.getText();
        String gender = (String) genderBox.getSelectedItem();
        String disease = diseaseField.getText();
        String contact = contactField.getText();
        String admissionDate = admissionDateField.getText();
        int doctorId = (int) doctorComboBox.getSelectedItem();  // Get selected doctor ID

        if (name.isEmpty() || age.isEmpty() || disease.isEmpty() || contact.isEmpty() || admissionDate.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO patients (name, age, gender, disease, contact, admission_date, doctor_id) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(age));
            pstmt.setString(3, gender);
            pstmt.setString(4, disease);
            pstmt.setString(5, contact);
            pstmt.setDate(6, java.sql.Date.valueOf(admissionDate));
            pstmt.setInt(7, doctorId);  // Insert doctor_id
            pstmt.executeUpdate();
            loadPatients();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error adding patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Please select a patient to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) patientTableModel.getValueAt(selectedRow, 0);

        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM patients WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            loadPatients();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error deleting patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        diseaseField.setText("");
        contactField.setText("");
        admissionDateField.setText("");  // Clear the admission date field
        genderBox.setSelectedIndex(0);
        doctorComboBox.setSelectedIndex(0);  // Reset doctor selection
    }
}
