package controllers;

import entities.Points;
import entities.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.PointsServiceBean;
import services.ProductServiceBean;
import services.UserServiceBean;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@WebServlet("/GoToLeaderboard")
public class GoToLeaderboard extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name = "services/ProductServiceBean")
    private ProductServiceBean productServiceBean;

    @EJB(name = "services/PointsServiceBean")
    private PointsServiceBean pointsServiceBean;

    @EJB(name = "services/UserServiceBean")
    private UserServiceBean userServiceBean;

    public GoToLeaderboard() {
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

        Product productOfTheDay = null;
        List<Points> pointsPerUser = null;

        try
        {
            //productServiceBean.updateProduct(productId);
            productOfTheDay = productServiceBean.findProductOfTheDay(new Date());
            pointsPerUser = productServiceBean.findLeaderboardByProductId(productOfTheDay.getId());

            /*
            for(Points p : pointsPerUser)
                System.out.println(p.getUserByUserId().getUsername()+" "+ p.getTotal());

             */

            //System.out.println("\n"+pointsPerUser);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String path = "/WEB-INF/leaderboard.html";
        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("pointsList", pointsPerUser);
        this.templateEngine.process(path, ctx, response.getWriter());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request,response);
    }

}
