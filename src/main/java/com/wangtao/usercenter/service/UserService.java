package com.wangtao.usercenter.service;

import com.wangtao.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description Service layer, which used to accept requests from controller and do relative services
 * like register or log in
* @createDate 2024-07-29 16:44:08
*/
public interface UserService extends IService<User> {



    /**
     * Check whether the user register information is valid, finish register and encrypt password
     *
     * @param userAccount   user's account
     * @param userPassword  user's Password
     * @param checkPassword user's check password
     * @return -1 if invalid for any one of three above, user Id otherwise.
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * User login
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return Desensitized user information
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * Generate safty user used to desensitize user
     * @param originalUser
     * @return safty user
     */
    User getSaftyUser(User originalUser);
}
