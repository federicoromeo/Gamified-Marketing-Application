package controllers;

import entities.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.*;
import java.sql.Timestamp;
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

    @EJB(name = "services/OffensiveWordServiceBean")
    private OffensiveWordServiceBean offensiveWordServiceBean;

    @EJB(name = "services/UserServiceBean")
    private UserServiceBean userServiceBean;

    @EJB(name = "services/LogServiceBean")
    private LogServiceBean logServiceBean;

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


        int age, numberOfResponses, saId, mqId, maId;
        String sex, expertiseLevel, path, answer;
        List<String> answers = new ArrayList<>();
        List<Integer> questionsId = new ArrayList<>();
        List<OffensiveWord> offensiveWords = null;
        User user = null;
        MarketingQuestion mq = null;
        StatisticalAnswer sa = null;
        Product product = null;

        //get statistical optional parameters from the form

        //age
        age = Integer.parseInt(request.getParameter("age"));

        //sex
        sex = request.getParameter("sex");
        if(sex != null && sex.equals("Not Chosen"))
            sex = null;

        //expertise
        expertiseLevel = request.getParameter("expertise-level");
        if(expertiseLevel != null && expertiseLevel.equals("Not Chosen"))
            expertiseLevel = null;

        //get marketing mandatory parameters from the form

        numberOfResponses = Integer.parseInt(request.getParameter("numberofresponses"));

        //get the active user
        user = (User) request.getSession().getAttribute("user");

        //retrieving all the offensive words from db
        offensiveWords=offensiveWordServiceBean.findAll();

        for(int i = 1; i <= numberOfResponses; i++)
        {
            answer = request.getParameter("response" + i);
            answers.add(answer);
            questionsId.add(Integer.parseInt(request.getParameter("question" + i)));

            //the user is blocked and transaction rolled back
            if (checkPresence(answer, offensiveWords))
            {
                userServiceBean.blockUser(user);
            }
        }

        //TODO: dobbiamo loggare anche chi viene bloccato?
        //TODO: se uno preme cancel non deve essere bloccato, così come è invece succede
        user = userServiceBean.find(user.getId());
        if(user.getBlocked()==1)
        {
            path = "/WEB-INF/blocked.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            this.templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        //MARKETING

        //for each marketing question/answer, fetch the question and we create the answers
        try
        {
            for(int i = 0; i < numberOfResponses; i++)
            {
                mq = marketingQuestionServiceBean.find(questionsId.get(i));
                maId = marketingAnswerServiceBean.createMarketingAnswer(answers.get(i),user,mq);
            }
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to register the marketing answers");
        }

        try
        {
            mq  = marketingQuestionServiceBean.find(questionsId.get(0));
            product = productServiceBean.find(mq.getProductId());
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to fetch the product of the day" + e.getMessage());
        }

        //create the statistical answer
        try
        {
            saId = statisticalAnswerServiceBean.createStatisticalAnswer(user,product,age,sex,expertiseLevel);
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to register the statistical answers");
        }

        //commit or cancel the questionnaire
        try
        {
            boolean committed = true;
            if(committed)
            {
                logServiceBean.createLog(user,product,(byte)1,new Timestamp(System.currentTimeMillis()));
                path = "/WEB-INF/greetings.html";
                ServletContext servletContext = this.getServletContext();
                WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                this.templateEngine.process(path, ctx, response.getWriter());
            }
            else
            {
                logServiceBean.createLog(user,product,(byte)0,new Timestamp(System.currentTimeMillis()));
                path = "/WEB-INF/home_user.html";
                ServletContext servletContext = this.getServletContext();
                WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("product", product);
                this.templateEngine.process(path, ctx, response.getWriter());
            }
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to log the response");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /*
     * return true if the string container contains at least one of the words in the list
     * */
    public static Boolean checkPresence(String container, List<OffensiveWord> words){

        for(OffensiveWord word:words){

            if(container.contains(word.getWord())) return true;
        }

        return false;
    }

}
