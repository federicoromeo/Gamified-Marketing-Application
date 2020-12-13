package controllers;

import javax.ejb.EJB;
import entities.MarketingQuestion;
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
import services.MarketingQuestionServiceBean;
import services.ProductServiceBean;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

@WebServlet("/InsertProduct")
@MultipartConfig
public class InsertProduct extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "services/ProductService")
    private ProductServiceBean productService;

    @EJB(name = "services/MarketingQuestionServiceBean")
    private MarketingQuestionServiceBean marketingQuestionServiceBean;

    public InsertProduct() {
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

    protected void errorAndRefresh(String error, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String path = "/WEB-INF/insertion.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("errormessage", error);
        templateEngine.process(path, ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String date, name;
        Part filePart;
        byte[] image;
        InputStream fileContent;
        int numberOfQuestions, newProductId, mqId;
        List<String> questions;
        Product newProduct;
        MarketingQuestion mqRef;
        List<MarketingQuestion> allQuestions = new ArrayList<>();;

        //get all parameters from the form

        //date of the questionnaire
        date = request.getParameter("date");

        //check if product is already present
        if(productService.findProductOfTheDay(date) != null)
        {
            errorAndRefresh("You already created a product for that day, please delete the old one first.", request, response);
            return;
        }

        //name of the product
        name = request.getParameter("name");

        //image of the product
        filePart = request.getPart("image");
        fileContent = filePart.getInputStream();
        image = IOUtils.toByteArray(fileContent);

        //check if too large for a BLOB

        /* todo : vaffanculo fra

        if(image.length > 60000)
        {
            errorAndRefresh("Please upload a smaller image", request, response);
            return;
        }*/

        //get the number of questions
        numberOfQuestions = Integer.parseInt(request.getParameter("numberofquestions"));
        questions = new ArrayList<>();
        for(int i = 1; i <= numberOfQuestions; i++)
            questions.add(request.getParameter("question" + i));

        try
        {
            newProductId = productService.createProduct(name, image, date);
            newProduct = productService.find(newProductId);

            for(String question : questions)
            {
                System.out.println(question);
                mqId = marketingQuestionServiceBean.createMarketingQuestion(question, newProduct);
                mqRef = marketingQuestionServiceBean.find(mqId);
                allQuestions.add(mqRef);
            }

            newProduct.setMarketingquestionsById(allQuestions);

            String path = "/WEB-INF/home_admin.html";
            ServletContext servletContext = getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("message", "Successful insertion!");
            this.templateEngine.process(path, ctx, response.getWriter());
        }
        catch(Exception e) //any unknown error in the creation of the product
        {
            errorAndRefresh("We encountered some problems in the creation of the questionnaire, please try again", request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

}
