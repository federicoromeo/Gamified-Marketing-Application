package examplePackageB;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Connection connection = null;

    public CheckLogin() {
        super();
        // Auto-generated constructor stub
    }

    public void init() throws ServletException {
        /*try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        /*UserDAO usr = new UserDAO(connection);
        User u = null;
        try {
            u = usr.checkCredentials(username, password);
        } catch (SQLException e) {
            //throw new ServletException(e);// for debugging
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database credential checking");
        }
        String path = getServletContext().getContextPath();
        if (u == null) {
            path = getServletContext().getContextPath() + "/index.html";
        } else {
            request.getSession().setAttribute("user", u);
            u.getIsAdmin() ? "/GoToHomeAdmin" : "/GoToHomeClient";
            path = path + target;
        }
        response.sendRedirect(path);*/
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
        }
    }
}