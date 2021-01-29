package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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
     * Get a single marketing answer by id
     */
    public MarketingAnswer find(int maId)
    {
        return em
                .find(MarketingAnswer.class, maId);
    }


    /**
     * Get all marketing answers
     * @return the list of all marketing answers, possibly empty
     */
    public List<MarketingAnswer> findAll()
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }


    /**
     * Get marketing answers of a question for a specific user
     * @param user the user for which we want to find the marketing answers
     * @param marketingQuestion the question for which we want find the marketing answers
     * @return the list of marketing answers, possibly empty
     */
    public List<MarketingAnswer> findMarketingAnswersByUserMarketingQuestion(User user, MarketingQuestion marketingQuestion)
    {
        return em
                .createNamedQuery("MarketingAnswer.findAll", MarketingAnswer.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getUserByUserId().getId()==user.getId())
                .filter(x -> x.getMarketingquestionByMarketingquestionId().getId()==marketingQuestion.getId())
                .collect(Collectors.toList());
    }


    /**
     * Create a new marketing answer
     * @param text the text of the marketing answer
     * @param user the user who submitted the marketing answer
     * @param marketingQuestion the question to which the user has answered
     * @return the id of the marketing answer just created
     */
    public int createMarketingAnswer(String text, User user, MarketingQuestion marketingQuestion)
    {
        MarketingAnswer marketingAnswer = new MarketingAnswer();
        marketingAnswer.setText(text);
        marketingAnswer.setUserByUserId(user);
        marketingAnswer.setMarketingquestionByMarketingquestionId(marketingQuestion);

        em.persist(marketingAnswer);
        em.flush();

        return marketingAnswer.getId();
    }
}
