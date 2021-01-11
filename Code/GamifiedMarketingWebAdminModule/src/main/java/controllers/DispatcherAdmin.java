package controllers;

import javax.ejb.EJB;
import entities.Product;
import org.thymeleaf.context.WebContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.ProductServiceBean;
import utils.Counter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

//TODO clean

@WebServlet("/DispatcherAdmin")
@MultipartConfig
public class DispatcherAdmin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "services/ProductService")
    private ProductServiceBean productService;

    public DispatcherAdmin() {
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

        String choice = null;
        String path = null;
        List<Product> pastProducts = null;

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        if ( !request.getParameter("button").equals("") &&
             !request.getParameter("button").isEmpty() &&
              request.getParameter("button") != null  )
        {
            choice = request.getParameter("button");
        }

        if (choice != null) {

            switch(choice) {

                case "insert-button":
                    System.out.println( LocalDate.now());
                    ctx.setVariable("now", LocalDate.now());
                    path = "/WEB-INF/insertion.html";
                    break;

                case "inspect-button":
                    pastProducts = productService.findPastProducts();
                    //pastProducts = productService.findAll();
                    ctx.setVariable("pastProducts", pastProducts);
                    ctx.setVariable("counter", new Counter());
                    path = "/WEB-INF/inspection.html";
                    break;

                case "delete-button":
                    pastProducts = productService.findPastProducts();
                    //pastProducts = productService.findAll();
                    ctx.setVariable("pastProducts", pastProducts);
                    path = "/WEB-INF/deletion.html";
                    break;

                default:
                    path = "/WEB-INF/home_admin.html";
                    break;
            }

            templateEngine.process(path, ctx, response.getWriter());
        }
        else{
            path = "/WEB-INF/home_admin.html";
            templateEngine.process(path, ctx, response.getWriter());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }


}
