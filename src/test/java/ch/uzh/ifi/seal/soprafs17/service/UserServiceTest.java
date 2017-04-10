package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.User;
import ch.uzh.ifi.seal.soprafs17.model.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;


/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class UserServiceTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private List<Game> games;

    @Test
    public void createUser() {
//        Assert.assertNull(userRepository.findByToken("t123"));
        User user = userService.createUser("testName", "testUsername", "t123", UserStatus.ONLINE, games);
        assertNotNull(userRepository.findByToken("t123"));
//        Assert.assertEquals(userRepository.findByToken("t123").getToken(), user.getToken());
    }

//    @Test
//    public void deleteUser() {
//        User user = userService.createUser("testName", "testUsername", "t123", UserStatus.ONLINE, games);
//        userService.deleteUser(user.getId());
//        Assert.assertNull(userService.getUser(user.getId()));
//    }


}
