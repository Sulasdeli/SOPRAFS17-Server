package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.Round;
import ch.uzh.ifi.seal.soprafs17.model.entity.Stone;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.AddStoneToShipMove;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.AMove;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.SailShipMove;
import ch.uzh.ifi.seal.soprafs17.model.entity.ships.AShip;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.SiteBoard;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.StoneBoard;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.Temple;
import ch.uzh.ifi.seal.soprafs17.model.repository.*;
import ch.uzh.ifi.seal.soprafs17.service.RuleEngine.RuleBook;
import ch.uzh.ifi.seal.soprafs17.service.ValidatorEngine.ValidatorManager;
import ch.uzh.ifi.seal.soprafs17.service.ValidatorEngine.exception.NotCurrentPlayerException;
import ch.uzh.ifi.seal.soprafs17.service.ValidatorEngine.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by erion on 29.03.17.
 */
@Service
@Transactional
public class MoveService {


//    @Autowired
//    private ShipRepository shipRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

//    @Autowired
//    private MoveRepository moveRepo;
//
//    @Autowired
//    private SiteBoardRepository siteBoardRepo;
//
//    @Autowired
//    private RuleBook ruleBook;

//    @Autowired
//    public MoveService(MoveRepository moveRepository) {
//        this.moveRepo = moveRepository;
//    }
//
//
//    public AMove getMove(Long moveId){
//        return moveRepo.findOne(moveId);
//    }
//
//    public void addStoneToShip(Game game, AMove move){
//        ruleBook.applyRule(game,move);
//    }
//    public void addStoneToSiteBoard(Long siteBoardId,String playerToken,Long gameId,Long shipId){
//        StoneBoard stoneBoard = siteBoardRepo.findById(siteBoardId);
//        Game game = gameRepo.findOne(gameId);
//        User player = userRepo.findByToken(playerToken);
//        AShip dockedShip = shipRepo.findById(shipId);
//        if(player == game.getCurrentPlayer() /*&& temple.getDiscriminatorValue().equals("temple")*/){
//            for(int i = dockedShip.getStones().length-1; i>=0;i--){
//                if(dockedShip.getStones()[i] != null){
//                    stoneBoard.addStone(dockedShip.getStones()[i]);
//                }
//            }
//            siteBoardRepo.save(stoneBoard);
//            userRepo.save(player);
//            game.findNextPlayer();
//            int index = (game.getPlayers().lastIndexOf(game.getCurrentPlayer())+1)%game.getPlayers().size();
//            game.setNextPlayer(game.getPlayers().get(index));
//            gameRepo.save(game);
//        }
//    }
//    public void sailShip(Game game,AMove move){
//        ruleBook.applyRule(game,move);
//        }
//    public void getStone(Game game,AMove move){
//        ruleBook.applyRule(game,move);
//    }
    public SiteBoard findSiteboardsByType(String siteBoardType,Long gameId){
        List<SiteBoard> siteBoards = gameRepo.findOne(gameId).getSiteBoards();
        SiteBoard siteBoard= null;
        if (!siteBoards.isEmpty()) {
            for (SiteBoard s : siteBoards) {
                if (s.getDiscriminatorValue().equals(siteBoardType)) {
                    siteBoard=s;
                }
            }
        }
        return siteBoard;
    }
}
