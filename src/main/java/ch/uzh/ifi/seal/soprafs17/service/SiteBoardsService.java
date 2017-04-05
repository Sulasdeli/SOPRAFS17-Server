package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.Stone;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.Pyramid;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.SiteBoard;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.StoneBoard;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.Temple;
import ch.uzh.ifi.seal.soprafs17.model.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.SiteBoardRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by erion on 21.03.17.
 */
@Service
@Transactional
public class SiteBoardsService {

//    @Autowired
//    private TempleRepository templeRepo;

    @Autowired
    private SiteBoardRepository siteBoardRepo;
    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;



    public String addTemple(Long gameId){
        Game game = gameRepo.findOne(gameId);
        StoneBoard temple = new Temple(game.getPlayers().size());
        game.getSiteBoards().add(temple);
        temple.setGame(game);
        temple=siteBoardRepo.save(temple);

        gameRepo.save(game);
        return "/game/"+gameId + "/" + temple.getId();
    }

    public StoneBoard getTemple(Long templeId){
        StoneBoard temple = siteBoardRepo.findById(templeId);
        return temple;
    }

    public String addPyramid(Long gameId){
        Game game = gameRepo.findOne(gameId);
        StoneBoard pyramid = new Pyramid();
        game.getSiteBoards().add(pyramid);
        pyramid.setGame(game);
        pyramid = siteBoardRepo.save(pyramid);
        game = gameRepo.save(game);
        return "/game/+gameId" + "/" + pyramid.getId();
    }

    public StoneBoard getPyramid(Long pyramidId){
        return siteBoardRepo.findById(pyramidId);
    }



}
