package entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "marketingquestion", schema = "gamified_db")
public class MarketingQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product productId;

    @OneToMany(mappedBy = "marketingquestionId")
    private Collection<MarketingAnswer> marketingAnswers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Collection<MarketingAnswer> getMarketingAnswers() {
        return marketingAnswers;
    }

    public void setMarketingAnswers(Collection<MarketingAnswer> marketingAnswers) {
        this.marketingAnswers = marketingAnswers;
    }
}
