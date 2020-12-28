package entities;

import javax.persistence.*;

@Entity
@Table(name = "statisticalanswer", schema = "gamified_db")
@NamedQuery(name="StatisticalAnswer.findAll", query="SELECT sa FROM StatisticalAnswer sa")
public class StatisticalAnswer {

    private int id;
    private byte age;
    private byte sex;
    private String expertise;
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
    @Column(name = "age", nullable = true)
    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }


    @Basic
    @Column(name = "sex", nullable = true)
    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }


    @Basic
    @Column(name = "expertise", nullable = true)
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
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
