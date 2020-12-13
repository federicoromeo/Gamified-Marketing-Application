package entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "product", schema = "gamified_db")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product {

    private int id;
    private String name;
    private byte[] image;
    private String date;
    private Collection<Log> logsById;
    private Collection<MarketingQuestion> marketingquestionsById;
    private List<Points> pointsById;
    private Collection<StatisticalAnswer> statisticalanswersById;

    @Id
    @SequenceGenerator( name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1, initialValue = 1 )
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="mySeq")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob //@Basic
    @Column(name = "image", nullable = false)
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "date", unique = true, nullable = false)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //when a product is removed all tuples referring to it are deleted
    //also orphan are deleted
    //cascade policy: lazy because in average there are more access made by simple user than of made by admin (which requires all the info),
    //cascade policy: eager only for marketing question because users need always question to answer to them

    @OneToMany(mappedBy = "productByProductId", cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<Log> getLogsById() {
        return logsById;
    }

    public void setLogsById(Collection<Log> logsById) {
        this.logsById = logsById;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productByProductId", cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<MarketingQuestion> getMarketingquestionsById() {
        return marketingquestionsById;
    }

    public void setMarketingquestionsById(Collection<MarketingQuestion> marketingquestionsById) {
        this.marketingquestionsById = marketingquestionsById;
    }

   /* Fetch type EAGER allows resorting the relationship list content also in the
	 * client Web servlet after the creation of a new mission. If you leave the
	 * default LAZY policy, the relationship is sorted only at the first access but
	 * then adding a new mission does not trigger the reloading of data from the
	 * database and thus the sort method in the client does not actually re-sort the
	 * list of missions.
	 * In average when a product is loaded the application need also the points made by each user (in the leaderboard)
    */

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productByProductId", cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    @OrderBy("total DESC")
    public List<Points> getPointsById() {
        return pointsById;
    }

    public void setPointsById(List<Points> pointsById) {
        this.pointsById = pointsById;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productByProductId", cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<StatisticalAnswer> getStatisticalanswersById() {
        return statisticalanswersById;
    }

    public void setStatisticalanswersById(Collection<StatisticalAnswer> statisticalanswersById) {
        this.statisticalanswersById = statisticalanswersById;
    }
}
