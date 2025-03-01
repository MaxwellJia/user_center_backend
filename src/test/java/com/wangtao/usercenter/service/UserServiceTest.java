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

    /**
     * Test for add user to database
     * A user will be created bt testing this, delete it if you want
     */
    @Test
    void testAddUser() {
//        User user = new User();
//        user.setUsername("Maxwell");
//        user.setUserAccount("Maxwell");
//        user.setAvatarUrl("https://www.code-nav.cn/user/1813081856547151874");
//        user.setGender(0);
//        user.setUserPassword("8bf75d25716494a5e1ae63de79db741a");//12345678
//        user.setPhone("123");
//        user.setEmail("456");
//
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        assertTrue(result);
    }

    /**
     * Test userRegister function.
     * The user initial information:
     * userAccount = "wangtao";
     * userPassword = "12345678";
     * checkPassword = "12345678";
     * Please delete the record after testing
     */
    @Test
    void userRegister() {
//
//        // Password is blank
//        String userAccount = "wangtao";
//        String userPassword = "";
//        String checkPassword = "12345678";
//        String securityCode = "1";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        // User account is less than 4
//        userPassword = "12345678";
//        userAccount = "wa";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        // Password is less than 8
//        userAccount = "wangtao";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        // No special symbols
//        userPassword = "12345678";
//        userAccount = "wang tao";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        // Check-password is the same as the password
//        userAccount = "wangtao";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        // No repeated account
//        checkPassword = "12345678";
//        userAccount = "123";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertEquals(-1, result);
//
//        userAccount = "wangtao";
//        result = userService.userRegister(userAccount, userPassword, checkPassword,securityCode);
//        Assert.assertTrue(result > 0);
    }
}