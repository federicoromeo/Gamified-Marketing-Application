
package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Stateless(name = "UserServiceEJB")
public class UserServiceBean {

    @PersistenceContext(unitName = "PersUn3")
    private EntityManager em;

    public UserServiceBean()
    {

    }


}