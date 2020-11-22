package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "marketinganswer", schema = "gamified_db")
public class MarketingAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "id")
    private MarketingQuestion marketingquestionId;

    private String text;
}