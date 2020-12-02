package entities;

import javax.persistence.*;

@Entity
@Table(name = "marketinganswer", schema = "gamified_db")
public class MarketingAnswer {

    private int id;
    private String text;
    private int userId;
    private int marketingquestionId;
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

    @Basic
    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "userId", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "marketingquestionId", nullable = false)
    public int getMarketingquestionId() {
        return marketingquestionId;
    }

    public void setMarketingquestionId(int marketingquestionId) {
        this.marketingquestionId = marketingquestionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketingAnswer that = (MarketingAnswer) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (marketingquestionId != that.marketingquestionId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + marketingquestionId;
        return result;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "id")//, nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "marketingquestionId", referencedColumnName = "id")//, nullable = false)
    public MarketingQuestion getMarketingquestionByMarketingquestionId() {
        return marketingquestionByMarketingquestionId;
    }

    public void setMarketingquestionByMarketingquestionId(MarketingQuestion marketingquestionByMarketingquestionId) {
        this.marketingquestionByMarketingquestionId = marketingquestionByMarketingquestionId;
    }
}
