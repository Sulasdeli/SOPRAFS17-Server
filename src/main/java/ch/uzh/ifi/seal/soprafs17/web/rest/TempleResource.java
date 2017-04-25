package ch.uzh.ifi.seal.soprafs17.web.rest;

import ch.uzh.ifi.seal.soprafs17.model.DTOs.siteBoardsDTO.TempleDTO;
import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.SiteBoard;
import ch.uzh.ifi.seal.soprafs17.model.entity.siteboards.Temple;
import ch.uzh.ifi.seal.soprafs17.model.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.SiteBoardRepository;
import ch.uzh.ifi.seal.soprafs17.service.SiteBoardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by erion on 20.03.17.
 */
@RestController
public class TempleResource extends GenericResource {

    static final String CONTEXT = "/games";
    @Autowired
    SiteBoardsService siteBoardsService;
    @Autowired
    GameRepository gameRepo;
    @Autowired
    SiteBoardRepository siteBoardRepo;

    @RequestMapping(value = CONTEXT + "/{gameId}/temple")
    @ResponseStatus(HttpStatus.OK)
    public TempleDTO getTemple(@PathVariable Long gameId) {
        Game game = gameRepo.findOne(gameId);
        Temple temple = gameRepo.findOne(gameId).getTemple();
        if (temple != null) {
            TempleDTO templeDTO = new TempleDTO(temple.getId(), temple.getStones(), gameId, temple.isOccupied(), temple.getInsertIndex(), temple.getCompletedRows());
            return templeDTO;
        } else {
            return null;
        }
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/temple/points")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> getTemplePoints(@PathVariable Long gameId) {
        List<SiteBoard> siteBoards = gameRepo.findOne(gameId).getSiteBoards();
        Temple temple = null;
        if (!siteBoards.isEmpty()) {
            for (SiteBoard s : siteBoards) {
                if (s.getDiscriminatorValue().equals("temple")) {
                    temple = (Temple) s;
                }
            }
        }
        return siteBoardsService.getTemplePoints(temple.getId());
    }
}