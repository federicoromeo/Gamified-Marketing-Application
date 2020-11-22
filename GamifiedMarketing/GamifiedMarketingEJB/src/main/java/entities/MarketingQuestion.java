package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "marketingquestion", schema = "gamified_db")
public class MarketingQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product productId;

    @OneToMany(mappedBy = "marketingquestionId")
    private Collection<MarketingQuestion> marketingQuestions;
}
