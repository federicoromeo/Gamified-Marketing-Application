package entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "marketingquestion", schema = "gamified_db")
public class MarketingQuestion {

    private int id;
    private String text;
    private int productId;
    private Collection<MarketingAnswer> marketinganswersById;
    private Product productByProductId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "productId", nullable = false)
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketingQuestion that = (MarketingQuestion) o;

        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + productId;
        return result;
    }

    @OneToMany(mappedBy = "marketingquestionByMarketingquestionId")
    public Collection<MarketingAnswer> getMarketinganswersById() {
        return marketinganswersById;
    }

    public void setMarketinganswersById(Collection<MarketingAnswer> marketinganswersById) {
        this.marketinganswersById = marketinganswersById;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "productId", referencedColumnName = "id")//, nullable = false)
    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}