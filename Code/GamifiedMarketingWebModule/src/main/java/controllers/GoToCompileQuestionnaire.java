package controllers;

import entities.Product;
import entities.User;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.ProductServiceBean;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/GoToCompileQuestionnaire")
public class GoToCompileQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="ProductServiceEJB")
    private ProductServiceBean productService;

    public GoToCompileQuestionnaire() {
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

        String product = null;
        int productId = 0;
        User user = null;

        try
        {
            product = StringEscapeUtils.escapeJava(request.getParameter("product"));

            if (product == null || product.isEmpty())
                throw new Exception("Missing informations about the product!");

            productId = Integer.parseInt(product);

        } catch (Exception e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
        }

        Product productOfTheDay = null;

        try
        {
            productOfTheDay = productService.find(productId);
        } catch (Exception e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not got product");
        }

        user = (User) request.getSession().getAttribute("user");

        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        //check if the user already made the questionnaire of the day or he is blocked
        Boolean doQuestionnaire = (Boolean)ctx.getVariable("doQuestionnaire");

        if((doQuestionnaire!=null && !doQuestionnaire) || user.getBlocked()==1)
            request.getRequestDispatcher("/GoToHomeUser").forward(request, response);

        else
        {
            String path = "/WEB-INF/questionnaire.html";
            ctx.setVariable("product", productOfTheDay);
            this.templateEngine.process(path, ctx, response.getWriter());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }



}
