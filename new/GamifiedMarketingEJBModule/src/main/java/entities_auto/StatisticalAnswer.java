package entities_auto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "statisticalanswer", schema = "gamified_db")
public class StatisticalAnswer implements Serializable {
    private int id;
    private Integer age;
    private String sex;
    private String expertise;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "expertise")
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    /*@Override
    public boolean equals(String o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticalAnswer that = (StatisticalAnswer) o;

        if (id != that.id) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (expertise != null ? !expertise.equals(that.expertise) : that.expertise != null) return false;

        return true;
    }*/

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (expertise != null ? expertise.hashCode() : 0);
        return result;
    }
}
