package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import entities.*;

@Stateless(name = "ProductServiceEJB")
public class ProductServiceBean
{
    @PersistenceContext(unitName = "PersUn")
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
     * @return the product of the day correspondent to today if present, possibly empty
     */
    public Product findProductOfTheDay(String today) {
        // try {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .getResultList()
                .stream()
                .filter(x -> x.getDate().equals(today))
                .findFirst()
                .orElse(null);
        // }
        //catch(Exception e){
        //  System.out.println(e.getMessage());
        // }
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

   /* static <T, E extends Exception> Consumer<T>
    consumerWrapper(Consumer<T> consumer, Class<E> clazz) {

        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    System.err.println(
                            "Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    } */


    /**
     * Get all products
     * @return the list of all past products, possibly empty
     */
    /* todo
    public List<Product> findPastProducts()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");
        Date today = (Date) Calendar.getInstance().getTime();


        return
                em
                    .createNamedQuery("Product.findAll", Product.class)
                    .getResultList()
                    .stream()
                    .filter( consumerWrapper(x-> dateFormat.parse(((Product)x).getDate()).before(today), ParseException.class));



    }*/

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
    public int createProduct(String name, byte[] image, String date) throws Exception {

        Product duplicate = findAll()
                                    .stream()
                                    .filter(x -> x.getDate().equals(date))
                                    .findFirst()
                                    .orElse(null);

        if(duplicate == null){
            Product product = null;
            try
            {
                product = new Product();
                product.setName(name);
                product.setDate(date);
                product.setImage(image);

                em.persist(product);
                em.flush();

                return product.getId();
            }
            //catch(SQLIntegrityConstraintViolationException e){
            catch(Exception e){
                System.out.println(e.getMessage());
                //em.getTransaction().rollback();
                return -1;
            }
        }

        else{
            return -1;
        }
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
