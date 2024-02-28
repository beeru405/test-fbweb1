import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MainServletTest {

    private MainServlet mainServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        mainServlet = new MainServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testValidRegistration() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking request parameters
        when(request.getParameter("Name")).thenReturn("John Doe");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("psw")).thenReturn("password");
        when(request.getParameter("bio")).thenReturn("This is a bio");
        when(request.getParameter("address")).thenReturn("123 Main St");

        // Mocking JDBC connection and statements
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(any(String.class), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("User registered successfully!"));
    }

    @Test
    public void testNameContainsOnlyLetters() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking invalid name with digits
        when(request.getParameter("Name")).thenReturn("John123");

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("Invalid name. Name should contain only letters."));
    }

    @Test
    public void testMobileNumberContainsOnlyDigits() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking invalid mobile number with characters
        when(request.getParameter("mobile")).thenReturn("1234abc567");

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("Invalid mobile number. Mobile number should contain only digits."));
    }

    @Test
    public void testMobileNumberContainsOnlyTenDigits() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking invalid mobile number with less than ten digits
        when(request.getParameter("mobile")).thenReturn("123456789");

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("Invalid mobile number. Mobile number should contain exactly ten digits."));
    }

    @Test
    public void testPasswordLength() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking invalid password with less than eight characters
        when(request.getParameter("psw")).thenReturn("pass");

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("Invalid password. Password should be at least eight characters long."));
    }

    @Test
    public void testPasswordAndRepeatPasswordMatch() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        // Mocking mismatched password and repeat password
        when(request.getParameter("psw")).thenReturn("password");
        when(request.getParameter("psw-repeat")).thenReturn("differentpassword");

        // Mocking response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Invoking doPost method
        mainServlet.doPost(request, response);

        // Verifying the response
        writer.flush();
        String result = stringWriter.toString().trim();
        assertTrue(result.contains("Passwords do not match. Please try again."));
    }
}
