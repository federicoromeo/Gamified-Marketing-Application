package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "MarketingAnswerServiceEJB")
public class MarketingAnswerServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public MarketingAnswerServiceBean(){
    }

    /**
     * Get a single marketingAnswer by Id
     */
    public MarketingAnswer find(int maId)
    {
        return em
                .find(MarketingAnswer.class, maId);
    }


    /**
     * Get all marketingAnswers
     * @return the list of all marketingAnswers, possibly empty
     */
    public List<MarketingAnswer> findAll()
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }




    /**
     * Get marketingAnswers of a user
     * @param user the user for which we want to find the marketingAnswers
     * @return the list of the user's marketingAnswers, possibly empty
     */
    public List<MarketingAnswer> findMarketingAnswerByUser(User user)
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getUserId()==user.getId())
                .collect(Collectors.toList());
    }



    /**
     * Get marketingAnswers of a marketingQuestion
     * @param marketingQuestion the marketingQuestion for which we want find the marketingAnswers
     * @return the list of the marketingQuestion's marketingAnswers, possibly empty
     */
    public List<MarketingAnswer> findMarketingAnswersByMarketingQuestion(MarketingQuestion marketingQuestion)
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getMarketingquestionId()==marketingQuestion.getId())
                .collect(Collectors.toList());
    }


    /**
     * Get marketingAnswers of a marketingQuestion and of a specific user
     * @param user the user for which we want to find the marketingAnswers
     * @param marketingQuestion the marketingQuestion for which we want find the marketingAnswers
     * @return the list of the marketingQuestion and user' marketingAnswers, possibly empty
     */
    public List<MarketingAnswer> findMarketingAnswersByUserMarketingQuestion(User user, MarketingQuestion marketingQuestion)
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getUserId()==user.getId())
                .filter(x -> x.getMarketingquestionId()==marketingQuestion.getId())
                .collect(Collectors.toList());
    }





    /**
     * Create a new marketingAnswer
     * @param text the text of the answer
     * @param user the user who write the answer
     * @param marketingQuestion the question to which the user has answered
     * @return the id of the marketingAnswer just created
     */
    public int createMarketingAnswer(String text, User user, MarketingQuestion marketingQuestion)
    {
        MarketingAnswer marketingAnswer = new MarketingAnswer();
        marketingAnswer.setText(text);
        marketingAnswer.setUserId(user.getId());
        marketingAnswer.setMarketingquestionId(marketingQuestion.getId());

        em.persist(marketingAnswer);
        em.flush();

        return marketingAnswer.getId();
    }

    /**
     * Remove a marketingAnswer
     * @param marketingAnswerId the Id of the marketingAnswer to remove
     */
    public void deleteMarketingAnswer(int marketingAnswerId)
    {
        Optional
                .of(em.find(MarketingAnswer.class, marketingAnswerId))
                .ifPresent(p -> em.remove(p));
    }
}
