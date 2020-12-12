package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "LogServiceEJB")
public class LogServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public LogServiceBean(){
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
     * Get logs of a user
     * @param user the user for which we want to find the logs
     * @return the list of the user's logs, possibly empty
     */
    public List<Log> findLogsByUser(User user) {

        return em
                .createNamedQuery("Log.findAll", Log.class)
                .getResultList()
                .stream()
                .filter(x -> x.getUserId()==user.getId())
                .collect(Collectors.toList());
    }


    /**
     * Get logs of a product
     * @param product the product for which we want to find the logs
     * @return the list of the product's logs, possibly empty
     */
    public List<Log> findLogsByProduct(Product product)
    {
        return em
                .createNamedQuery("Log.findAll", Log.class)
                .getResultList()
                .stream()
                .filter(x -> x.getProductId()==product.getId())
                .collect(Collectors.toList());

    }


    /**
     * Get logs of a user for a product
     * @param user the user for which we want to find the logs
     * @param product the id of the user for which we want find the logs
     * @return the list of the user and product' logs, possibly empty
     */
    public List<Log> findLogsByUserProduct(User user, Product product)
    {
        return em
                .createNamedQuery("Log.findAll", Log.class)
                .getResultList()
                .stream()
                .filter(x -> x.getUserId()==user.getId())
                .filter(x -> x.getProductId()==product.getId())
                .collect(Collectors.toList());


    }


    /**
     * Get submitted or cancelled logs
     * @param submitted indicates if we want submitted or cancelled logs
     * @return the list of submitted or cancelled logs, possibly empty
     */
    public List<Log> findSubmittedCancelledLogs(byte submitted)
    {
        return em
                .createNamedQuery("Log.findAll", Log.class)
                .getResultList()
                .stream()
                .filter(x -> x.getSubmitted()==submitted)
                .collect(Collectors.toList());
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

    public boolean isLogPresent(int userId, int productId)
    {
        return (long) em
                .createNamedQuery("Log.findByUserProduct", Log.class)
                .setParameter(1, userId)
                .setParameter(2, productId)
                .getResultList()
                .size() > 0;
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
        log.setUserId(user.getId());
        log.setProductId(product.getId());
        log.setSubmitted(submitted);
        log.setTime(timestamp);

        em.persist(log);
        em.flush();

        return log.getId();
    }

    /**
     * Remove a log
     * @param logId the Id of the log to remove
     */
    public void deleteLog(int logId)
    {
        Optional
                .of(em.find(Log.class, logId))
                .ifPresent(p -> em.remove(p));
    }
}
