package formValidation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FormValidationServlet extends HttpServlet {
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/form_data";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // Change to your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String birthDate = request.getParameter("birthDate");

        // Set content type
        response.setContentType("text/html");

        // Get the output writer
        PrintWriter out = response.getWriter();

        // Validation (Same as before)
        boolean valid = true;
        StringBuilder errorMessage = new StringBuilder();

        if (name == null || name.isEmpty()) {
            valid = false;
            errorMessage.append("Name is required.<br>");
        }

        if (email == null || email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            valid = false;
            errorMessage.append("Invalid email format.<br>");
        }

        if (password == null || password.isEmpty() || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.#])[A-Za-z\\d@$!%*?&.#]{8,}$")) {
            valid = false;
            errorMessage.append("Password must be at least 8 characters long, containing uppercase, lowercase, a number, and a special character.<br>");
        }

        if (!password.equals(confirmPassword)) {
            valid = false;
            errorMessage.append("Passwords do not match.<br>");
        }

        if (phone == null || phone.isEmpty() || !phone.matches("^(\\+?\\d{1,3}[-.\\s]?)?(\\(?\\d{2,3}\\)?[-.\\s]?)?[\\d]{3}[-.\\s]?\\d{3,4}$")) {
            valid = false;
            errorMessage.append("Invalid phone number format.<br>");
        }

        if (birthDate == null || birthDate.isEmpty() || !birthDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            valid = false;
            errorMessage.append("Date must be in the format YYYY-MM-DD.<br>");
        }

        // If validation fails, show errors
        if (!valid) {
            out.println("<h3>Form submission failed:</h3>");
            out.println("<p>" + errorMessage.toString() + "</p>");
            out.println("<a href='index.html'>Go back to the form</a>");
            return;
        }

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into the database
            String sqlInsert = "INSERT INTO users (name, email, password, phone, birth_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password); // In real-world, password should be encrypted
            pstmt.setString(4, phone);
            pstmt.setString(5, birthDate);
            pstmt.executeUpdate();

            // Fetch and display data (excluding password)
            String sqlFetch = "SELECT name, email, phone, birth_date FROM users WHERE email = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(sqlFetch);
            fetchStmt.setString(1, email);

            ResultSet rs = fetchStmt.executeQuery();

            // Display user details
            out.println("<h3>Form submitted successfully!</h3>");
            out.println("<h4>User Information:</h4>");
            if (rs.next()) {
                out.println("<p><strong>Name:</strong> " + rs.getString("name") + "</p>");
                out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
                out.println("<p><strong>Phone:</strong> " + rs.getString("phone") + "</p>");
                out.println("<p><strong>Birth Date:</strong> " + rs.getString("birth_date") + "</p>");
            }

            // Close resources
            rs.close();
            pstmt.close();
            fetchStmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<h3>Database error: " + e.getMessage() + "</h3>");
        }
    }
}
