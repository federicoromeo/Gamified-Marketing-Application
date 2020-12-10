package controllers;

import entities.Points;
import entities.Product;
import entities.User;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));
        List<Points> pointsPerUser = new ArrayList<>();

        if(productId > 0){

            try
            {
                pointsPerUser = pointsServiceBean.findPointsByProduct(productId);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            pointsPerUser.sort(Collections.reverseOrder());

            Map<String,Integer> userAndPoints = new HashMap<>();

            for(Points p : pointsPerUser)
            {
                try
                {
                    User u = userServiceBean.find(p.getUserId());
                    if(u != null)
                        userAndPoints.put(p.getUserByUserId().getUsername(), p.getTotal());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            String path = "/WEB-INF/leaderboard.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("map", userAndPoints);
            this.templateEngine.process(path, ctx, response.getWriter());
        }
        else
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad product id");
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
