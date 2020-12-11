package controllers;

import entities.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.*;

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


        String path = null;
        ServletContext servletContext = this.getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

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
        String tmpAnswer=null;

        //retrieving all the offensive words from db
        List<OffensiveWord> offensiveWords=offensiveWordServiceBean.findAll();

        //check if the user is blocked
        User user = (User) request.getSession().getAttribute("user");

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(user.getBlocked());

        if(user.getBlocked()==0) {

            System.out.println("Utente non bloccato!");

            for (int i = 1; i <= numberOfResponses && user.getBlocked()==0; i++) {

                //check if the answer include some offensive words
                tmpAnswer = request.getParameter("response" + i); //retrieve the answer

                if (checkPresence(tmpAnswer, offensiveWords)) { //the user is blocked and transaction rolled back
                    userServiceBean.blockUser(user);
                    System.out.println("ora ho bloccato l'utente");
                }

                answers.add(request.getParameter("response" + i));
                questionsId.add(Integer.parseInt(request.getParameter("question" + i)));
            }
        }

        //only if the user is not blocked
        if(user.getBlocked()==0) {
            //MARKETING

            System.out.println("Utente non bloccato!");

            MarketingQuestion mq = null;
            int maId = 0;
            for (int i = 0; i < numberOfResponses; i++) {
                try {
                    mq = marketingQuestionServiceBean.find(questionsId.get(i));
                } catch (Exception e) {
                    //todo
                }
                try {
                    maId = marketingAnswerServiceBean.createMarketingAnswer(answers.get(i), user, mq);
                } catch (Exception e) {
                    //todo
                }
            }

            Product p = null;
            try {
                int productId = mq.getProductId();
                p = productServiceBean.find(productId);
            } catch (Exception e) {
                //todo
            }


            //STATISTICAL

            if (sex != null) {
                if (sex.isEmpty() || sex.equals("Not Chosen")) {
                    sex = null;
                }
            }
            if (expertiseLevel != null) {
                if (expertiseLevel.isEmpty() || expertiseLevel.equals("Not Chosen")) {
                    expertiseLevel = null;
                }
            }

            StatisticalAnswer sa = null;
            int saId = 0;

            try {
                saId = statisticalAnswerServiceBean.createStatisticalAnswer(user, p, age, sex, expertiseLevel);
                sa = statisticalAnswerServiceBean.find(saId);
            } catch (Exception e) {
                //todo
            }


            System.out.println("age: " + age);
            System.out.println("expertise: " + expertiseLevel);
            System.out.println("sex: " + sex + "\n");
            for (int i = 0; i < numberOfResponses; i++) {
                System.out.println(questionsId.get(i));
                System.out.println(answers.get(i));
                System.out.println();

            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();



            boolean committed = true; //fixme


            if(committed){
                path = "/WEB-INF/greetings.html";
                this.templateEngine.process(path, ctx, response.getWriter());
            }
            else
            {
                request.getRequestDispatcher("/GoToHomeUser").forward(request, response);
            }



        }
        else{ //the user is blocked

            request.getRequestDispatcher("/GoToHomeUser").forward(request, response);
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
