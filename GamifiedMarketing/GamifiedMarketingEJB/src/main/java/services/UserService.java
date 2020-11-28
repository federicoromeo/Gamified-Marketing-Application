package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import java.util.List;

import entities.*;
import exceptions.*;

@Stateless
public class UserService
{
    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    /**
     * Check the credential of the user and eventually fetch the user object
     * @param username the username
     * @param password the password
     * @return the user
     * @throws CredentialsException when the credentials are not in the database
     * @throws NonUniqueResultException when there is more than one user associated with such credentials
     */
    public User checkCredentials(String username, String password) throws CredentialsException, NonUniqueResultException
    {
        try
        {
            List<User> users = em
                    .createNamedQuery("User.checkCredentials", User.class)
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getResultList();

            if(users.isEmpty())
                return null;

            if(users.size() == 1)
                return users.get(0);

            throw new NonUniqueResultException("More than one user registered with same credentials");
        }
        catch (PersistenceException e)
        {
            throw new CredentialsException("Could not verify credentials");
        }
    }
}
