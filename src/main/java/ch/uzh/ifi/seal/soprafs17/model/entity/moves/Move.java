package ch.uzh.ifi.seal.soprafs17.model.entity.moves;

import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.Round;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Inheritance
public abstract class Move implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="USER_ID")
//	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
	private User user;

	@OneToOne
	@JoinColumn(name="ROUND_ID")
	private Round round;
	
    @ManyToOne
    @JoinColumn(name="GAME_ID")
	private Game game;

	Move(){}
	Move(User user,Game game,Round round){
		this.user=user;
		this.game=game;
		this.round=round;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}


	public abstract Game makeMove(Game game);
}