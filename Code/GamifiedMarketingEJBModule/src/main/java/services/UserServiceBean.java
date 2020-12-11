package services;

import entities.User;
import exceptions.CredentialsException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Stateless(name = "UserServiceEJB")
public class UserServiceBean {

    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public UserServiceBean()
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
        user.setAdmin((byte) 0);

        em.persist(user);
        em.flush();

        return user.getId();
    }


    /**
     * Get a single user by Id
     * @param userId
     * @return the user with the specified id
     */
    public User find(int userId)
    {
        return em
                .find(User.class, userId);
    }

    /**
     * Get a single user by Email
     * @param userEmail
     * @return the user with the specified email
     */
    public User findByEmail(String userEmail)
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getEmail().equals(userEmail))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a single user by username
     * @param userUsername
     * @return the user with the specified username
     */
    public User findByUsername(String userUsername)
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getUsername().equals(userUsername))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all admins
     * @return the list of all admins, possibly empry
     */
    public List<User> findAllAdmins()
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getAdmin()==1)
                .collect(Collectors.toList());
    }


    /**
     * Get all simple users
     * @return the list of all simple users, possibly empry
     */
    public List<User> findAllSimpleUsers()
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getAdmin()==0)
                .collect(Collectors.toList());
    }


    /**
     * Get all blocked/ non blocked users
     * @param blocked 1 for retrieving the blocked users, 0 otherwise
     * @return the list of all admins, possibly empry
     */
    public List<User> findAllAdmin(byte blocked)
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getBlocked()==blocked)
                .collect(Collectors.toList());
    }



    /**
     * Get all users
     * @return the list of all users, possibly empty
     */
    public List<User> findAll()
    {
        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList();
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


    /**
     * Update a user
     * @param user the user to update
     */
    public void blockUser(User user) {
        user.setBlocked((byte) 1);
        em.merge(user);
        em.flush();
    }


     /* find User to that point
     * @param pointId id
     * @return user with that point id
     */
    public User findUserByPointId(int pointId) {

        return em
                .createNamedQuery("User.findAll", User.class)
                .getResultList()
                .stream()
                .filter(x -> x.getPointsById().equals(pointId))
                .findFirst()
                .orElse(null);

    }


}