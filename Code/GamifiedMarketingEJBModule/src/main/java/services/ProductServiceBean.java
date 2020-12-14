package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        return em
                .createNamedQuery("Product.findAll", Product.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList()
                .stream()
                .filter(x -> x.getDate().equals(today))
                .findFirst()
                .orElse(null);
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
     * Get all PAST  products
     * @return the list of all past products, possibly empty
     */
    public List<Product> findPastProducts() throws ParseException {

        List<Product> pastProducts = new ArrayList<>();
        List<Product> allProducts;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String todayString = LocalDate.now().format(formatter);
        LocalDate today = LocalDate.parse(todayString, formatter);
        System.out.println("today: " + today + "\n");

        allProducts = em
                        .createNamedQuery("Product.findAll", Product.class)
                        .getResultList();

        for(Product p : allProducts){

            LocalDate date = LocalDate.parse(p.getDate(), formatter);
            System.out.println(date);

            if(date.isBefore(today)) {
                pastProducts.add(p);
                System.out.println("aggiunto prod con data: " + date);
            }
            else
                System.out.println("scartato prod con data: " + date);
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
    public int createProduct(String name, byte[] image, String date)
    {
        Product product = new Product();
        product.setName(name);
        product.setDate(date);
        product.setImage(image);

        System.out.println("Prodotto creato\n\n\n");

        em.persist(product);

        try
        {
            em.flush();
        }
        catch(Exception e)
        {
            System.out.println(e.getClass());
        }

        return product.getId();

        /*
        Product duplicate = findAll()
                .stream()
                .filter(x -> x.getDate().equals(date))
                .findFirst()
                .orElse(null);

        if(duplicate == null) {
            Product product = null;
            try {
                product = new Product();
                product.setName(name);
                product.setDate(date);
                product.setImage(image);

                em.persist(product);
                em.flush();

                return product.getId();
            } catch (Exception e) {
                System.out.println("\n\n\n\n\n\n");
                System.out.println("cause.");
                System.out.println(e.getCause());
            }
        }
        /*}

        else{
            return -1;
        }*/
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

    public void updateProduct(int productId) {

        Product product=em.createNamedQuery("Product.findAll", Product.class)
                                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                                .getResultList()
                                .stream()
                                .findFirst()
                                .orElse(null);


        //em.refresh(product);


    }
}
