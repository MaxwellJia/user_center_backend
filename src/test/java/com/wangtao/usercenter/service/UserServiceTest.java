package com.wangtao.usercenter.service;
import java.util.Date;

import com.wangtao.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
/**
 * User service test
 * **/


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("Maxwell");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.code-nav.cn/user/1813081856547151874");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }
}