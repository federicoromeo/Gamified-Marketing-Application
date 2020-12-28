package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import entities.*;
import utils.Data;

@Stateless(name = "ProductServiceEJB")
public class ProductServiceBean
{
    @PersistenceContext(unitName = "PersUn")
    private EntityManager em;

    public ProductServiceBean()
    {
    }


    /**
     * Get a single product by Id
     */
    public Product find(int productId)
    {
        return em
                .find(Product.class, productId);
    }


    /**
     * Get the product of a specific day
     * @param day a Date object
     * @return the product of the specified day possibly empty
     */
    public Product findProductOfTheDay(Date day)
    {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> Data.equalDates(x.getDate(), day))
                .findFirst()
                .orElse(null);
    }


    public List<Points> findLeaderboardByProductId(int productId)
    {
        return em
                .createNamedQuery("Product.getOrderedPoints", Points.class)
                .setParameter(1, productId)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }


    /**
     * Get the default product
     * @return the default product empty
     */
    public Product findDefaultNullImage()
    {
        return em
                .find(Product.class, 2);
    }


    /**
     * Get all past  products
     * @return the list of all past products, possibly empty
     */
    public List<Product> findPastProducts()
    {
        Date today = new Date();

        return em
                        .createNamedQuery("Product.findAll", Product.class)
                        .getResultList()
                        .stream()
                        .filter(x -> x.getDate().before(today) && x.getId()!=2)
                        .collect(Collectors.toList());
    }


    /**
     * Get all products
     * @return the list of all products, possibly empty
     */
    public List<Product> findAll()
    {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }


    /**
     * Create a new product
     * @param name the name of the product
     * @param image the image associated to the product
     * @param date the day in which the product is scheduled to be "product of the day"
     * @return the id of the product just created
     */
    public int createProduct(String name, byte[] image, Date date) throws  Exception
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


    public void updateProduct(int productId)
    {
        Product product = em.createNamedQuery("Product.findAll", Product.class)
                                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                                .getResultList()
                                .stream()
                                .findFirst()
                                .orElse(null);
    }


    /**
     * Check if a product for a given date is already present
     * @param date the date
     * @return true if the database already contains a product for that day
     */
    public boolean productAlreadyCreatedForTheDate(Date date)
    {
        return findProductOfTheDay(date) != null;
    }
}
