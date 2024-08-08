package banking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class GeneratePDFServlet
 */
@WebServlet("/GeneratePDFServlet")
public class GeneratePDFServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneratePDFServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/bankingsystem";
            String user = "root";
            String passwordDB = "root";
            
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
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM transaction_history WHERE account_number = ?");
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            // Add content to the PDF document
            document.add(new Paragraph("Transaction History"));
            PdfPTable table = new PdfPTable(5);
            table.addCell("Transaction ID");
            table.addCell("Transaction Type");
            table.addCell("Amount");
            table.addCell("Balance");
            table.addCell("Transaction Date");
            
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("transaction_type"));
                table.addCell(String.valueOf(rs.getDouble("amount")));
                table.addCell(String.valueOf(rs.getDouble("balance")));
                table.addCell(String.valueOf(rs.getTimestamp("transaction_date")));
            }
            
            document.add(table);
            
            // Close resources
            rs.close();
            pstmt.close();
            lastRowRs.close();
            lastRowStmt.close();
            conn.close();
        } 
        catch (DocumentException | ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            document.close();
        }
    }
}
