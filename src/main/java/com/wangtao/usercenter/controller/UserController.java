package com.wangtao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangtao.usercenter.common.BaseResponse;
import com.wangtao.usercenter.common.ErrorCode;
import com.wangtao.usercenter.common.ResultUtils;
import com.wangtao.usercenter.exception.BusinessException;
import com.wangtao.usercenter.model.domain.User;
import com.wangtao.usercenter.model.domain.request.UserLoginRequest;
import com.wangtao.usercenter.model.domain.request.UserRegisterRequest;
import com.wangtao.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request is null");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String securityCode = userRegisterRequest.getSecurityCode();
        // Check any blank for parameters
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, securityCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account or password or check password error");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, securityCode);
        return ResultUtils.success(result);
    }


    /**
     * User Log in Controller
     * @param userLoginRequest
     * @param request
     * @return User object
     */
    @PostMapping("/login")// Create a address or path after /user, user can access this address to log in
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request is null");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // Check any blank for parameters
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account or password is null");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }


    /**
     * User Log out Controller
     * @param request
     * @return User object
     */
    @PostMapping("/logout")// Create a address or path after /user, user can access this address to log out
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request is null");
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN, "current user is null");
        }
        long userId = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(userId);
        User saftyUser = userService.getSaftyUser(user);

        return ResultUtils.success(saftyUser);
    }


    /**
     * Fuzzy User Search (Administrator only)
     * @param params
     * @return list of users
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestParam Map<String, String> params, HttpServletRequest request) {
        // 权限校验
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "User is not admin");
        }

        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        params.forEach((key, value) -> {
            if (value != null && !value.trim().isEmpty()) {
                switch (key) {
                    case "username":
                    case "userAccount":
                    case "avatarUrl":
                    case "phone":
                    case "email":
                    case "securityCode":
                        queryWrapper.like(true, key, value);
                        break;
                    case "gender":
                    case "userStatus":
                    case "userRole":
                        queryWrapper.eq(parseInteger(value) != null, key, parseInteger(value));
                        break;
                    case "createTime":
                        queryWrapper.apply("Date(" + key + ") LIKE '" + value + "%'");
                        System.out.println("Date(" + key + ") LIKE '" + value + "%'");
                        break;
                    default:
                        // Ignore unrecognized parameters
                }
            }
        });

        // 执行查询并处理返回结果
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream()
                .map(userService::getSaftyUser)
                .collect(Collectors.toList());

        return ResultUtils.success(result);
    }

    // 安全解析 Integer
    private Integer parseInteger(String value) {
        try {
            return value != null ? Integer.parseInt(value.trim()) : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value: " + value);
            return null;
        }
    }


    /**
     * Update Users (Administrator only)
     * @return list of users
     */
    @PostMapping("/update")
    public BaseResponse<User> update(@RequestBody User user, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "User is not admin");
        }

        updateUsersByList(user);

        return ResultUtils.success(user);
    }

    @Transactional
    public void updateUsersByList(User user) {
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "Invalid user or user id");
        }
        boolean success = userService.updateById(user);
        if (!success) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Failed to update user: " + user.getId());
        }
    }


    /**
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestParam long id, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "User is not admin");
        }
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User's id can't be less than 0");
        }
        boolean result = userService.removeById(id);

        return ResultUtils.success(result);
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
