package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.text.ParseException;
import java.util.*;
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
     * Get the product of the day
     * @param day date formatted like yyyy/mm/dd
     * @return the product of the day correspondent to today if present, possibly empty
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
    public Product findDefaultNullImage() {
        return em
                .find(Product.class, 2);
    }


    /**
     * Get all PAST  products
     * @return the list of all past products, possibly empty
     */
    public List<Product> findPastProducts() throws ParseException
    {
        List<Product> pastProducts = new ArrayList<>();
        List<Product> allProducts;
        Date today = new Date();

        allProducts = em
                        .createNamedQuery("Product.findAll", Product.class)
                        .getResultList();

        for(Product p : allProducts)
        {
            if(p.getDate().before(today) && p.getId()!=2)  //remove default one
            {
                pastProducts.add(p);
                System.out.println("aggiunto prod con data: " + p.getDate());
            }
            else
                System.out.println("scartato prod con data: " + p.getDate());
        }

        System.out.println("PAST PRODUCTS:");
        for(Product p : pastProducts)
            System.out.println("prod: " + p.getName() + ", date: "+ p.getDate());

        return pastProducts;
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
