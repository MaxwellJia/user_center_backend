package com.wangtao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangtao.usercenter.common.ErrorCode;
import com.wangtao.usercenter.exception.BusinessException;
import com.wangtao.usercenter.model.domain.User;
import com.wangtao.usercenter.service.UserService;
import com.wangtao.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wangtao.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author lenovo
* @description implement for UserService
* @createDate 2024-07-29 16:44:08
 * implement of UserService
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * SALT that added to the encrypted password
     */
    private static final String SALT = "abcd";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String securityCode) {
        /* return negative number means invalid **/
        // todo negative number need to be modified to error
        // 1. check
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, securityCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "parameter is empty");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account length is less than 4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User password length is less than 8");
        }
        if (securityCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "security code length is greater than 5");
        }

        //There is no special symbols for user account
        String validPattern = "[ !@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "there are special characters in user account");
        }

        // Check-password is the same with password
        if (!checkPassword.equals(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password is not the same as the check password");
        }

        // No repeated account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "account is repeated");
        }

        // No repeated securityCode
//        queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("securityCode", securityCode);
//        count = userMapper.selectCount(queryWrapper);
//        if (count > 0){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "security code is repeated");
//        }

        // 2. encryption
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. Insert data
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setSecurityCode(securityCode);
        user.setIsDelete(0);
        boolean saveResult = this.save(user);

        if (!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "user can't be saved");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        /* return null means invalid **/
        // 1. check
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account or password is empty");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user account length is less than 4");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "user password length is less than 8");
        }

        //There is no special symbols for user account
        String validPattern = "[ !@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }

        // 2. encryption and check whether this user is in our database and the information is correct

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // Check whether user exists in our database
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // user information doesn't exist in our database
        if (user == null){
            log.info("user login failed, userAccount cannot match password");
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "user does not exist");
        }// if there is a user login too many times, do something here

        // 3.User desensitisation
        User safeUser = getSaftyUser(user);

        // 4.Memorize user's login session (status)
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);

        return safeUser;
    }


    /**
     * Generate safty user used to desensitize user
     * @param originalUser
     * @return safty user
     */
    @Override
    public User getSaftyUser(User originalUser){
        if (originalUser == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "there is no original user in getSaftyUser");
        }
        User safeUser = new User();
        safeUser.setId(originalUser.getId());
        safeUser.setUsername(originalUser.getUsername());
        safeUser.setUserAccount(originalUser.getUserAccount());
        safeUser.setAvatarUrl(originalUser.getAvatarUrl());
        safeUser.setGender(originalUser.getGender());
        safeUser.setPhone(originalUser.getPhone());
        safeUser.setEmail(originalUser.getEmail());
        safeUser.setUserRole(originalUser.getUserRole());
        safeUser.setUserStatus(originalUser.getUserStatus());
        safeUser.setCreateTime(originalUser.getCreateTime());
        safeUser.setSecurityCode(originalUser.getSecurityCode());
        return safeUser;
    }

    /**
     * Request user logout
     * @param request
     * @return
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        // Remove user login status
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




