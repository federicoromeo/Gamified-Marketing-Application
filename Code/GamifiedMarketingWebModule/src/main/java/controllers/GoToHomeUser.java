package controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.MarketingAnswer;
import entities.MarketingQuestion;
import entities.User;
import utils.Counter;
import entities.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Date;

//TODO clean

@WebServlet("/GoToHomeUser")
public class GoToHomeUser extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name="ProductServiceEJB")
    private ProductServiceBean productService;

    @EJB(name="MarketingAnswerServiceEJB")
    private MarketingAnswerServiceBean marketingAnswerService;

    @EJB(name="MarketingQuestionServiceEJB")
    private MarketingQuestionServiceBean marketingQuestionService;

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
        boolean doQuestionnaire=true;

        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        try
        {
            productOfTheDay = this.productService.findProductOfTheDay(new Date());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if (productOfTheDay == null)
            productOfTheDay = this.productService.findDefaultNullImage();

        String image = Base64.getEncoder().encodeToString(productOfTheDay.getImage());

        //List<Integer> iteration = new ArrayList<>();

        User user = (User) request.getSession().getAttribute("user");

        if(questionnaireAlreadyMade(user, productOfTheDay) || user.getBlocked()==1)
            doQuestionnaire=false;

        List<MarketingQuestion> questions = marketingQuestionService.findMarketingQuestionsByProduct(productOfTheDay);

        String path = "/WEB-INF/home_user.html";
        ctx.setVariable("doQuestionnaire", doQuestionnaire);
        ctx.setVariable("product", productOfTheDay);
        ctx.setVariable("questions", questions);
        ctx.setVariable("image", image);
        ctx.setVariable("counter", new Counter());
        //ctx.setVariable("iteration", iteration);
        this.templateEngine.process(path, ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.doGet(request, response);
    }


    public Boolean questionnaireAlreadyMade(User user, Product product){

        if(product==null || user==null)
            return false;

        List<MarketingQuestion> questions = marketingQuestionService.findMarketingQuestionsByProduct(product);
        //List<MarketingQuestion> questions = (List)product.getMarketingquestionsById();
        /*
        System.out.println("\nSTART");


        for(MarketingQuestion mq: questions) {
            System.out.println(mq.getText());
            for (MarketingAnswer ma: mq.getMarketinganswersById()){
                System.out.println(ma.getText());
            }
        }
        System.out.println("END\n");

         */

        for(MarketingQuestion mq: questions){
            List<MarketingAnswer> answers=marketingAnswerService.findMarketingAnswersByUserMarketingQuestion(user, mq);

            if(answers!=null && !answers.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
