package ch.uzh.ifi.seal.soprafs17.model.entity.marketCards;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by tonio99tv on 10/04/17.
 */
@Entity
@DiscriminatorValue("hammer")
public class Hammer extends AMarketCard implements MCAction {
    public String cardType = "HAMMER";
    @Id
    @GeneratedValue
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCardType() {
        return this.cardType;
    }


}
