package entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "marketingquestion", schema = "gamified_db")
@NamedQuery(name="MarketingQuestion.findAll", query="SELECT mq FROM MarketingQuestion mq")
public class MarketingQuestion
{
    private int id;
    private String text;
    private Collection<MarketingAnswer> marketinganswersById;
    private Product productByProductId;

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

    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @OneToMany(mappedBy = "marketingquestionByMarketingquestionId",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.PERSIST },
            orphanRemoval = true)
    public Collection<MarketingAnswer> getMarketinganswersById() {
        return marketinganswersById;
    }

    public void setMarketinganswersById(Collection<MarketingAnswer> marketinganswersById)
    {
        this.marketinganswersById = marketinganswersById;
    }


    @ManyToOne
    @JoinColumn(name = "productId")
    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
