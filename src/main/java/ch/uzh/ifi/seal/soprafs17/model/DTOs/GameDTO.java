package ch.uzh.ifi.seal.soprafs17.model.DTOs;

import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by ilecipi on 01.04.17.
 */
public class GameDTO {
    GameDTO(){}
    public GameDTO(Long id, String name, String owner, GameStatus status, Long currentPlayer, Long nextPlayer,
                   List<Long> rounds, List<UserDTO> players, List<Long> siteBoards, Map<String, Integer> points, Map<Integer, String> marketCards) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.status = status;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
        this.rounds = rounds;
        this.players = players;
        this.siteBoards = siteBoards;
        this.points=points;
        this.marketCards=marketCards;
    }

    public Long id;
    public String name;
    public String owner;
    public GameStatus status;
    public Long currentPlayer;
    public Long nextPlayer;
    public List<Long> rounds;
    public List<UserDTO> players;
    public List<Long> siteBoards;
    public Map<String,Integer> points;
    public Map<Integer, String> marketCards;
}
