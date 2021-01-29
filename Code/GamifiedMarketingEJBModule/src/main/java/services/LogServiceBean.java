package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

import entities.*;

@Stateless(name = "LogServiceEJB")
public class LogServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public LogServiceBean()
    {
    }

    /**
     * Get a single log by Id
     * @param logId the id of the log to find
     * @return the log with that id, possibly empty
     */
    public Log find(int logId)
    {
        return em
                .find(Log.class, logId);
    }

    /**
     * Get all logs
     * @return the list of all logs, possibly empty
     */
    public Log findAll()
    {
        return em
                .createNamedQuery("Log.findAll", Log.class)
                .getResultList()
                .get(0);
    }


    /**
     * Check if a log with the specified status is already present
     * @param userId the user referred in the log
     * @param productId the product referred in the log
     * @param submitted the status of the log to look for (true = submitted, false = canceled)
     * @return true if a log with that status is present
     */
    public boolean isLogPresent(int userId, int productId, boolean submitted)
    {
        return em
                .createNamedQuery("Log.findByUserProduct", Log.class)
                .setParameter(1, userId)
                .setParameter(2, productId)
                .getResultList()
                .stream()
                .anyMatch(l -> l.getSubmitted() == (submitted ? 1 : 0));
    }


    /**
     * Create a new log
     * @param user the user the log refers to
     * @param product the log refers to
     * @param submitted indicates if the product's questionnaire made by the user was submitted
     * @param timestamp the timestamp in which the user submitted or cancelled the questionnaire
     * @return the id of the log just created
     */
    public int createLog(User user, Product product, byte submitted, Timestamp timestamp)
    {
        Log log = new Log();
        log.setUserByUserId(user);
        log.setProductByProductId(product);
        log.setSubmitted(submitted);
        log.setTime(timestamp);

        em.persist(log);
        em.flush();

        return log.getId();
    }
}
