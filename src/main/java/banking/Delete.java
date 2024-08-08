package banking;

	import java.io.IOException;
	import java.io.PrintWriter;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

	@WebServlet("/Delete")
	public class Delete extends HttpServlet {
	    private static final long serialVersionUID = 1L;

	    private static final String DELETE_QUERY = "DELETE FROM admin_dashboard WHERE full_name = ? AND mobile_no = ?";
	    
	    public Delete() {
	        super();
	    }

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        PrintWriter pw = response.getWriter();
	        String fullName = request.getParameter("fullName");
	        String mobileNo = request.getParameter("mobileNo");

	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }

	        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingsystem", "root", "root");
	             PreparedStatement psDelete = con.prepareStatement(DELETE_QUERY)) {

	            // Delete record from admin_dashboard table
	            psDelete.setString(1, fullName);
	            psDelete.setString(2, mobileNo);
	            int count = psDelete.executeUpdate();

	            if (count > 0) {
	                pw.println("Customer deleted successfully.");
	                // Redirect to Admin.html after 5 seconds
	                response.setHeader("Refresh", "3; URL=Admin.html");
	            } else {
	                pw.println("Failed to delete customer.");
	            }
	        } catch (SQLException se) {
	            pw.println(se.getMessage());
	            se.printStackTrace();
	        } catch (Exception e) {
	            pw.println(e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}

