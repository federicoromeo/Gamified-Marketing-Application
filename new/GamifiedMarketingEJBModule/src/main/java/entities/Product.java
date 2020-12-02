package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "product", schema = "gamified_db")
public class Product {

    private int id;
    private String name;
    private byte[] image;
    private Date date;
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

    @Basic
    @Column(name = "image", nullable = false)
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (!Arrays.equals(image, product.image)) return false;
        if (date != null ? !date.equals(product.date) : product.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
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
