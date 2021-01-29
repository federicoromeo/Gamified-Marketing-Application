package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entities.*;

@Stateless(name = "PointServiceEJB")
public class PointsServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public PointsServiceBean(){
    }

    /**
     * Get a single point by id
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
}


