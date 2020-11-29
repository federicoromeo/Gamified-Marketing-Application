package services;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Optional;

import entities.*;
import exceptions.*;

@Stateless
public class UserService
{
    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;


    public UserService()
    {

    }

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

    /**
     * Create an user
     * @param username the username
     * @param password the password
     * @param email the email address
     * @return the id of the newly generated user
     */
    public int createUser(String username, String password, String email)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setAdmin(false);

        em.persist(user);
        em.flush();

        return user.getId();
    }

    /**
     * Remove a user
     * @param userId the Id of the user to remove
     */
    public void deleteUser(int userId)
    {
        Optional
                .of(em.find(User.class, userId))
                .ifPresent(u -> em.remove(u));
    }
}
