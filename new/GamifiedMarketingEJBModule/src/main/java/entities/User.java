package entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", schema = "gamified_db")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private boolean admin;

    private boolean blocked;

    private String password;

    private String email;

    //RELATIONS

    @OneToMany(mappedBy = "userId")
    private Collection<StatisticalAnswer> statisticalAnswers;

    @OneToMany(mappedBy = "userId")
    private Collection<MarketingAnswer> marketingAnswers;

    @OneToMany(mappedBy = "userId")
    private Collection<Points> points;

    @OneToMany(mappedBy = "userId")
    private Collection<Log> logs;

    //GETTERS AND SETTERS

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
