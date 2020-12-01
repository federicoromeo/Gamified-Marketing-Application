package entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "statisticalanswer", schema = "gamified_db")
public class StatisticalAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int age;

    private Sex sex;

    private int productId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

    private Expertise expertise;
}
