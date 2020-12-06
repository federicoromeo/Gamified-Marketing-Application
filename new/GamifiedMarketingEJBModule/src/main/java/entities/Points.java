package entities;

import javax.persistence.*;

@Entity
@Table(name = "points", schema = "gamified_db")
@NamedQuery(name="Points.findAll", query="SELECT p FROM Points p")
public class Points {

    private int id;
    private int userId;
    private int productId;
    private int total;
    private User userByUserId;
    private Product productByProductId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "productId", nullable = false)
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "total", nullable = false, length = 255)
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Points points = (Points) o;

        if (id != points.id) return false;
        if (userId != points.userId) return false;
        if (productId != points.productId) return false;
        if (total != points.total) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + productId;
        result = 31 * result + total;
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
    @PrimaryKeyJoinColumn(name = "productId", referencedColumnName = "id")//, nullable = false)
    public Product getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(Product productByProductId) {
        this.productByProductId = productByProductId;
    }
}






/*
********Trigger per calcolare i punti della sezione 1:
        NB: nel nostro db il testo delle marketingAnswer deve essere not null,
        * quindi per ciascun inserimento il punteggio è incrementato di uno

        CREATE TRIGGER UpdatePointsSection1
        AFTER INSERT ON MarketingAnswer
        FOR EACH ROW
        BEGIN
            IF EXISTS (SELECT *
                        FROM Points P
                        WHERE P.userId==NEW.userId
                         AND P.productId==NEW.productId)
            THEN
                UPDATE Points
                    SET total=total+1;
                WHERE P.userId==NEW.userId
                AND P.productId==NEW.productId
            ELSE
                INSERT ON Points(userId, productid, total)
                VALUES(NEW.userId, NEW.productId, 1)
        END;

********Trigger per calcolare i punti della sezione 2:
        NB:il trigger calcola correttamente i punti solo se non vi solo valori di default in statistical answer

        CREATE TRIGGER UpdatePointsSection2
        AFTER INSERT ON StatisticalAnswer
        FOR EACH ROW
        BEGIN
            DECLARE pointToAdd integer;
                WHEN((NEW.age IS NULL, 2, 0)+ IF(NEW.expertise IS NULL, 2, 0) + IF(NEW.sex IS NULL, 2, 0) INTO pointsToAdd )>0
                    IF EXISTS ( SELECT *
                                 FROM Points P
                                 WHERE P.userId==NEW.userId
                                  AND P.productId==NEW.productId)
                    THEN
                        UPDATE Points
                            SET total=total+pointsToAdd;
                        WHERE P.userId==NEW.userId
                        AND P.productId==NEW.productId
                    ELSE
                        INSERT ON Points(userId, productid, total)
                        VALUES(NEW.userId, NEW.productId, pointsToAdd)
                 //quest'ultimo caso dovrebbe capitare raramente in quanto l'inserimento di una statisticalAnswer
                    avviene in teoria solamante l'inserimento di una marketingQuestion che scatena il precedente trigger.
                    Il caso è comunque considerato per mettersi al riparo nel caso in cui il dbms processi i trigger o
                    l'inserimento delle tuple con ordine differente da quanto aspettato

 */
