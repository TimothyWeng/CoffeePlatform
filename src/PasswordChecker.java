import java.sql.*;

public class PasswordChecker {
	Connection con;
	public PasswordChecker() {
        con = null;
        try {
        	// type the path of coffee customer's data
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("Password checker open sql failed");
            System.out.println(e.getMessage());
        }
		
	}
	
	public boolean check(String user, String hashpassword) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT 帳號,密碼 FROM users where 帳號 = ?";
        try {
        	ps = con.prepareStatement(sql);
        	ps.setString(1, user);
        	rs = ps.executeQuery();
        	String userPassword = rs.getString(2);
            System.out.printf("user: %s\n", userPassword);
            System.out.printf("realuser: %s\n", hashpassword);
        	return hashpassword.equals(userPassword);
        }
        catch (Exception e) {
            System.out.println("Password checker query failed");
            System.out.println(e.getMessage());
        }
        return false;
	}
}