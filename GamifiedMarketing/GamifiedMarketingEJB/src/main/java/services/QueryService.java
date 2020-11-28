package services;

import javax.ejb.Stateful;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class QueryService
{
    @PersistenceContext(unitName = "AlbumEJB", type = PersistenceContextType.EXTENDED)
    private EntityManager em;
}
