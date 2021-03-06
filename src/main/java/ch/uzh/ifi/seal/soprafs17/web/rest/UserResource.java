package ch.uzh.ifi.seal.soprafs17.web.rest;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs17.model.DTOs.UserDTO;
import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.AMove;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;


@RestController
@RequestMapping(UserResource.CONTEXT)
public class UserResource extends GenericResource {

    Logger logger = LoggerFactory.getLogger(UserResource.class);

    static final String CONTEXT = "/users";
    public int counter = 1;

    @Autowired
    private UserService userService;

    /**
     *
     * @param user
     * @return the added user
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        logger.debug("addUser: " + user);
        String token =""+counter++;
        User u = userService.createUser(user.getName(),user.getUsername(), token, UserStatus.OFFLINE, new ArrayList<>());
        u= userService.login(u.getId());
        return u;
    }

    /**
     *
     * @return a list of users
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<UserDTO> listUsers() {
        logger.debug("listUsers");
        Iterable<User> users = userService.listUsers();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User u : users){
            List<Long> gamesId = new ArrayList<>();
            List<Long> movesId = new ArrayList<>();
            for(Game g : u.getGames()){
                gamesId.add(g.getId());
            }
            for(AMove m : u.getAMoves()){
                movesId.add(m.getId());
            }
            usersDTO.add(new UserDTO(u.getId(),u.getName(),u.getUsername(),u.getToken(),u.getStatus(),gamesId,movesId,u.getColor(),u.getSupplySled(),u.getMarketCards(),u.getStoneQuarry()));
        }
        return usersDTO;
    }


    /**
     *
     * @param userId
     * @return a user
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);
        User u = userService.getUser(userId);
        List<Long> gamesId = new ArrayList<>();
        List<Long> movesId = new ArrayList<>();
        if(u.getGames() != null) {
            for (Game g : u.getGames()) {
                gamesId.add(g.getId());
            }
        }
        if(u.getAMoves() != null) {
            for (AMove m : u.getAMoves()) {
                movesId.add(m.getId());
            }
        }
        return new UserDTO(u.getId(),u.getName(),u.getUsername(),u.getToken(),u.getStatus(),gamesId,movesId,u.getColor(),u.getSupplySled(),u.getMarketCards(),u.getStoneQuarry());
    }

    /**
     *
     * @param userId
     * @param userToken
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable Long userId, @RequestParam("token") String userToken) {
        logger.debug("getUser: " + userId);
        userService.logout(userId,userToken);
    }
}
