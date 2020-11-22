package entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", schema = "gamified_db")
public class User
{
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
