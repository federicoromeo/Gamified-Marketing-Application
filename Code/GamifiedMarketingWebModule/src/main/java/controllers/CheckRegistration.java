package controllers;

import java.io.IOException;
import java.util.Date;

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
import utils.Data;

import javax.persistence.NonUniqueResultException;
import javax.naming.*;

//TODO clean

@WebServlet("/CheckRegistration")
public class CheckRegistration extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="UserServiceEJB")  //prima era services/UserService
    private UserServiceBean userService;

    public CheckRegistration()
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

        String username, password, email, sex = null;
        Date date = null;

        try
        {
            username = StringEscapeUtils.escapeJava(request.getParameter("username"));
            password = StringEscapeUtils.escapeJava(request.getParameter("password"));
            email = StringEscapeUtils.escapeJava(request.getParameter("email"));
            date = Data.stringToDate(request.getParameter("date"));
            sex = request.getParameter("sex");

            if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty() || email.isEmpty() ||
                sex == null || sex.isEmpty() || date == null)
                throw new Exception("Missing or empty credential value");

            Date today = new Date();
            if(date.after(today))
                throw new Exception("Impossible birthdate!");

            if(!sex.equals("MALE") && !sex.equals("FEMALE") && !sex.equals("OTHER"))
                throw new Exception("Impossible Gender!");

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
            // Check if the username is valid
            user = userService.findByUsername(username);
        }
        catch (NonUniqueResultException e)
        {
            //DEBUG e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // If the user exists, add info to the session and go to home page,
        // otherwise show login page with error message

        String path;
        User newUser = null;
        int idUser;

        // Username already taken
        if (user != null)
        {
            path = "/index.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("errormessage", "Username already taken, register with a different one!");
            templateEngine.process(path, ctx, response.getWriter());
        }
        //the username is not taken   --> OK!
        else
        {
            //QueryService qService = null;
            try
            {
                newUser = userService.createUser(username, password, email, sex, date);

                if(newUser.getId() == 0) {
                    System.out.println("something went wrong");
                    path = "/index.html";
                    ServletContext servletContext = getServletContext();
                    final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                    ctx.setVariable("errormessage", "Something went wrong in the id assigning!");
                    templateEngine.process(path, ctx, response.getWriter());
                }
                else
                    {
                    System.out.println("created user " + newUser.getUsername() + " with id: " + newUser.getId());
                    request.getSession().setAttribute("user", newUser);
                    //request.getSession().setAttribute("queryService", qService);
                    path = getServletContext().getContextPath() + "/GoToHomeUser";
                    response.sendRedirect(path);
                }

                /* Get the Initial Context for the JNDI lookup for a local EJB
                InitialContext ic = new InitialContext();
                // Retrieve the EJB using JNDI lookup
                qService = (QueryService) ic.lookup("java:/openejb/local/QueryServiceLocalBean");
                */
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public void destroy()
    {
    }

}