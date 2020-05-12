package com.smc.service;

import com.smc.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Rollback
    public void findAllTest() {
        List<User> userList = userService.findAll();
        Assert.assertThat(userList.size(), greaterThan(0));
    }

    @Test
    @Rollback
    public void findUserByIdTest() {
        User user = userService.findUserById(1);
        Assert.assertThat(user.getId(), is(1));
        Assert.assertThat(user.getId(), not(2));
    }

    @Test
    @Rollback
    public void updateUserTest() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test123");
        boolean result = userService.updateUser(user);
        Assert.assertThat(result, is(true));
    }

}
