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


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="UserServiceBean")
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
        User user = null;
        String path = null;

        //get username and password: mandatory parameters from the form
        try
        {
            username = StringEscapeUtils.escapeJava(request.getParameter("username"));
            password = StringEscapeUtils.escapeJava(request.getParameter("password"));

            if (username == null || password == null || username.isEmpty() || password.isEmpty())
                throw new Exception("Missing or empty credential value");

        }
        catch (Exception e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        //check credentials in the database
        try
        {
            user = userService.checkCredentials(username, password);
        }
        catch (CredentialsException | NonUniqueResultException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }

        // If the user exists and it is admin, add info to the session and go to home page admin,
        // otherwise show login page with error message

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
            request.getSession().setAttribute("user", user);

            if((int)user.getAdmin() != 0)
            {
                path = getServletContext().getContextPath() + "/GoToHomeAdmin";
                response.sendRedirect(path);
            }
            else
            {
                path = "/index.html";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("errormessage", "You tried to log as an USER account!");
                templateEngine.process(path, ctx, response.getWriter());
            }
        }
    }

    public void destroy()
    {

    }

}