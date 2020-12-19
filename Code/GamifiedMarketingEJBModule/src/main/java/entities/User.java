package entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user", schema = "gamified_db")
@NamedQueries({
    @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
    @NamedQuery(name = "User.checkRegistration", query = "SELECT r FROM User r  WHERE r.username = ?1"),
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u")
})
public class User {

    private int id;
    private byte admin;
    private byte blocked;
    private String username;
    private String email;
    private String password;
    private Date birthDate;
    private String sex;
    private Collection<Log> logsById;
    private Collection<MarketingAnswer> marketinganswersById;
    private Collection<Points> pointsById;
    private Collection<StatisticalAnswer> statisticalanswersById;

    @Id
    @SequenceGenerator( name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1, initialValue = 1 )
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="mySeq")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "birthDate", unique = true, nullable = false)
    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @Basic
    @Column(name = "sex", nullable = true)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "admin", nullable = false)
    public byte getAdmin() {
        return admin;
    }

    public void setAdmin(byte admin) {
        this.admin = admin;
    }

    @Basic
    @Column(name = "blocked", nullable = false)
    public byte getBlocked() {
        return blocked;
    }

    public void setBlocked(byte blocked) {
        this.blocked = blocked;
    }

    @Basic
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (admin != user.admin) return false;
        if (blocked != user.blocked) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (int) admin;
        result = 31 * result + (int) blocked;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    //when a user is removed all tuples referring to him/her are deleted
    //also orphan are deleted
    //cascade policy: lazy because in average there are more access made by simple user than of those made by admin (which requires all the info)

    @OneToMany(mappedBy = "userByUserId" ,cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<Log> getLogsById() {
        return logsById;
    }

    public void setLogsById(Collection<Log> logsById) {
        this.logsById = logsById;
    }

    @OneToMany(mappedBy = "userByUserId", cascade = {CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<MarketingAnswer> getMarketinganswersById() {
        return marketinganswersById;
    }

    public void setMarketinganswersById(Collection<MarketingAnswer> marketinganswersById) {
        this.marketinganswersById = marketinganswersById;
    }

    @OneToMany(mappedBy = "userByUserId", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<Points> getPointsById() {
        return pointsById;
    }

    public void setPointsById(Collection<Points> pointsById) {
        this.pointsById = pointsById;
    }

    @OneToMany(mappedBy = "userByUserId", cascade = {CascadeType.REMOVE,
            CascadeType.REFRESH }, orphanRemoval = true)
    public Collection<StatisticalAnswer> getStatisticalanswersById() {
        return statisticalanswersById;
    }

    public void setStatisticalanswersById(Collection<StatisticalAnswer> statisticalanswersById) {
        this.statisticalanswersById = statisticalanswersById;
    }
}
