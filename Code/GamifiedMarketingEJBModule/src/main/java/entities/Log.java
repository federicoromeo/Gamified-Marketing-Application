package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "log", schema = "gamified_db")
@NamedQueries({
    @NamedQuery(name = "Log.findByUserProduct", query = "SELECT l FROM Log l  WHERE l.userByUserId.id = ?1 and l.productByProductId.id = ?2"),
    @NamedQuery(name= "Log.findAll", query="SELECT p FROM Product p")
})
public class Log
{
    private int id;
    private Timestamp time;
    private byte submitted;
    private User userByUserId;
    private Product productByProductId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Column(name = "submitted", nullable = false)
    public byte getSubmitted() {
        return submitted;
    }

    public void setSubmitted(byte submitted) {
        this.submitted = submitted;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
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
