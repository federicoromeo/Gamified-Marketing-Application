package controllers;

import entities.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.MarketingAnswerServiceBean;
import services.MarketingQuestionServiceBean;
import services.ProductServiceBean;
import services.StatisticalAnswerServiceBean;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AnswerQuestionnaire")
public class AnswerQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TemplateEngine templateEngine;

    @EJB(name = "services/MarketingAnswerServiceBean")
    private MarketingAnswerServiceBean marketingAnswerServiceBean;

    @EJB(name = "services/MarketingQuestionServiceBean")
    private MarketingQuestionServiceBean marketingQuestionServiceBean;

    @EJB(name = "services/StatisticalAnswerServiceBean")
    private StatisticalAnswerServiceBean statisticalAnswerServiceBean;

    @EJB(name = "services/ProductServiceBean")
    private ProductServiceBean productServiceBean;


    public AnswerQuestionnaire() {
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

        // get statistical optional parameters from the form
        int age = 0;
        String sex = null;
        String expertiseLevel = null;
        age = Integer.parseInt(request.getParameter("age"));
        sex = request.getParameter("sex");
        expertiseLevel = request.getParameter("expertise-level");

        // get marketing mandatory parameters from the form
        int numberOfResponses = Integer.parseInt(request.getParameter("numberofresponses"));
        List<String> answers = new ArrayList<>();
        List<Integer> questionsId = new ArrayList<>();
        for(int i = 1; i <= numberOfResponses; i++)
        {
            answers.add(request.getParameter("response" + i));
            questionsId.add(Integer.parseInt(request.getParameter("question" + i)));
        }

        User user = (User) request.getSession().getAttribute("user");

        //MARKETING

        MarketingQuestion mq = null;
        int maId = 0;
        for(int i = 0; i < numberOfResponses; i++)
        {
            try{
                mq = marketingQuestionServiceBean.find(questionsId.get(i));
            }
            catch (Exception e)
            {
                //todo
            }
            try {
                maId = marketingAnswerServiceBean.createMarketingAnswer(answers.get(i),user,mq);
            }
            catch (Exception e)
            {
                //todo
            }
        }

        Product p = null;
        try
        {
            int productId = mq.getProductId();
            p = productServiceBean.find(productId);
        }
        catch (Exception e)
        {
            //todo
        }


        //STATISTICAL

        if(sex != null)
        {
            if(sex.isEmpty() || sex.equals("Not Chosen"))
            {
                sex = null;
            }
        }
        if(expertiseLevel != null)
        {
            if(expertiseLevel.isEmpty() || expertiseLevel.equals("Not Chosen"))
            {
                expertiseLevel = null;
            }
        }

        StatisticalAnswer sa = null;
        int saId = 0;

        try
        {
            saId = statisticalAnswerServiceBean.createStatisticalAnswer(user,p,age,sex,expertiseLevel);
            sa = statisticalAnswerServiceBean.find(saId);
        }
        catch (Exception e)
        {
            //todo
        }


        System.out.println("age: " + age);
        System.out.println("expertise: "+ expertiseLevel);
        System.out.println("sex: " + sex + "\n");
        for(int i = 0; i < numberOfResponses; i++)
        {
            System.out.println(questionsId.get(i));
            System.out.println(answers.get(i));
            System.out.println();

        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        String path = null;

        boolean committed = true; //fixme

        if(committed){
            path = "/WEB-INF/greetings.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            this.templateEngine.process(path, ctx, response.getWriter());
        }
        else
        {
            path = "/WEB-INF/home_user.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("product", p);
            this.templateEngine.process(path, ctx, response.getWriter());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
