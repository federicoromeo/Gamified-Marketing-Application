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


    @Column(name = "age", nullable = true)
    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }


    @Column(name = "sex", nullable = true)
    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }


    @Column(name = "expertise", nullable = true)
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    @ManyToOne
    @JoinColumn(name = "userId")//, nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "productId")//, nullable = false)
    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}
