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

    //RELATIONS

    @OneToMany(mappedBy = "productId")
    private Collection<Points> points;

    @OneToMany(mappedBy = "productId")
    private Collection<MarketingQuestion> marketingQuestions;

    @OneToMany(mappedBy = "productId")
    private Collection<Log> logs;

    //GETTERS AND SETTERS

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Collection<Points> getPoints() {
        return points;
    }

    public Collection<MarketingQuestion> getMarketingQuestions() {
        return marketingQuestions;
    }

    public Collection<Log> getLogs() {
        return logs;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }
}
