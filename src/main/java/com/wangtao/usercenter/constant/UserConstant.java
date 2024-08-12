package com.wangtao.usercenter.constant;

/**
 * User Constant
 *
 * @author Wangtao
 */
public interface UserConstant {

    /**
     * The key name for saving user's information in session
     */
    String USER_LOGIN_STATE = "userLoginState";

    // ------ Authority ------

    /**
     * default authority (normal user)
     */
    int DEFAULT_ROLE = 0;

    /**
     * administrator authority (administrator user)
     */
    int ADMIN_ROLE = 1;


}
