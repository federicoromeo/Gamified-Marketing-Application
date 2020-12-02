package entities;

import javax.persistence.*;

@Entity
@Table(name = "points", schema = "gamified_db")
public class Points
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;

    private int total;
}