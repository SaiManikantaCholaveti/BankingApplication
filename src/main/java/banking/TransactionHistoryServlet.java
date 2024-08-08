package banking;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/TransactionHistoryServlet")
public class TransactionHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/bankingsystem";
        String user = "root";
        String passwordDB = "root";

        int limit = 10; // default limit
        String limitParam = request.getParameter("limit");
        if (limitParam != null) {
            try {
                limit = Integer.parseInt(limitParam);
            } catch (NumberFormatException e) {
                // Use default limit if the parameter is not a valid number
            }
        }

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, passwordDB);

            // Retrieve the last row from the customer_login table
            PreparedStatement lastRowStmt = conn.prepareStatement("SELECT * FROM customer_login ORDER BY id DESC LIMIT 1");
            ResultSet lastRowRs = lastRowStmt.executeQuery();
            String accountNumber = "";

            if (lastRowRs.next()) {
                accountNumber = lastRowRs.getString("account");
            }

            // Retrieve transaction history for the last account
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM transaction_history WHERE account_number = ? ORDER BY id DESC LIMIT ?");
            pstmt.setString(1, accountNumber);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            // Write HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Transaction History</title>");
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Transaction History</h2>");

            // Dropdown form to select number of transactions to display
            out.println("<form action=\"TransactionHistoryServlet\" method=\"get\">");
            out.println("<label for=\"limit\">Show last: </label>");
            out.println("<select name=\"limit\" id=\"limit\" onchange=\"this.form.submit()\">");
            out.println("<option value=\"10\"" + (limit == 10 ? " selected" : "") + ">10</option>");
            out.println("<option value=\"20\"" + (limit == 20 ? " selected" : "") + ">20</option>");
            out.println("<option value=\"50\"" + (limit == 50 ? " selected" : "") + ">50</option>");
            out.println("</select>");
            out.println(" transactions");
            out.println("</form>");

            out.println("<table>");
            out.println("<tr><th>Transaction ID</th><th>Transaction Type</th><th>Amount</th><th>Balance</th><th>Transaction Date</th></tr>");

            // Populate table rows with transaction data
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("transaction_type") + "</td>");
                out.println("<td>" + rs.getDouble("amount") + "</td>");
                out.println("<td>" + rs.getDouble("balance") + "</td>");
                out.println("<td>" + rs.getTimestamp("transaction_date") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            // Add the form to generate the PDF
            out.println("<form action=\"GeneratePDFServlet\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"data\" value=\"transactions\">");
            out.println("<button type=\"submit\" class=\"dialog-button\">Print as PDF</button>");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");

            // Close resources
            rs.close();
            pstmt.close();
            lastRowRs.close();
            lastRowStmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("Exception occurred: " + e.getMessage());
        }
    }
}
