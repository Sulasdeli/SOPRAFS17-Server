package ch.uzh.ifi.seal.soprafs17.model.entity;

import ch.uzh.ifi.seal.soprafs17.model.entity.marketCards.AMarketCard;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.AMove;
import ch.uzh.ifi.seal.soprafs17.model.entity.ships.AShip;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erion on 20.03.17.
 */
@Entity
public class Round implements Serializable {


    @Column
    Stone[] stonesLeverCard;
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "GAME_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private Game game;
    @OneToMany(mappedBy = "round")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private List<AMove> AMoves;
    //TODO: REVISE WHETHER WE NEED mappedBy or JoinColumn
    @OneToMany
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private List<AShip> ships;
    @OneToMany
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private List<AMarketCard> marketCards;
    private boolean isActionCardHammer;
    private int isActionCardChisel;
    private int isActionCardSail;
    @ElementCollection
    private List<String> userColorsLeverCard = new ArrayList<>();
    @JsonIgnore
    private boolean isActionCardLever = false;
    @ElementCollection
    private List<String> ListActionCardLever = new ArrayList<>();
    @JsonIgnore
    @Column
    private boolean immediateCard;

    public Round() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<AMove> getAMoves() {
        return AMoves;
    }

    public void setAMoves(List<AMove> AMoves) {
        this.AMoves = AMoves;
    }

    public List<AShip> getShips() {
        return ships;
    }

    public void setShips(List<AShip> ships) {
        this.ships = ships;
    }

    public List<AMarketCard> getMarketCards() {
        return marketCards;
    }

    public void setMarketCards(List<AMarketCard> marketCards) {
        this.marketCards = marketCards;
    }

    public boolean isActionCardLever() {
        return isActionCardLever;
    }

    public void setActionCardLever(boolean actionCardLever) {
        isActionCardLever = actionCardLever;
    }

    public List<String> getUserColorsLeverCard() {
        return userColorsLeverCard;
    }

    public void setUserColorsLeverCard(List<String> userColorsLeverCard) {
        this.userColorsLeverCard = userColorsLeverCard;
    }

    public Stone[] getStonesLeverCard() {
        return stonesLeverCard;
    }

    public void setStonesLeverCard(Stone[] stonesLeverCard) {
        this.stonesLeverCard = stonesLeverCard;
    }

    public List<String> getListActionCardLever() {
        return ListActionCardLever;
    }

    public void setListActionCardLever(List<String> listActionCardLever) {
        ListActionCardLever = listActionCardLever;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isImmediateCard() {
        return immediateCard;
    }

    public void setImmediateCard(boolean immediateCard) {
        this.immediateCard = immediateCard;
    }

    public boolean isActionCardHammer() {
        return isActionCardHammer;
    }

    public void setActionCardHammer(boolean actionCardHammer) {
        isActionCardHammer = actionCardHammer;
    }

    public int getIsActionCardChisel() {
        return isActionCardChisel;
    }

    public void setIsActionCardChisel(int isActionCardChisel) {
        this.isActionCardChisel = isActionCardChisel;
    }

    public int getIsActionCardSail() {
        return isActionCardSail;
    }

    public void setIsActionCardSail(int isActionCardSail) {
        this.isActionCardSail = isActionCardSail;
    }
}
