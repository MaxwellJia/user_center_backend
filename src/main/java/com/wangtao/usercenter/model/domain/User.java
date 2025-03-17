package com.wangtao.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Save users
 * @TableName user
 */
@TableName(value ="[user]")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User's names
     */
    private String username;

    /**
     * User's account
     */
    private String userAccount;

    /**
     * User's avatar
     */
    private String avatarUrl;

    /**
     * Gender of the user
     */
    private Integer gender;

    /**
     * User's password
     */
    private String userPassword;

    /**
     * User's phone number
     */
    private String phone;

    /**
     * User's email
     */
    private String email;

    /**
     * 0 is normal
     */
    private Integer userStatus;

    /**
     * 0 is normal user, 1 is administrator
     */
    private Integer userRole;

    /**
     * The time that created the table
     */
    private Date createTime;

    /**
     * The time that the table updated
     */
    private Date updateTime;

    /**
     * Whether the table was deleted
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * Security code
     */
    private String securityCode;
}