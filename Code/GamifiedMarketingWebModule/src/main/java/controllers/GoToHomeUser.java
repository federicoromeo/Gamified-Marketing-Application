package controllers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entities.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.*;
import java.time.LocalDate;
import java.util.Base64;

@WebServlet("/GoToHomeUser")
public class GoToHomeUser extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="ProductServiceEJB")
    private ProductServiceBean productService;

    public GoToHomeUser()
    {
        super();
    }

    public void init() throws ServletException
    {
        ServletContext servletContext = this.getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Product productOfTheDay = null;
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try
        {
            productOfTheDay = this.productService.findProductOfTheDay(today);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if (productOfTheDay == null)
            productOfTheDay = this.productService.findDefaultNullImage();

        String image = Base64.getEncoder().encodeToString(productOfTheDay.getImage());

        String path = "/WEB-INF/home_user.html";
        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("product", productOfTheDay);
        ctx.setVariable("image", image);
        this.templateEngine.process(path, ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.doGet(request, response);
    }

}
