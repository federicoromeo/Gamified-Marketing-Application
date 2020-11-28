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

    @OneToMany(mappedBy = "userId")
    private Collection<StatisticalAnswer> statisticalAnswers;

    @OneToMany(mappedBy = "userId")
    private Collection<MarketingAnswer> marketingAnswers;

    @OneToOne
    private Points points;

    @OneToMany(mappedBy = "userId")
    private Collection<Log> logs;
}
