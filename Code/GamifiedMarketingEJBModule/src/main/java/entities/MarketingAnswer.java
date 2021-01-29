package entities;

import javax.persistence.*;

@Entity
@Table(name = "marketinganswer", schema = "gamified_db")
@NamedQuery(name="MarketingAnswer.findAll", query="SELECT ma FROM MarketingAnswer ma")
public class MarketingAnswer
{
    private int id;
    private String text;
    private User userByUserId;
    private MarketingQuestion marketingquestionByMarketingquestionId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @ManyToOne
    @JoinColumn(name = "userId")
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }


    @ManyToOne
    @JoinColumn(name = "marketingquestionId")
    public MarketingQuestion getMarketingquestionByMarketingquestionId()
    {
        return marketingquestionByMarketingquestionId;
    }

    public void setMarketingquestionByMarketingquestionId(MarketingQuestion marketingquestionByMarketingquestionId)
    {
        this.marketingquestionByMarketingquestionId = marketingquestionByMarketingquestionId;
    }
}
