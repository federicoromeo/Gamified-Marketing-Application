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
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "marketingquestionId")
    private MarketingQuestion marketingquestionId;

    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public MarketingQuestion getMarketingquestionId() {
        return marketingquestionId;
    }

    public void setMarketingquestionId(MarketingQuestion marketingquestionId) {
        this.marketingquestionId = marketingquestionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}