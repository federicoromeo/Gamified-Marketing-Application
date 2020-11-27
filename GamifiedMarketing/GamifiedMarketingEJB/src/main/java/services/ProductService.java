package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import entities.*;

//TODO implement createProduct

@Stateless
public class ProductService
{
    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    /**
     * Get a single product by Id
     */
    public Product find(int productId)
    {
        return em
                .find(Product.class, productId);
    }

    /**
     * Get the default product
     * @return the default product (the first one) if present, "null" otherwise
     */
    public Product findDefault()
    {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all products
     * @return the list of all products, possibly empty
     */
    public List<Product> findAll()
    {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .getResultList();
    }

    /**
     * Remove a product
     * @param productId the Id of the product to remove
     */
    public void deleteProduct(int productId)
    {
        Optional
                .of(em.find(Product.class, productId))
                .ifPresent(p -> em.remove(p));
    }
}
