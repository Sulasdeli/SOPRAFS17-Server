package ch.uzh.ifi.seal.soprafs17.web.rest;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.model.DTOs.GameDTO;
import ch.uzh.ifi.seal.soprafs17.model.DTOs.UserDTO;
import ch.uzh.ifi.seal.soprafs17.model.DTOs.siteBoardsDTO.*;
import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import ch.uzh.ifi.seal.soprafs17.model.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.model.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ilecipi on 13.04.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GameResourceTest {

    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected UserRepository userRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private GameResource gameResource;

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;


    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    public void oneTestForAll() throws Exception {
        this.addGame();
        this.listGames();
        this.getGame();
        this.createPlayer();
        this.startGame();
        this.getPlayer();
        this.listGames();
        this.getGame();
        this.fastForward();
    }


    public void addGame() throws Exception {
        // list games with size = 0
        List<GameDTO> gamesBefore = template.getForObject(base + "games", List.class);
        Assert.assertTrue(gamesBefore.size() == 0);

        Game gameRequest = new Game();
        gameRequest.setName("testGame");
        HttpEntity<Game> httpGameEntity = new HttpEntity<Game>(gameRequest);

        String ownerToken = null;
        User userRequest = new User();
        userRequest.setName("Test");
        userRequest.setUsername("testUser1");
        HttpEntity<User> httpUserEntity = new HttpEntity<User>(userRequest);

        // add Game with no userToken or false user Token
        ResponseEntity<String> responseGame = template.exchange(base + "games?token=" + ownerToken, HttpMethod.POST, httpGameEntity, String.class);
        Assert.assertEquals(null, responseGame.getBody());

        //add User
        ResponseEntity<UserDTO> responseUser = template.exchange(base + "users", HttpMethod.POST, httpUserEntity, UserDTO.class);
        ownerToken = responseUser.getBody().token;

        // add Game with userToken
        responseGame = template.exchange(base + "games?token=" + ownerToken, HttpMethod.POST, httpGameEntity, String.class);
        JSONObject jsonResponse = new JSONObject(responseGame.getBody());
        Long gameId = Long.parseLong(jsonResponse.get("id").toString());
        Game game = gameRepository.findOne(gameId);
        Assert.assertEquals(game.getId().intValue(), Integer.parseInt(jsonResponse.get("id").toString()));
    }

    public void listGames() throws Exception {
        List<GameDTO> gamesAfter = template.getForObject(base + "games", List.class);
        Assert.assertEquals(1, gamesAfter.size());
    }

    public void getGame() throws Exception {
        GameDTO game = template.getForObject(base + "games" + "/1", GameDTO.class);
        Assert.assertNotNull(game);
    }

    public void createPlayer() throws Exception {
        User userRequest2 = new User();
        userRequest2.setName("Test2");
        userRequest2.setUsername("testUser2");
        userRequest2.setToken("2");
        HttpEntity<User> httpUserEntity = new HttpEntity<User>(userRequest2);
        //Add second user to the game
        ResponseEntity<User> responseUser = template.exchange(base + "users", HttpMethod.POST, httpUserEntity, User.class);
        //http://localhost:8080/games/1/player?token=2
        ResponseEntity<String> responseGame = template.exchange(base + "games" + "/1" + "/player?token=" + userRequest2.getToken(), HttpMethod.POST, httpUserEntity, String.class);
        assertEquals(UserStatus.IN_A_LOBBY, userRepository.findByName("Test2").getStatus());
        String ownerUsername = gameRepository.findOne(1L).getOwner();
        String ownerToken = userRepository.findByUsername(ownerUsername).getToken();
        assertNotNull(ownerToken);
        User owner = userRepository.findByToken(ownerToken);
        HttpEntity<User> HttpOwnerEntity = new HttpEntity<>(owner);
        responseGame = template.exchange(base + "games" + "/1" + "?token=" + ownerToken, HttpMethod.PUT, HttpOwnerEntity, String.class);
        assertEquals(UserStatus.IS_READY, userRepository.findByToken(ownerToken).getStatus());

        responseGame = template.exchange(base + "games" + "/1" + "?token=" + userRequest2.getToken(), HttpMethod.PUT, httpUserEntity, String.class);

        assertEquals(UserStatus.IS_READY, userRepository.findByToken(userRequest2.getToken()).getStatus());
    }

    public void startGame() throws Exception {


        Game game = gameRepository.findOne(1L);
        assertNotNull(game);
        User owner = userRepository.findByUsername(game.getOwner());

        URL obj = new URL(base + "games" + "/1" + "/start");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        String urlParameters = "playerToken=" + owner.getToken();

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        assertEquals(GameStatus.RUNNING, gameRepository.findOne(1L).getStatus());

        ResponseEntity<MarketDTO> responseMarket = template.exchange(base + "games" + "/1" + "/market", HttpMethod.GET, null, MarketDTO.class);
        assertNotNull(responseMarket.getBody());

        ResponseEntity<PyramidDTO> responsePyramid = template.exchange(base + "games" + "/1" + "/pyramid", HttpMethod.GET, null, PyramidDTO.class);
        assertNotNull(responsePyramid.getBody());

        ResponseEntity<ObeliskDTO> responseObelisk = template.exchange(base + "games" + "/1" + "/obelisk", HttpMethod.GET, null, ObeliskDTO.class);
        assertNotNull(responseObelisk.getBody());

        ResponseEntity<BurialChamberDTO> responseBurialChamber = template.exchange(base + "games" + "/1" + "/burialChamber", HttpMethod.GET, null, BurialChamberDTO.class);
        assertNotNull(responseBurialChamber.getBody());

        ResponseEntity<TempleDTO> responseTemple = template.exchange(base + "games" + "/1" + "/temple", HttpMethod.GET, null, TempleDTO.class);
        assertNotNull(responseTemple.getBody());


    }

    public void getPlayer() throws Exception {
        UserDTO player = template.getForObject(base + "games" + "/1" + "/players" + "/1", UserDTO.class);
        assertNotNull(player);
    }

    public void fastForward() throws Exception {
        //TODO: check the size round everytime and at the end the status of the game
        ResponseEntity<GameDTO> responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(1, responseGameDTO.getBody().rounds.size());

        ResponseEntity<String> responseGame = template.exchange(base + "games" + "/1" + "/fastforwardoneround", HttpMethod.PUT, null, String.class);
        assertEquals(HttpStatus.ACCEPTED, responseGame.getStatusCode());
        responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(2, responseGameDTO.getBody().rounds.size());

        responseGame = template.exchange(base + "games" + "/1" + "/fastforwardoneround", HttpMethod.PUT, null, String.class);
        assertEquals(HttpStatus.ACCEPTED, responseGame.getStatusCode());
        responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(3, responseGameDTO.getBody().rounds.size());

        responseGame = template.exchange(base + "games" + "/1" + "/fastforwardoneround", HttpMethod.PUT, null, String.class);
        assertEquals(HttpStatus.ACCEPTED, responseGame.getStatusCode());
        responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(4, responseGameDTO.getBody().rounds.size());

        responseGame = template.exchange(base + "games" + "/1" + "/fastforwardoneround", HttpMethod.PUT, null, String.class);
        assertEquals(HttpStatus.ACCEPTED, responseGame.getStatusCode());
        responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(5, responseGameDTO.getBody().rounds.size());

        responseGame = template.exchange(base + "games" + "/1" + "/fastforwardoneround", HttpMethod.PUT, null, String.class);
        assertEquals(HttpStatus.ACCEPTED, responseGame.getStatusCode());
        responseGameDTO = template.exchange(base + "games" + "/1", HttpMethod.GET, null, GameDTO.class);
        assertEquals(6, responseGameDTO.getBody().rounds.size());


    }


}