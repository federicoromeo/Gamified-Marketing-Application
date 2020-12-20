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


        int numberOfResponses, saId, mqId, maId, pId;
        String expertiseLevel, path, answer;
        List<String> answers = new ArrayList<>();
        List<Integer> questionsId = new ArrayList<>();
        List<OffensiveWord> offensiveWords = null;
        User user = null;
        MarketingQuestion mq = null;
        StatisticalAnswer sa = null;
        Product product = null;
        Object sex, age;

        //get statistical optional parameters from the form

        //age
        age = request.getParameter("checkboxAge");

        //sex
        sex = request.getParameter("checkboxSex");

        //expertise
        expertiseLevel = request.getParameter("expertise-level");
        if(expertiseLevel != null && expertiseLevel.equals("Not Chosen"))
            expertiseLevel = null;

        //get marketing mandatory parameters from the form

        numberOfResponses = Integer.parseInt(request.getParameter("numberofresponses"));
        System.out.println(numberOfResponses);

        //get the active user
        user = (User) request.getSession().getAttribute("user");

        //get the product
        pId = Integer.parseInt(request.getParameter("product"));
        product = productServiceBean.find(pId);

        //check if this is a duplicate submission
        if(logServiceBean.isLogPresent(user.getId(), product.getId(), true))
        {
            path = "/WEB-INF/duplicate.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            this.templateEngine.process(path, ctx, response.getWriter());
            return;
        }

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
                //qua dovrebbe scattare trigger
            }
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to register the marketing answers");
        }

        //create the statistical answer
        try
        {
            //TODO da cambiare
            saId = statisticalAnswerServiceBean.createStatisticalAnswer(user,product,(byte) (age!=null? 1 : 0),(byte) (sex!=null? 1 : 0),expertiseLevel);
            //qua dovrebbe scattare trigger
        }
        catch(Exception e)
        {
            System.err.println("It was not possible to register the statistical answers");
        }

        //commit the questionnaire
        try
        {
            logServiceBean.createLog(user,product,(byte)1,new Timestamp(System.currentTimeMillis()));
            path = "/WEB-INF/greetings.html";
            ServletContext servletContext = this.getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            this.templateEngine.process(path, ctx, response.getWriter());
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
