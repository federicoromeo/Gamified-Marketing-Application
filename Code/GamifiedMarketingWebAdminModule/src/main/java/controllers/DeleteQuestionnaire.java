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
import java.io.IOException;
import java.util.List;


@WebServlet("/DeleteQuestionnaire")
@MultipartConfig
public class DeleteQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "services/ProductService")
    private ProductServiceBean productService;

    public DeleteQuestionnaire()
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
        Integer productId = -1;
        List<Product> pastProducts = null;
        String path = null;

        if (!request.getParameter("product").equals("") &&
            !request.getParameter("product").isEmpty() &&
            request.getParameter("product") != null )
        {
            productId = Integer.parseInt(request.getParameter("product"));
        }

        if (productId!=-1 && productId!=2)      // 2 is default, undeletable
        {
            try
            {
                productService.deleteProduct(productId);

            }
            catch(Exception e)
            {
                System.out.println(e.getClass());
            }
        }

        pastProducts = productService.findPastProducts();

        ServletContext servletContext = getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("pastProducts", pastProducts);
        path = "/WEB-INF/deletion.html";
        this.templateEngine.process(path, ctx, response.getWriter());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

}
