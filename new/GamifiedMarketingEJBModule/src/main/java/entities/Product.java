package entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "product", schema = "gamified_db")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
@NamedQuery(name="Blob.find", query="SELECT p.image FROM Product p")
public class Product {

    private int id;
    private String name;
    private byte[] image;
    private String date;
    private Collection<Log> logsById;
    private Collection<MarketingQuestion> marketingquestionsById;
    private Collection<Points> pointsById;
    private Collection<StatisticalAnswer> statisticalanswersById;

    @Id
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
    @Column(name = "date", nullable = false)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @OneToMany(mappedBy = "productByProductId")
    public Collection<Log> getLogsById() {
        return logsById;
    }

    public void setLogsById(Collection<Log> logsById) {
        this.logsById = logsById;
    }

    @OneToMany(mappedBy = "productByProductId")
    public Collection<MarketingQuestion> getMarketingquestionsById() {
        return marketingquestionsById;
    }

    public void setMarketingquestionsById(Collection<MarketingQuestion> marketingquestionsById) {
        this.marketingquestionsById = marketingquestionsById;
    }

    @OneToMany(mappedBy = "productByProductId")
    public Collection<Points> getPointsById() {
        return pointsById;
    }

    public void setPointsById(Collection<Points> pointsById) {
        this.pointsById = pointsById;
    }

    @OneToMany(mappedBy = "productByProductId")
    public Collection<StatisticalAnswer> getStatisticalanswersById() {
        return statisticalanswersById;
    }

    public void setStatisticalanswersById(Collection<StatisticalAnswer> statisticalanswersById) {
        this.statisticalanswersById = statisticalanswersById;
    }
}
