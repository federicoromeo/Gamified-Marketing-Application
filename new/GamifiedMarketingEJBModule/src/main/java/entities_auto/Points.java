package entities_auto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "points", schema = "gamified_db")
public class Points implements Serializable {
    private int id;
    private String total;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "total")
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
        if (total != null ? !total.equals(points.total) : points.total != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
