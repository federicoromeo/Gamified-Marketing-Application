package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import entities.*;

@Stateless(name = "PointServiceEJB")
public class PointsServiceBean
{
    @PersistenceContext(unitName = "GamifiedDB")
    private EntityManager em;

    public PointsServiceBean(){
    }

    /**
     * Get a single point by Id
     */
    public Points find(int pointsId)
    {
        return em
                .find(Points.class, pointsId);
    }


    /**
     * Get all points
     * @return the list of all points, possibly empty
     */
    public List<Points> findAll()
    {
        return em
                .createNamedQuery("Points.findAll", Points.class)
                .getResultList();
    }





    //TODO
    /**
     * Get points of a product
     * @param product the product for which we want to find the points
     * @return the list of the product's points, possibly empty
     */
    public List<Points> findPointsByProduct(Product product)
    {
        return null;
    }

    //TODO
    /**
     * Get points of a user
     * @param user the user for which we want to find the points
     * @return the list of the user's points, possibly empty
     */
    public List<Points> findPointsByUser(Product user)
    {
        return null;
    }



    /**
     * Create a new points
     * @param user the user which refer the points
     * @param product the product which refer the points
     * @param total the total number of points
     * @return the id of the points just created
     */
    public int createPoints(User user, Product product, int total)
    {
       Points points=new Points();
       points.setUserId(user.getId());
       points.setProductId(product.getId());
       points.setTotal(total);

        em.persist(points);
        em.flush();

        return points.getId();
    }

    /**
     * Remove points
     * @param pointsId the Id of the points to remove
     */
    public void deletePoints(int pointsId)
    {
        Optional
                .of(em.find(Points.class, pointsId))
                .ifPresent(p -> em.remove(p));
    }
}


