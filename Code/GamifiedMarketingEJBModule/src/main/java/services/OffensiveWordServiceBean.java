package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import entities.*;

@Stateless(name = "OffensiveWordServiceEJB")
public class OffensiveWordServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public OffensiveWordServiceBean()
    {
    }

    /**
     * Get a single offensive word by Id
     */
    public OffensiveWord find(int offensiveWordId)
    {
        return em
                .find(OffensiveWord.class, offensiveWordId);
    }


    /**
     * Get all offensive words
     * @return the list of words, possibly empty
     */
    public List<OffensiveWord> findAll()
    {
        return em
                .createNamedQuery("OffensiveWord.findAll", OffensiveWord.class)
                .getResultList();
    }
}

