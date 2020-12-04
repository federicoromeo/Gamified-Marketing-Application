package controllers;

import java.io.IOException;
import entities.User;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import exceptions.CredentialsException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.*;
import javax.persistence.NonUniqueResultException;
import javax.naming.*;


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="UserServiceEJB")  //prima era services/UserService
    private UserServiceBean userService;

    public CheckLogin()
    {
        super();
    }

    public void init() throws ServletException
    {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = null;
        String password = null;

        try
        {
            username = StringEscapeUtils.escapeJava(request.getParameter("username"));
            password = StringEscapeUtils.escapeJava(request.getParameter("password"));

            if (username == null || password == null || username.isEmpty() || password.isEmpty())
                throw new Exception("Missing or empty credential value");

        }
        catch (Exception e)
        {
            //DEBUG e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        User user = null;
        try
        {
            user = userService.checkCredentials(username, password);
        }
        catch (CredentialsException | NonUniqueResultException e)
        {
            //DEBUG e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }

        // If the user exists, add info to the session and go to home page,
        // otherwise show login page with error message

        String path;
        if (user == null)
        {
            path = "/index.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("errormessage", "Incorrect username or password!");
            templateEngine.process(path, ctx, response.getWriter());
        }
        else
        {
            QueryService qService = null;
            try
            {
                // Get the Initial Context for the JNDI lookup for a local EJB
                InitialContext ic = new InitialContext();
                // Retrieve the EJB using JNDI lookup
                qService = (QueryService) ic.lookup("java:/openejb/local/QueryServiceLocalBean");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("queryService", qService);

            if((int)user.getAdmin() != 0)
                path = getServletContext().getContextPath() + "/GoToHomeAdmin";
            else
                path = getServletContext().getContextPath() + "/GoToHomeUser";

            response.sendRedirect(path);
        }
    }

    public void destroy()
    {

    }

}