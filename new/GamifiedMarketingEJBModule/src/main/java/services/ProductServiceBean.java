package services;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import entities.*;

@Stateless(name = "ProductServiceEJB")
public class ProductServiceBean
{
    @PersistenceContext(unitName = "PersUn3")
    private EntityManager em;

    public ProductServiceBean(){
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
     * @param today date formatted like yyyy/mm/dd
     * @return the product of the day correspondant to today if present,  "null"o therwise
     */
    public Product findProductOfTheDay(String today) {

            return em
                    .createNamedQuery("Product.findAll", Product.class)
                    .getResultList()
                    .stream()
                    .filter(x -> x.getDate().equals(today))
                    .findFirst()
                    .orElse(null);
    }

    /**
     * Get the default product, which in the database is stored with id = 2
     * @return the default product when no image is present for the day
     */
    public Product findDefault()
    {
        return em
                .find(Product.class, 2);

                /*.createNamedQuery("Product.findAll", Product.class)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);*/
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
