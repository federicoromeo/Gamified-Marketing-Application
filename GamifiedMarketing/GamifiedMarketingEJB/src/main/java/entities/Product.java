package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "product", schema = "gamified_db")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] image;

    private String date;

    @OneToOne
    private Points points;

    @OneToMany(mappedBy = "productId")
    private Collection<MarketingQuestion> marketingQuestions;

    @OneToMany(mappedBy = "userId")
    private Collection<Log> logs;
}
