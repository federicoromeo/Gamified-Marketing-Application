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

    /*@Override
    public boolean equals(String o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticalAnswer that = (StatisticalAnswer) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (productId != that.productId) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (expertise != null ? !expertise.equals(that.expertise) : that.expertise != null) return false;

        return true;
    }*/

    @Override
    public int hashCode() {
        int result = id;
        //result = 31 * result + (age != null ? age.hashCode() : 0);
        //result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (expertise != null ? expertise.hashCode() : 0);
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
