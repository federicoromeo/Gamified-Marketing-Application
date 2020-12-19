package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "StatisticalAnswerServiceEJB")
public class StatisticalAnswerServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public StatisticalAnswerServiceBean(){
    }

    /**
     * Get a single statisticalAnswer by Id
     */
    public StatisticalAnswer find(int statisticalAnswerId)
    {
        return em
                .find(StatisticalAnswer.class, statisticalAnswerId);
    }


    /**
     * Get all statisticalAnswers
     * @return the list of all statisticalAnswers, possibly empty
     */
    public List<StatisticalAnswer> findAll()
    {
        return em
                .createNamedQuery("StatisticalAnswer.findAll", StatisticalAnswer.class)
                .getResultList();
    }









    /**
     * Get statisticalAnswer for expertise
     * @param expertise the expertise for which we want to retrieve the statisticalAnswers
     * @return the list of statisticalAnswers of a expertise, possibly empty
     */
    public List<StatisticalAnswer> findStatisticalAnswerByExpertise(String expertise)
    {
        return em
                .createNamedQuery("StatisticalAnswer.findAll", StatisticalAnswer.class)
                .getResultList()
                .stream()
                .filter(x -> x.getExpertise().equals(expertise))
                .collect(Collectors.toList());
    }

    /**
     * Create a new StatisticalAnswer
     * @param user the user related to the statisticalAnswer
     * @param product the product related to the statisticalAnswer
     * @param age the age related to the statisticalAnswer
     * @param sex the sex related to the statisticalAnswer
     * @param expertise the sex related to the statisticalAnswer
     * @return the id of the statisticalAnswer just created
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
     * Remove a statisticalAnswer
     * @param statisticalAnswerId the Id of the statisticalAnswer to remove
     */
    public void deleteStatisticalAnswer(int statisticalAnswerId)
    {
        Optional
                .of(em.find(StatisticalAnswer.class, statisticalAnswerId))
                .ifPresent(p -> em.remove(p));
    }
}

