package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.Move;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import ch.uzh.ifi.seal.soprafs17.model.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.TempleRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by erion on 13.03.17.
 */
@Service
@Transactional
public class GameService {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final String CONTEXT = "/games";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private SiteBoardsService siteBoardsService;



    public UserRepository getUserRepo() {
        return userRepo;
    }

    public GameRepository getGameRepo() {
        return gameRepo;
    }

    public List<Game> listGames(){
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);
        return result;
    }

    public String addGame(Game game, String userToken){
        User owner = userRepo.findByToken(userToken);
        if (owner != null&&owner.getStatus()!=UserStatus.IS_PLAYING) {
//            userRepo.save(owner);
//            gameRepo.save(game);
            game.setOwner(owner.getUsername());
            game.setCurrentPlayer(owner);
            game.setStatus(GameStatus.PENDING);
            game = gameRepo.save(game);
            owner.getGames().add(game);
            owner.setStatus(UserStatus.ONLINE);
            userRepo.save(owner);
//            gameRepo.save(game);

            return "/" + game.getId();
        }

        return null;
    }

    public Game getGame(Long gameId){
        Game game = gameRepo.findOne(gameId);
        return game;
    }

    public void startGame(Long gameId, String userToken){

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        //the game can be started only from the owner
        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())
                && game.getPlayers().size()>= GameConstants.MIN_PLAYERS&&game.getPlayers().size()<=GameConstants.MAX_PLAYERS
                && game.getStatus() != GameStatus.RUNNING){

            //The game cannot start if not every player is ready
            boolean allPlayersReady = true;
            for(User u : game.getPlayers()){
                if(u.getStatus()!=UserStatus.IS_READY){
                    allPlayersReady=false;
                }
            }
            if(allPlayersReady) {
                siteBoardsService.addTemple(game.getId());
                game.setCurrentPlayer(owner);
                // TODO: Start game in GameService
                game.setStatus(GameStatus.RUNNING);
                for (User u : game.getPlayers()) {
                    u.setStatus(UserStatus.IS_PLAYING);
                    userRepo.save(u);
                }

                gameRepo.save(game);
            }
        }
    }

    public void stopGame(Long gameId, String userToken){
        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            for(User u :game.getPlayers()){
                u.setStatus(UserStatus.OFFLINE);
                u.getGames().remove(game);
                u.setColor(null);
                userRepo.save(u);
            }
            gameRepo.delete(game);
        }
    }

    public List<Move> listMoves(Long gameId){

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves();
        }

        return null;
    }

    public void addMove(){ // TODO Mapping into Move + execution of move


    }


    public Move getMove(Long gameId,Integer moveId){
        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves().get(moveId);
        }

        return null;
    }

    public List<User> listPlayers(Long gameId){
        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getPlayers();
        }

        return null;
    }

    public String addUser(Long gameId,String userToken){
        Game game = gameRepo.findOne(gameId);
        User player = userRepo.findByToken(userToken);

        if (game != null && player != null && game.getPlayers().size() < GameConstants.MAX_PLAYERS) {
            player.getGames().add(game);
            player.setStatus(UserStatus.ONLINE);
            if(game.getPlayers().size()==1){                //Set the second player as the nextPlayer
                game.setNextPlayer(player);
            }
            game.getPlayers().add(player);
            userRepo.save(player);
            gameRepo.save(game);
            logger.debug("Game: " + game.getName() + " - player added: " + player.getUsername());
            return CONTEXT + "/" + gameId + "/player/" + (game.getPlayers().size());
        } else {
            logger.error("Error adding player with token: " + userToken);
        }
        return null;
    }

    public User getPlayer(Long gameId,Integer playerId){
        Game game = gameRepo.findOne(gameId);

        return game.getPlayers().get(playerId);
    }

    public void createPlayer(Long gameId, String userToken){

        User user = userRepo.findByToken(userToken);
        Game game = gameRepo.findOne(gameId);
        //assign color to user
        if (game.getPlayers().contains(user)) {
            boolean colorNotChosen = true;
            String color;
            while (colorNotChosen) {
                Random rn = new Random();
                int i = rn.nextInt() % 4;
                if (i == 0) {
                    color = "black";
                } else if (i == 1) {
                    color = "white";
                } else if (i == 2) {
                    color = "brown";
                } else {
                    color = "grey";
                }
                if (!game.getColors().get(color)) {
                    user.setColor(color);
                    game.getColors().put(color, true);
                    user.setStatus(UserStatus.IS_READY);
                    colorNotChosen = false;

                }
            }

        }
        gameRepo.save(game);
        userRepo.save(user);
    }

}