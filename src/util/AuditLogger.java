
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogger {
    public static void log(String action, String details) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO AuditLog (UserID, Action, Details, LogTime) " +
                "VALUES (?, ?, ?, GETDATE())");
            ps.setInt(1,    SessionManager.getUserID());
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Audit log error: " + e.getMessage());
        }
    }
}