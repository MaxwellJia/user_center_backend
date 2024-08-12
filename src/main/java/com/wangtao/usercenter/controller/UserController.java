package com.wangtao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangtao.usercenter.model.domain.User;
import com.wangtao.usercenter.model.domain.request.UserLoginRequest;
import com.wangtao.usercenter.model.domain.request.UserRegisterRequest;
import com.wangtao.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wangtao.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.wangtao.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * User Interface
 * @description Accept requests from the HTTP(json always), transfer them to entity to use relative parameters
 * then call service to deal with these request and return results to the front.
 * @author Wangtao
 */
@RestController // This class is to process requests from HTTP
@RequestMapping("/user") // Create a first-class address
public class UserController {
    @Resource // Inject userService
    private UserService userService;


    /**
     * User Register Controller
     * @param userRegisterRequest
     * @return user id Long
     */
    @PostMapping("/register") // Create a address or path after /user, user can access this address to register
    //@RequestBody is to reflect parameter coming in to a class
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // Check any blank for parameters
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }


    /**
     * User Log in Controller
     * @param userLoginRequest
     * @param request
     * @return User object
     */
    @PostMapping("/login")// Create a address or path after /user, user can access this address to log in
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // Check any blank for parameters
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }


    /**
     * Fuzzy User Search (Administrator only)
     * @param username
     * @return list of users
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){
        if (!isAdmin(request)){
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username); //Fuzzy query
        }
        List<User> userList = userService.list(queryWrapper);


        return userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSaftyUser(user);
        }).collect(Collectors.toList());
    }


    /**
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    public boolean deleteUsers(@RequestParam long id, HttpServletRequest request){
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     *  Check whether user is an administrator
     * @param request
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request){
        // Get session (input user)
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        // Check if session is null or user is an administrator
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }



}
