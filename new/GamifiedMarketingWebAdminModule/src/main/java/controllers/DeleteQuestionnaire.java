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
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.ProductServiceBean;


import java.io.IOException;
import java.util.List;



@WebServlet("/DeleteQuestionnaire")
@MultipartConfig
public class DeleteQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "services/ProductService")
    private ProductServiceBean productService;

    public DeleteQuestionnaire() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer productId=-1;
        String path=null;

        ServletContext servletContext = getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        if (request.getParameter("product") != "" && !request.getParameter("product").isEmpty()) {
            productId= Integer.parseInt(request.getParameter("product"));
        }

        if (productId!=-1) {

            productService.deleteProduct(productId);
        }

        List<Product> pastProducts= pastProducts=productService.findAll();

        ctx.setVariable("pastProducts", pastProducts);

        path = "/WEB-INF/deletion.html";
        this.templateEngine.process(path, ctx, response.getWriter());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }


}
