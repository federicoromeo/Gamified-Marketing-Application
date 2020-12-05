package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "MarketingQuestionServiceEJB")
public class MarketingQuestionServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public MarketingQuestionServiceBean(){
    }

    /**
     * Get a single marketingQuestion by Id
     */
    public MarketingQuestion find(int mqId)
    {
        return em
                .find(MarketingQuestion.class, mqId);
    }


    /**
     * Get all marketingQuestions
     * @return the list of all marketingQuestions, possibly empty
     */
    public List<MarketingQuestion> findAll()
    {
        return em
                .createNamedQuery("MarketingQuestion.findAll", MarketingQuestion.class)
                .getResultList();
    }






    /**
     * Get marketingQuestions of a product
     * @param product the product for which we want to find the marketingQuestions
     * @return the list of the product's marketingQuestions, possibly empty
     */
    public List<MarketingQuestion> findMarketingQuestionsByProduct(Product product)
    {
        return em
                .createNamedQuery("MarketingQuestion.findAll", MarketingQuestion.class)
                .getResultList()
                .stream()
                .filter(x -> x.getProductId()==product.getId())
                .collect(Collectors.toList());
    }





    /**
     * Create a new marketingQuestion
     * @param text the text of the question
     * @param product the product to which the question is related
     * @return the id of the marketingAnswer just created
     */
    public int createMarketingQuestion(String text, Product product)
    {
        MarketingQuestion marketingQuestion=new MarketingQuestion();
        marketingQuestion.setText(text);
        marketingQuestion.setProductId(product.getId());

        em.persist(marketingQuestion);
        em.flush();

        return marketingQuestion.getId();
    }

    /**
     * Remove a marketingQuestion
     * @param marketingQuestionId the Id of the marketingQuestion to remove
     */
    public void deleteMarketingQuestion(int marketingQuestionId)
    {
        Optional
                .of(em.find(MarketingAnswer.class, marketingQuestionId))
                .ifPresent(p -> em.remove(p));
    }
}

