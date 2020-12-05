package entities;

import javax.persistence.*;

@Entity
@Table(name = "points", schema = "gamified_db")
@NamedQuery(name="Points.findAll", query="SELECT p FROM Points p")
public class Points {

    private int id;
    private int userId;
    private int productId;
    private String total;
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

    @Basic
    @Column(name = "total", nullable = false, length = 255)
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Points points = (Points) o;

        if (id != points.id) return false;
        if (userId != points.userId) return false;
        if (productId != points.productId) return false;
        if (total != null ? !total.equals(points.total) : points.total != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + productId;
        result = 31 * result + (total != null ? total.hashCode() : 0);
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
