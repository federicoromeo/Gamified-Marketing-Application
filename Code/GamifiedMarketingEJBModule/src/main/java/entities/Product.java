package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "product", schema = "gamified_db")
@NamedQueries({
    @NamedQuery(name="Product.findAll", query="SELECT p FROM Product p"),
    @NamedQuery(name = "Product.getOrderedPoints", query = "Select po FROM Product pr, Points po WHERE pr.id = ?1 AND po.productByProductId.id = pr.id ORDER BY po.total DESC") })
public class Product implements Serializable {

    private int id;
    private String name;
    private byte[] image;
    private Date date;
    private Collection<Log> logsById;
    private Collection<MarketingQuestion> marketingquestionsById;
    private List<Points> pointsById;
    private Collection<StatisticalAnswer> statisticalanswersById;

    @Id
    @SequenceGenerator( name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1, initialValue = 1 )
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="mySeq")
    @Column(name = "id", nullable = false)
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 255)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Lob
    @Column(name = "image", nullable = false)
    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date", unique = true, nullable = false)
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


    /**
     * cascade: when a product is removed all tuples referring to it are deleted and also orphan are deleted
     * fetch policy: (default) lazy because in average there are more access made by simple user than of made by admin (which requires all the info)
     */
    @OneToMany(
            mappedBy = "productByProductId",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
            orphanRemoval = true)
    public Collection<Log> getLogsById()
    {
        return logsById;
    }

    public void setLogsById(Collection<Log> logsById)
    {
        this.logsById = logsById;
    }


    /**
     * fetch policy: eager because users need always question to answer to them
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "productByProductId",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
            orphanRemoval = true)
    public Collection<MarketingQuestion> getMarketingquestionsById()
    {
        return marketingquestionsById;
    }

    public void setMarketingquestionsById(Collection<MarketingQuestion> marketingquestionsById)
    {
        this.marketingquestionsById = marketingquestionsById;
    }


   /**
    * fetch policy: eager because in average when a product is loaded the application
    * needs also the points made by each user (in the leaderboard)
    */
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "productByProductId",
            cascade = {CascadeType.REMOVE, CascadeType.REFRESH },
            orphanRemoval = true)
    public List<Points> getPointsById()
    {
        return pointsById;
    }

    public void setPointsById(List<Points> pointsById)
    {
        this.pointsById = pointsById;
    }


    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "productByProductId",
            cascade = { CascadeType.REMOVE, CascadeType.REFRESH },
            orphanRemoval = true)
    public Collection<StatisticalAnswer> getStatisticalanswersById()
    {
        return statisticalanswersById;
    }

    public void setStatisticalanswersById(Collection<StatisticalAnswer> statisticalanswersById)
    {
        this.statisticalanswersById = statisticalanswersById;
    }
}
