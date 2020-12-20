package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "points", schema = "gamified_db")
@NamedQuery(name="Points.findAll", query="SELECT p FROM Points p")
public class Points implements Serializable {

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

       CREATE DEFINER=`root`@`localhost` TRIGGER `marketinganswer_AFTER_INSERT` AFTER INSERT ON `marketinganswer` FOR EACH ROW BEGIN

	DECLARE x INTEGER;
    SELECT productId INTO x
    FROM marketingQuestion
    WHERE NEW.marketingquestionId=id;

            IF EXISTS (SELECT *
                        FROM points p
                        WHERE p.userId=NEW.userId
                         AND p.productId=x)
            THEN
                UPDATE points
                    SET total=total+1
                WHERE userId=NEW.userId
                AND productId=x;
            ELSE
                INSERT INTO points(userId, productid, total)
                VALUES(NEW.userId, x, 1);
			END IF;
END

********Trigger per calcolare i punti della sezione 2:
        NB:il trigger calcola correttamente i punti solo se non vi solo valori di default in statistical answer

       CREATE DEFINER=`root`@`localhost` TRIGGER `statisticalanswer_AFTER_INSERT` AFTER INSERT ON `statisticalanswer` FOR EACH ROW BEGIN
   DECLARE pointsToAdd integer;


   SELECT IF(NEW.age=0, 0, 2)+ IF(NEW.expertise IS NULL, 0, 2) + IF(NEW.sex=0, 0, 2) INTO pointsToAdd
   FROM points
   WHERE NEW.userId=userId
   AND NEW.productId=productId;


                IF (pointsToAdd>0)
                   AND EXISTS ( SELECT *
                                 FROM points P
                                 WHERE P.userId=NEW.userId
                                  AND P.productId=NEW.productId)
                    THEN
                        UPDATE Points
                            SET total=total+pointsToAdd
                        WHERE userId=NEW.userId
                        AND productId=NEW.productId;
                    ELSE
                        INSERT INTO Points(userId, productid, total)
                        VALUES(NEW.userId, NEW.productId, pointsToAdd);
END IF;
END
                 //quest'ultimo caso dovrebbe capitare raramente in quanto l'inserimento di una statisticalAnswer
                    avviene in teoria solamante l'inserimento di una marketingQuestion che scatena il precedente trigger.
                    Il caso è comunque considerato per mettersi al riparo nel caso in cui il dbms processi i trigger o
                    l'inserimento delle tuple con ordine differente da quanto aspettato

 */
