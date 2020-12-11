package controllers;

import entities.MarketingAnswer;
import entities.MarketingQuestion;
import entities.Product;
import entities.User;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.MarketingAnswerServiceBean;
import services.MarketingQuestionServiceBean;
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
import java.util.Collection;
import java.util.List;


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


        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        try {
            product = StringEscapeUtils.escapeJava(request.getParameter("product"));

            if (product == null || product.isEmpty())
                throw new Exception("Missing informations about the product!");

            productId = Integer.parseInt(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
        }

        Product productOfTheDay = null;

        try {
            productOfTheDay = productService.find(productId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not got product");
        }


        //check if the user has already made the questionnaire of the day or he is blocked

        User user = (User) request.getSession().getAttribute("user");
        Boolean doQuestionnaire=(Boolean)ctx.getVariable("doQuestionnaire");

        if((doQuestionnaire!=null && !doQuestionnaire) || user.getBlocked()==1){

            request.getRequestDispatcher("/GoToHomeUser").forward(request, response);
        }
        else {


            String path = "/WEB-INF/questionnaire.html";
            ctx.setVariable("product", productOfTheDay);
            this.templateEngine.process(path, ctx, response.getWriter());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }



}
