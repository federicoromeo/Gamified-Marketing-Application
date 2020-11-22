package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "points", schema = "gamified_db")
public class Points
{
    @Id
    @OneToOne(mappedBy = "id")
    private User userId;

    @Id
    @OneToOne(mappedBy = "id")
    private Product productId;

    private int total;
}