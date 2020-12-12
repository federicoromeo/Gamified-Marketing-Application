package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "log", schema = "gamified_db")

@NamedQueries({
        @NamedQuery(name = "Log.findByUserProduct", query = "SELECT l FROM Log l  WHERE l.userId = ?1 and l.productId = ?2"),
        @NamedQuery(name= "Log.findAll", query="SELECT p FROM Product p")
})
public class Log {

    private int id;
    private Timestamp time;
    private byte submitted;
    private int userId;
    private int productId;
    private User userByUserId;
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
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "submitted", nullable = false)
    public byte getSubmitted() {
        return submitted;
    }

    public void setSubmitted(byte submitted) {
        this.submitted = submitted;
    }

    @Basic
    @Column(name = "userId", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

        Log log = (Log) o;

        if (id != log.id) return false;
        if (submitted != log.submitted) return false;
        if (userId != log.userId) return false;
        if (productId != log.productId) return false;
        if (time != null ? !time.equals(log.time) : log.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (int) submitted;
        result = 31 * result + userId;
        result = 31 * result + productId;
        return result;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "id")//, nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
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
