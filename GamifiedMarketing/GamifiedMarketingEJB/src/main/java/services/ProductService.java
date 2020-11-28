package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import entities.*;

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


    //TODO
    /**
     * Get all products
     * @return the list of all past products, possibly empty
     */
    public List<Product> findPastProducts()
    {
        return null;
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
     * Create a new product
     * @param name the name of the product
     * @param image the image associated to the product
     * @param date the day in which the product is scheduled to be "product of the day"
     * @return the id of the product just created
     */
    public int createProduct(String name, byte[] image, String date)
    {
        Product product = new Product();
        product.setName(name);
        product.setDate(date);
        product.setImage(image);

        em.persist(product);
        em.flush();

        return product.getId();
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
