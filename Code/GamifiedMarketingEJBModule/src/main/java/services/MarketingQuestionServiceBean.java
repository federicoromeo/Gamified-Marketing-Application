package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "MarketingQuestionServiceEJB")
public class MarketingQuestionServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public MarketingQuestionServiceBean()
    {
    }

    /**
     * Get a single question by Id
     */
    public MarketingQuestion find(int mqId)
    {
        return em
                .find(MarketingQuestion.class, mqId);
    }


    /**
     * Get all questions
     * @return the list of all questions, possibly empty
     */
    public List<MarketingQuestion> findAll()
    {
        return em
                .createNamedQuery("MarketingQuestion.findAll", MarketingQuestion.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }


    /**
     * Get questions of a product
     * @param product the product for which we want to find the questions
     * @return the list of questions, possibly empty
     */
    public List<MarketingQuestion> findMarketingQuestionsByProduct(Product product)
    {
        return em
                .createNamedQuery("MarketingQuestion.findAll", MarketingQuestion.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getProductByProductId().getId() == product.getId())
                .collect(Collectors.toList());
    }


    /**
     * Create a new question
     * @param text the text of the question
     * @param product the product to which the question is related
     * @return the id of the question just created
     */
    public int createMarketingQuestion(String text, Product product)
    {
        MarketingQuestion marketingQuestion = new MarketingQuestion();
        marketingQuestion.setText(text);
        marketingQuestion.setProductByProductId(product); //added fede

        em.persist(marketingQuestion);
        em.flush();

        System.out.println("Created question " + marketingQuestion.getId());
        return marketingQuestion.getId();
    }
}

