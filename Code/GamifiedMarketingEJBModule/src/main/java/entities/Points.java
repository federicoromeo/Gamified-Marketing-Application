package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "points", schema = "gamified_db")
@NamedQuery(name="Points.findAll", query="SELECT p FROM Points p")
public class Points implements Serializable {

    private int id;
    private int total;
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


    @Column(name = "total", nullable = false, length = 255)
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
