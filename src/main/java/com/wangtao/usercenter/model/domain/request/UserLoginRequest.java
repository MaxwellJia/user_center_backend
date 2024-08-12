package com.wangtao.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User register request body
 * Used to be the class of log in request for controller
 *
 * @author wangtao
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

}
