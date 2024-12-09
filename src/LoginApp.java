import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginApp extends JFrame {
private JTextField emailField;
private JPasswordField passwordField;
private static final String DB_URL = "jdbc:mysql://avnadmin:AVNS_BOwgsttXw1DG074BVy8@mysql-1e7edf9b-lhr-b3a4.e.aivencloud.com:25416/softwaretesting?ssl-mode=REQUIRED";

public LoginApp() {
setTitle("Login Screen");
setSize(350, 200);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);

JPanel panel = new JPanel();
panel.setLayout(new GridLayout(3, 2, 10, 10));

// Email Label and Text Field
panel.add(new JLabel("Email:"));
emailField = new JTextField();
panel.add(emailField);

// Password Label and Password Field
panel.add(new JLabel("Password:"));
passwordField = new JPasswordField();
panel.add(passwordField);

// Login Button
JButton loginButton = new JButton("Login");
loginButton.addActionListener(new LoginAction());
panel.add(loginButton);

add(panel);
}

private class LoginAction implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
String email = emailField.getText();
String password = new String(passwordField.getPassword()); // Password is ignored for validation

String userName = authenticateUser(email);
if (userName != null) {
JOptionPane.showMessageDialog(null, "Welcome, " + userName + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
} else {
JOptionPane.showMessageDialog(null, "User not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
}
}
}

private String authenticateUser(String email) {
String userName = null;
try (Connection conn = DriverManager.getConnection(DB_URL)) {
String query = "SELECT name FROM User WHERE Email = ?";
PreparedStatement stmt = conn.prepareStatement(query);
stmt.setString(1, email);
ResultSet rs = stmt.executeQuery();

if (rs.next()) {
userName = rs.getString("Name");
}
rs.close();
stmt.close();
} catch (Exception e) {
e.printStackTrace();
}
return userName;
}

public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
LoginApp loginApp = new LoginApp();
loginApp.setVisible(true);
});
}
}
