package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import entities.*;

@Stateless(name = "StatisticalAnswerServiceEJB")
public class StatisticalAnswerServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public StatisticalAnswerServiceBean(){
    }

    /**
     * Get a single statistical answer by Id
     */
    public StatisticalAnswer find(int statisticalAnswerId)
    {
        return em
                .find(StatisticalAnswer.class, statisticalAnswerId);
    }


    /**
     * Get all statistical answers
     * @return the list of all statistical answers, possibly empty
     */
    public List<StatisticalAnswer> findAll()
    {
        return em
                .createNamedQuery("StatisticalAnswer.findAll", StatisticalAnswer.class)
                .getResultList();
    }


    /**
     * Create a new statistical answer
     * @param user the user related to the statistical answer
     * @param product the product related to the statistical answer
     * @param age the age related to the statistical answer
     * @param sex the sex related to the statistical answer
     * @param expertise the sex related to the statistical answer
     * @return the id of the statistical answer just created
     */
    public int createStatisticalAnswer(User user, Product product, byte age, byte sex, String expertise)
    {
        StatisticalAnswer statisticalAnswer=new StatisticalAnswer();
        statisticalAnswer.setUserId(user.getId());
        statisticalAnswer.setProductId(product.getId());
        statisticalAnswer.setAge(age);
        statisticalAnswer.setSex(sex);
        statisticalAnswer.setExpertise(expertise);

        em.persist(statisticalAnswer);
        em.flush();

        return statisticalAnswer.getId();
    }


    /**
     * Remove a statistical answer
     * @param statisticalAnswerId the id of the statistical answer to remove
     */
    public void deleteStatisticalAnswer(int statisticalAnswerId)
    {
        Optional
                .of(em.find(StatisticalAnswer.class, statisticalAnswerId))
                .ifPresent(p -> em.remove(p));
    }
}

