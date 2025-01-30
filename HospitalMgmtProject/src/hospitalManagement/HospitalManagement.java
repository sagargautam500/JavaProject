package hospitalManagement;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HospitalManagement {
    private JFrame frame;
    private PatientManagement patientManagement;
    private DoctorManagement doctorManagement;
    private Connection connection;

    public HospitalManagement() {
        // Initialize the database connection
        connectToDatabase();

        // Initialize the frame
        frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Tabbed Pane for Patient and Doctor Management
        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize and add Patient Management Panel
        patientManagement = new PatientManagement(connection);
        tabbedPane.addTab("Patient Management", patientManagement.getPanel());

        // Initialize and add Doctor Management Panel
        doctorManagement = new DoctorManagement(connection);
        tabbedPane.addTab("Doctor Management", doctorManagement.getPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            // Update the URL, username, and password as per your database setup
            String url = "jdbc:mysql://localhost:3306/hospital_db";
            String user = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database!\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new HospitalManagement();
    }
}
