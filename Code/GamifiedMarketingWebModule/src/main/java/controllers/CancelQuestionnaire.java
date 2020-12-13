package controllers;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.LogServiceBean;
import entities.*;
import org.thymeleaf.TemplateEngine;
import services.MarketingQuestionServiceBean;
import services.ProductServiceBean;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/CancelQuestionnaire")
public class CancelQuestionnaire extends HttpServlet
{
    @EJB(name = "services/LogServiceBean")
    private LogServiceBean logServiceBean;

    @EJB(name = "services/MarketingQuestionServiceBean")
    private MarketingQuestionServiceBean marketingQuestionServiceBean;

    @EJB(name = "services/ProductServiceBean")
    private ProductServiceBean productServiceBean;

    private TemplateEngine templateEngine;

    public CancelQuestionnaire()
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
        int pId;
        String path;
        User user;
        MarketingQuestion mq;
        Product product;


        //get parameters from the context, only the ones needed to log

        //user
        user = (User) request.getSession().getAttribute("user");

        //the first question

        pId = Integer.parseInt(request.getParameter("product"));
        System.out.println("product: "+pId);

        //fetch the product
        product = productServiceBean.find(pId);

        //log the cancel
        try
        {
            logServiceBean.createLog(user,product,(byte)0,new Timestamp(System.currentTimeMillis()));
            path = getServletContext().getContextPath() + "/GoToHomeUser";
            response.sendRedirect(path);
        }
        catch(Exception e)
        {
            System.out.println("Error in logging the response");
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.doGet(request, response);
    }
}
