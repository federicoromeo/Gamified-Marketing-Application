package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "log", schema = "gamified_db")
public class Log
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Timestamp time;

    private boolean outcome;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;
}
