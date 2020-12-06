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
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
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

    public void init() throws ServletException {

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // get all parameters from the form
        String date = request.getParameter("date");

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //LocalDate date = LocalDate.parse(date, formatter);
        //System.out.println("2 :" + date);

        String name = request.getParameter("name");

        Part filePart = request.getPart("image");
        //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        byte[] image = IOUtils.toByteArray(fileContent);

        int numberOfQuestions = Integer.parseInt(request.getParameter("numberofquestions"));

        List<String> questions = new ArrayList<>();
        for(int i = 1; i <= numberOfQuestions; i++)
            questions.add(request.getParameter("question" + i));

        //create new product from inserted datas
        int newProductId = -1;
        try
        {
            newProductId = productService.createProduct(name, image, date);
            if(newProductId == -1){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create product");
                String path = "/WEB-INF/insertion.html";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("errormessage", "Invalid date for the product! Please change it.");
                templateEngine.process(path, ctx, response.getWriter());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(); //todo
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create product");
            String path = "/WEB-INF/insertion.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("errormessage", "Invalid date for the product! Please change it.");
            templateEngine.process(path, ctx, response.getWriter());

            return;
        }

        // get the product just inserted in the db
        Product newProduct = null;
        try
        {
            newProduct = productService.find(newProductId);
        }
        catch(Exception e)
        {
            e.printStackTrace();  //todo
        }

        // insert all the questions separately
        List<MarketingQuestion> allQuestions = new ArrayList<>();
        if(newProduct != null)
        {
            for(String question : questions)
            {
                // create marketing question and get its autogen id
                int mqId = -1;
                try
                {
                    mqId = marketingQuestionServiceBean.createMarketingQuestion(question, newProduct);
                }
                catch(Exception e)
                {
                    e.printStackTrace(); //todo
                }

                //get from db the just created marketing question
                MarketingQuestion mq = null;
                try
                {
                    mq = marketingQuestionServiceBean.find(mqId);
                    if(mq != null)
                    {
                        allQuestions.add(mq);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace(); //todo
                }
            }
        }

        //absolutely unuseful
        newProduct.setMarketingquestionsById(allQuestions);



        ServletContext servletContext = getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        String path = "/WEB-INF/home_admin.html";
        this.templateEngine.process(path, ctx, response.getWriter());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

}
