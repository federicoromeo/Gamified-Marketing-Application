package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import entities.*;

@Stateless(name = "OffensiveWordServiceEJB")
public class OffensiveWordServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public OffensiveWordServiceBean(){
    }

    /**
     * Get a single offensiveWord by Id
     */
    public OffensiveWord find(int offensiveWordId)
    {
        return em
                .find(OffensiveWord.class, offensiveWordId);
    }


    /**
     * Get all offensiveWords
     * @return the list of all offensiveWords, possibly empty
     */
    public List<OffensiveWord> findAll()
    {
        return em
                .createNamedQuery("OffensiveWord.findAll", OffensiveWord.class)
                .getResultList();
    }



    /**
     * Create a new offensiveWord
     * @param word the word of the offensiveWord
     * @return the id of the offensiveWord just created
     */
    public int createOffensiveWord(String word)
    {
        OffensiveWord offensiveWord=new OffensiveWord();
        offensiveWord.setWord(word);

        em.persist(offensiveWord);
        em.flush();

        return offensiveWord.getId();
    }

    /**
     * Remove a offensiveWord
     * @param offensiveWordId the Id of the offensiveWord to remove
     */
    public void deleteOffensiveWord(int offensiveWordId)
    {
        Optional
                .of(em.find(OffensiveWord.class, offensiveWordId))
                .ifPresent(p -> em.remove(p));
    }
}

